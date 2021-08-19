package oram.pathoram;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/*
Each Bucket object gets to hold 9 Blocks in current implementation.
*/

public class Bucket implements java.io.Serializable
{
    private
        int lvl;
        static int counter = -1;
        static int NUM = 9;
        Block[] block = new Block[NUM];
        SecretKey iv;
        static int AES_128 = 128;
        static String ALGORITHM = "AES";

    protected Bucket() throws NoSuchAlgorithmException {
        counter++;
        setIV();
        for (int i = 0; i < NUM; i++)
            block[i] = new Block(counter);
        setLevel(0);
    }

    public SecretKey getIV()
    {
        return this.iv;
    }

    public void setLevel(int lvl)
    {
        this.lvl = lvl;
    }

    protected void setIV() throws NoSuchAlgorithmException {
        // Create key & IV for AES algorithm.
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CryptoManager.ALGORITHM);
        keyGenerator.init(AES_128);
        this.iv = keyGenerator.generateKey();
    }
}