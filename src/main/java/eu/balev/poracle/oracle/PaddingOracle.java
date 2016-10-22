package eu.balev.poracle.oracle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import eu.balev.poracle.encrypt.Encryption;
import eu.balev.poracle.encrypt.EncryptionConstants;
import eu.balev.poracle.key.KeyHolder;

/**
 * This is a padding oracle. It answers just one question - is the encryption structure correctly padded or not.
 */
public enum PaddingOracle
{
    INSTANCE;

    /**
     * Returns true is the encrypted text is correctly padded.
     * 
     * @param encrypted
     * @return
     */
    public boolean isCorrectlyPadded(Encryption encrypted)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(EncryptionConstants.CIPHER_SPEC);
            cipher.init(Cipher.DECRYPT_MODE, KeyHolder.INSTANCE.getSecretKey(), new IvParameterSpec(encrypted.iv));
            cipher.doFinal(encrypted.encrypted);
        }
        catch(Exception exc)
        {
            if (exc instanceof BadPaddingException)
            {
                return false;
            }
        }
        return true;
    }
}
