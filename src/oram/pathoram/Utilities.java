package oram.pathoram;

import java.util.BitSet;

public class Utilities
{
    // This function transforms a BitSet object to a byte array object.
    public static byte[] toByteArray1(BitSet bs)
    {
        byte[] bytes = new byte[16];
        int size = bs.size();
        for (int i = 0; i < 128; i++)
        {
            if (bs.get(i))
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
        }
        return bytes;
    }

    private static Boolean isBitSet(byte b, int bit)
    {
        return (b & (1 << bit)) != 0;
    }
}