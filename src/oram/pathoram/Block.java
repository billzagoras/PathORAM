package oram.pathoram;

import java.util.BitSet;
import java.util.Random;

/*
Block constitutes the structural content of a Bucket.
It holds an id as an integer value and a BitSet object to store it's data.
*/

public class Block implements java.io.Serializable
{
    private
        int id;
        BitSet record;
        byte[] data;

     public Block()
     {
         for(int i = 0; i < 128; i++)
             this.record.clear(i);
         this.data = Utilities.toByteArray1(this.record);
     }

     protected Block(int id)
     {
         this.id = id;
         this.record = new BitSet(128);
         randomizeBitSet();
         this.data = Utilities.toByteArray1(this.record);
     }

    //Randomize the encoding of 0 and 1 binary  representation
    private void randomizeBitSet()
    {
        int tmp;
        Random rand = new Random();
        for(int i = 0; i < 128; i++)
        {
            tmp = rand.nextInt(2);
            if(tmp == 1)
                this.record.set(i);
        }
    }

    protected int getID()
    {
        return this.id;
    }

    protected byte[] getData()
    {
        return this.data;
    }
}