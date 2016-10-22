package eu.balev.poracle.encrypt;

/**
 * Represents an encrypted text with a block cipher.
 * 
 * This simple structure maintains the IV with which the encryption was started
 * as well as the cipher text blocks.
 */
public class Encryption
{
    public byte[] encrypted;
    public byte[] iv;
    
    private Encryption()
    {}
    
    public static Encryption create(byte[] iv, byte[] encrypted)
    {
        Encryption ret = new Encryption();
        
        ret.iv = iv.clone();
        ret.encrypted = encrypted.clone();
        
        return ret;
    }
    
    public static Encryption create(Encryption encr)
    {
        return create(encr.iv, encr.encrypted);
    }
    
    public int getNumberOfBlocks()
    {
        return encrypted.length / EncryptionConstants.CIPHER_BLOCK_SIZE;
    }
    
    public Encryption withoutLastBlock()
    {
        if (getNumberOfBlocks() == 1)
        {
            throw new IllegalStateException("Cannot strip the last block, because there is only one block left...");
        }
        
        byte remainingBlocks[] = new byte[encrypted.length - EncryptionConstants.CIPHER_BLOCK_SIZE];
        System.arraycopy(encrypted, 0, remainingBlocks, 0, remainingBlocks.length);
        
        return create(iv, remainingBlocks);
    }
}
