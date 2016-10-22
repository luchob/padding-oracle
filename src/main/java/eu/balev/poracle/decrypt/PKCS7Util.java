package eu.balev.poracle.decrypt;

import java.util.Arrays;

import javax.crypto.BadPaddingException;

public enum PKCS7Util
{
    INSTANCE;
    
    public byte[] stripPadding(byte[] paddedText) throws BadPaddingException
    {
        byte lastByte = paddedText[paddedText.length - 1];
        if (lastByte < 1 || lastByte > 16)
        {
            throw new BadPaddingException("The provided bytes have no valid paddig! Bytes: " + Arrays.toString(paddedText));
        }
        byte[] stripped = new byte[paddedText.length - lastByte];
        
        System.arraycopy(paddedText, 0, stripped, 0, stripped.length);
        
        return stripped;
    }
}
