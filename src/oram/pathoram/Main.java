package oram.pathoram;

import static java.lang.Math.pow;
/*
In current main function Client and Server objects are created.
After that, position map array and bucket objects get created and initialized via Client.
Client also encrypts bucket objects and sets Server's binary tree nodes to hold these buckets.
Finally, bucket with id 1 gets retrieved.
*/
public class Main
{
    private static int L = 3;
    private static int AES_128 = 128;
    private static int BUCKET_NUM = 15;
    public static void main(String[] args) throws Exception {

        Client client = new Client();
        Server server = new Server();
        client.createPositionMap();
        client.initializePositionMap();
        client.createBuckets();

        for(int i = 0; i < (int)pow(2, L + 1) - 1; i++) {
            client.encryptBucket(i);
        }

        server.initializeTree(client.getBuckets());
        String bucketID = "1";
        client.obliviousAccess(bucketID, server.getTree());
    }
}