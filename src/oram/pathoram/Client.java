package oram.pathoram;
import javax.crypto.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import static java.lang.Math.pow;

/*
Client class is responsible for creating buckets, setting their level values in their corresponding path,
creating and updating position map accordingly after each access made to server's binary tree.
*/
public class Client
{
    private
        static int L = 3;
        static final byte[] key = "pUrpl3P4r0+F1y1n".getBytes(); // 128 bits key
        static String[] PositionMap;
        static Bucket[] bucketList = new Bucket[(int)pow(2, L + 1) - 1];
        static Bucket[] stash = new Bucket[L + 1];
        static String path = "";
        static int BLOCK_NUM = 9;

    public Client(){}

    // Creation of buckets with respect to Server's binary tree height.
    public void createBuckets() throws NoSuchAlgorithmException
    {
        for(int i = 0; i < pow(2, L + 1) - 1; i++)
            this.bucketList[i] = new Bucket();
        // Initialization of levels, as if Server's binary tree holds the buckets from the bucket list with DFS logic.
        bucketList[0].setLevel(0);
        bucketList[1].setLevel(1);
        bucketList[2].setLevel(2);
        bucketList[3].setLevel(3);
        bucketList[4].setLevel(3);
        bucketList[5].setLevel(2);
        bucketList[6].setLevel(3);
        bucketList[7].setLevel(3);
        bucketList[8].setLevel(1);
        bucketList[9].setLevel(2);
        bucketList[10].setLevel(3);
        bucketList[11].setLevel(3);
        bucketList[12].setLevel(2);
        bucketList[13].setLevel(3);
        bucketList[14].setLevel(3);
    }

    public void setBuckets(Bucket[] bucketList)
    {
        this.bucketList = bucketList;
    }

    public Bucket[] getBuckets()
    {
        return this.bucketList;
    }

    public void createPositionMap()
    {
        this.PositionMap = new String[(int) pow(2, L)];
    }

    // Position map initialization.
    public void initializePositionMap()
    {
        this.PositionMap[0] = "3,0";
        this.PositionMap[1] = "4,2";
        this.PositionMap[2] = "6,5";
        this.PositionMap[3] = "7,1";
        this.PositionMap[4] = "10,8";
        this.PositionMap[5] = "11,9";
        this.PositionMap[6] = "13,12";
        this.PositionMap[7] = "14";
    }

    // obliviousAccess: (read)
    protected void obliviousAccess(String bucketID, Server.BinaryTree tree) throws Exception {
        int tempPos = -1;
        int tempPosRoot = -1;
        // Find root's node bucket id.
        String rootBucketID = Integer.toString(tree.getRoot().getBucket().block[0].getID());
        int i = 0;
        do { // Find wanted node's path.
            if(PositionMap[i].contains(bucketID))
                tempPos = i;
            i++;
        } while(tempPos == -1);

        i = 0;
        do { // Find root's node path.
            if(PositionMap[i].contains(rootBucketID))
                tempPosRoot = i;
            i++;
        } while(tempPosRoot == -1);

        // Update Position Map.
        int x = PositionMap[tempPos].indexOf(bucketID);
        int y = PositionMap[tempPosRoot].indexOf(rootBucketID);
        PositionMap[tempPosRoot].toCharArray()[y] = bucketID.charAt(0);
        PositionMap[tempPos].toCharArray()[x] = rootBucketID.charAt(0);

        // Get to-be-followed path to binary format.
        path = Integer.toBinaryString(tempPos);
        while(path.length() < 3)
            path = "0" + path;

        // Get decrypted bucket.
        stash[0] = tree.getRoot().getBucket();
        for (int j = 0; j < BLOCK_NUM; j++)
            stash[0].block[j].data = CryptoManager.decrypt(this.key, tree.getRoot().getBucket().getIV().getEncoded(), tree.getRoot().getBucket().block[j].data);
        // set focusNode iterator to point to tree's root root.
        Server.Node focusNode = tree.getRoot();

        // Follow the path found before and update stash accordingly.
        for(int j = 0; j < path.length(); j++)
        {
            if (path.charAt(j) == '0') {
                stash[j + 1] = focusNode.getLeft().getBucket();
                // Decrypt each bucket's block.
                for (int z = 0; z < BLOCK_NUM; z++)
                    stash[j + 1].block[z].data = CryptoManager.decrypt(this.key, focusNode.getLeft().getBucket().getIV().getEncoded(), focusNode.getLeft().getBucket().block[z].data);
                focusNode = focusNode.getLeft();
            }
            else if (path.charAt(j) == '1') {
                stash[j + 1] = focusNode.getRight().getBucket();
                // Decrypt each bucket's block.
                for (int z = 0; z < BLOCK_NUM; z++)
                    stash[j + 1].block[z].data = CryptoManager.decrypt(this.key, focusNode.getRight().getBucket().getIV().getEncoded(), focusNode.getRight().getBucket().block[z].data);
                focusNode = focusNode.getRight();
            }
        }
        // Encrypt read buckets with a ned iv and re-write read path to the tree.
        obliviousAccess(bucketID, tree, 'w');
    }

    // obliviousAccess: overloading (write)
    protected void obliviousAccess(String bucketID, Server.BinaryTree tree, char ch) throws Exception {
        int i = -1;
        // Re-encrypt all read buckets with a new iv.
        for(int j = 0; j < L + 1; j++)
        {
            do {
                i++;
                if (bucketList[i].block[0].getID() == stash[j].block[0].getID()) {
                    encryptBucket(i);
                    stash[j] = bucketList[i];
                }
            } while (bucketList[i].block[0].getID() != stash[j].block[0].getID());
            i = 0;
        }
        Bucket tempBucket = stash[Integer.parseInt(bucketID)];
        stash[Integer.parseInt(bucketID)] = stash[0];
        stash[0] = tempBucket;
        tree.getRoot().setBucket(stash[0]);
        Server.Node focusNode = tree.getRoot();

        // Place the re-encrypted buckets back to the path, with root bucket being the requested bucket.
        for(int j = 0; j < path.length(); j++)
        {
            if (path.charAt(j) == '0') {
                focusNode.getLeft().setBucket(stash[j + 1]);
                focusNode = focusNode.getLeft();
            }
            else if (path.charAt(j) == '1') {
                focusNode.getRight().setBucket(stash[j + 1]);
                focusNode = focusNode.getRight();
            }
        }
    }

    // Bucket's data encryption using CryptoManager class.
    public void encryptBucket(int i) throws Exception {
        try {
            for (int j = 0; j < BLOCK_NUM; j++)
                this.bucketList[i].block[j].data = CryptoManager.encrypt(this.key, this.bucketList[i].getIV().getEncoded(), this.bucketList[i].block[j].data);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
}
    // Object serialization process.
    private byte[] convertToBytes(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(object);
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            return bos.toByteArray();
        }
    }

    // Object deserialization process.
    private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream BIS = new ByteArrayInputStream(bytes);
             ObjectInput OIS = new ObjectInputStream(BIS)) {
            return (Object) OIS.readObject();
        }
    }

}