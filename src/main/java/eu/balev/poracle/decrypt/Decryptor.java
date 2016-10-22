package eu.balev.poracle.decrypt;

import static eu.balev.poracle.encrypt.EncryptionConstants.CIPHER_BLOCK_SIZE;

import javax.crypto.BadPaddingException;

import eu.balev.poracle.encrypt.Encryption;
import eu.balev.poracle.oracle.PaddingOracle;

/**
 * This utility tries to decrypt the last plain text block of the encrypted text. If the encrypted text has only one
 * block the utility is clever enough to use the IV for the padding oracle attack.
 */
public enum Decryptor
{
    INSTANCE;

    /**
     * Decrypts all of the blocks and strips the PKCS7 padding.
     * 
     * @param encrypted
     * @return
     */
    public byte[] decryptAllBlocks(Encryption encrypted)
    {
        byte[] plainText = new byte[encrypted.getNumberOfBlocks() * CIPHER_BLOCK_SIZE];

        for (int blockNum = encrypted.getNumberOfBlocks(); blockNum >= 1; blockNum--)
        {
            byte[] decryptedBlock = Decryptor.INSTANCE.decryptLastBlock(encrypted);
            if (blockNum > 1)
                encrypted = encrypted.withoutLastBlock();
            System.arraycopy(decryptedBlock, 0, plainText, (blockNum - 1) * CIPHER_BLOCK_SIZE, CIPHER_BLOCK_SIZE);
        }

        try
        {
            plainText = PKCS7Util.INSTANCE.stripPadding(plainText);
        }
        catch(BadPaddingException e)
        {
            // should not happen
            throw new RuntimeException(e);
        }

        return plainText;
    }

    /**
     * Decrypts the last block of the encryption sequence by using a padding Oracle.
     * 
     * @param encrypted
     *            the encrypted text
     * @return the decrypted last block of the text including the PKCS7 padding.
     */
    public byte[] decryptLastBlock(Encryption encrypted)
    {
        // we will store the decrypted bytes here
        byte decryptedBlock[] = new byte[CIPHER_BLOCK_SIZE];
        // clone the encrypted struct so that we can easily modify it
        Encryption encStruct = Encryption.create(encrypted);

        byte[] bytesToPlay = getBytesToPlay(encStruct);
        // we start at the last byte of the last but one block, this might be the iv as well
        int byteToRotateIdx = bytesToPlay.length > CIPHER_BLOCK_SIZE ? bytesToPlay.length - CIPHER_BLOCK_SIZE - 1
                        : CIPHER_BLOCK_SIZE - 1;

        for (int decryptedByteIdx = decryptedBlock.length - 1; decryptedByteIdx >= 0; decryptedByteIdx--)
        {
            int fakePaddingByte = decryptedBlock.length - decryptedByteIdx;// eg, idx 15 -> padding -> 0x01

            // the guess for the last one is, e.g. b ^ 0x01 ^ i, where i e [0;255]
            // when i hits the correct plain text we will have correct padding
            byte guessBase = (byte)((bytesToPlay[byteToRotateIdx] ^ fakePaddingByte) & 0xff);
            // used to resolve ambiguities,
            // see here: http://crypto.stackexchange.com/questions/40800/is-the-padding-oracle-attack-deterministic
            int ambiguityByte = 0;
            boolean bAlreadyHit = false;

            for (int i = 0; i < 255; i++)
            {
                bytesToPlay[byteToRotateIdx] = (byte)((guessBase ^ i) & 0xff);

                if (PaddingOracle.INSTANCE.isCorrectlyPadded(encStruct))
                {
                    if (bAlreadyHit)
                    {
                        i = 0;
                        bytesToPlay[byteToRotateIdx - 1] = (byte)(ambiguityByte & 0xff);
                        bAlreadyHit = false;
                        ambiguityByte++;
                        continue;
                    }

                    bAlreadyHit = true;
                    decryptedBlock[decryptedByteIdx] = (byte)(i & 0xff);

                }
            }

            // "fix" the other bytes so that we can move on, clone the original thingy
            encStruct = Encryption.create(encrypted);
            bytesToPlay = getBytesToPlay(encStruct);

            byteToRotateIdx--;
            // now we have to adjust all the bytes in the 'right' direction

            int fixStartByteIdx = byteToRotateIdx + 1;
            int fixEndByteIdx = (((byteToRotateIdx / CIPHER_BLOCK_SIZE) * CIPHER_BLOCK_SIZE) + CIPHER_BLOCK_SIZE - 1);

            for (int idx = fixStartByteIdx; idx <= fixEndByteIdx; idx++)
            {
                int decryptedIdx = idx % CIPHER_BLOCK_SIZE;
                byte fakeByte = (byte)((bytesToPlay[idx] ^ (fakePaddingByte + 1) ^ decryptedBlock[decryptedIdx])
                                & 0xff);
                bytesToPlay[idx] = fakeByte;
            }
            // break;
        }

        return decryptedBlock;

    }

    private static byte[] getBytesToPlay(Encryption enc)
    {
        return enc.getNumberOfBlocks() > 1 ? enc.encrypted : enc.iv;
    }
}
