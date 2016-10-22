package eu.balev.poracle.encrypt;

import java.security.AlgorithmParameters;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import eu.balev.poracle.key.KeyHolder;

/**
 * A small utility that encrypts a sequence of bytes.
 */
public enum EncryptUtils
{
    INSTANCE;
    
    public Encryption encrypt(byte bytes[])
    {
        try
        {
            Cipher cipher = Cipher.getInstance(EncryptionConstants.CIPHER_SPEC);
            cipher.init(Cipher.ENCRYPT_MODE, KeyHolder.INSTANCE.getSecretKey());

            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] encrypted = cipher.doFinal(bytes);

            return Encryption.create(iv, encrypted);
        }
        catch(Exception exc)
        {
            throw new RuntimeException(exc);
        }
    }
}
