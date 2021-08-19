package oram.pathoram;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
CryptoManager is the encryption/decryption class.
It has one main function for encryption decryption (encryptDecrypt),
which decides whether to encrypt or decrypt by reading the argument mode.
Encryption and decryption is done using AES-128 with CBC mode.
*/

public class CryptoManager
{
    public static String ALGORITHM = "AES";
    private static String AES_CBC_NO_PADDING = "AES/CBC/NoPadding";
    static String AES_CBC_PADDING = "AES/CBC/PKCS5Padding";
    static String AES_ECB_PADDING = "AES/ECB/PKCS5Padding";

    public static byte[] encrypt(final byte[] key, final byte[] IV, final byte[] message) throws Exception
    {
        return CryptoManager.encryptDecrypt(Cipher.ENCRYPT_MODE, key, IV, message);
    }

    public static byte[] decrypt(final byte[] key, final byte[] IV, final byte[] message) throws Exception
    {
        return CryptoManager.encryptDecrypt(Cipher.DECRYPT_MODE, key, IV, message);
    }

    private static byte[] encryptDecrypt(final int mode, final byte[] key, final byte[] IV, final byte[] message) throws Exception
    {
        final Cipher cipher = Cipher.getInstance(AES_CBC_NO_PADDING);
        final SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
        final IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(mode, keySpec, ivSpec);
        return cipher.doFinal(message);
    }
}