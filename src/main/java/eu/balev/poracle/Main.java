package eu.balev.poracle;

import static eu.balev.poracle.encrypt.EncryptionConstants.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.balev.poracle.decrypt.Decryptor;
import eu.balev.poracle.encrypt.EncryptUtils;
import eu.balev.poracle.encrypt.Encryption;

public class Main
{
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception
    {
        logger.debug("Going to try to encrypt a given text with " + CIPHER_SPEC
                        + " and decrypt it withouth knowledge of the key with a padding oracle attack.");
        // sample text
        String toEncrypt = "Hi, I'm Lucho! This is a test plain text, encrypted with " + CIPHER_SPEC + ".";

        // encrypted
        Encryption encrypted = EncryptUtils.INSTANCE.encrypt(toEncrypt.getBytes());

        // some logging
        logger.debug("Length of the text to encrypt is {} bytes.", toEncrypt.length());
        logger.debug("Length of the encrypted text is {}. Spread over {} blocks of {} bytes.",
                        encrypted.encrypted.length, encrypted.getNumberOfBlocks(), CIPHER_BLOCK_SIZE);

        // the actual decryption using the padding oracle attack
        byte plainText[] = Decryptor.INSTANCE.decryptAllBlocks(encrypted);

        logger.debug("Original  text is: {}", toEncrypt);
        logger.debug("Decrypted text is: {}", new String(plainText));
    }

}
