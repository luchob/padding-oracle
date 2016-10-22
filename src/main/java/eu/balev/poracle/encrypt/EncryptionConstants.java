package eu.balev.poracle.encrypt;

/**
 * Some constants which describe the encryption algorithm.
 */
public class EncryptionConstants
{
    /**
     * The algorithm, mode of operation and padding as understood by JSSE.
     */
    public static final String CIPHER_SPEC = "AES/CBC/PKCS5Padding";
    
    /**
     * The block size in bytes of the chosen block cipher, e.g. 16 bytes for AES.
     */
    public static final int CIPHER_BLOCK_SIZE = 16;
}
