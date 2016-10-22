package eu.balev.poracle.key;

import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A holder for a sample secret key that will be used to encrypt a text and execute a padding oracle attack.
 */
public enum KeyHolder
{
    INSTANCE;

    private final SecretKey secretKey;

    private KeyHolder()
    {
        String key = "my key is this one :-)";

        SecretKeyFactory factory;
        try
        {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key.toCharArray(), "salt".getBytes(), 65536, 128);
            SecretKey tmp = factory.generateSecret(spec);
            secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        }
        catch(Exception ex)
        {
            throw new RuntimeException();
        }
    }

    public SecretKey getSecretKey()
    {
        return secretKey;
    }
}
