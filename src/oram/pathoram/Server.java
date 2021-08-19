package oram.pathoram;

/*
Server is responsible for maintaining and manipulating the binary tree that holds all buckets encrypted.
*/

public class Server
{
    private
        BinaryTree tree;

    public Server(){}

    public static class BinaryTree
    {
        private Node root;

        public BinaryTree(Bucket bucket)
        {
            root = new Node(bucket);
        }

        public void add(Node parent, Node child, String orientation)
        {
            if (orientation.equals("left"))
            {
                parent.setLeft(child);
            }
            else
            {
                parent.setRight(child);
            }
        }

        public Node getRoot()
        {
            return this.root;
        }
    }

    static class Node
    {
        private Bucket bucket;
        private Node left;
        private Node right;

        Node (Bucket bucket) {
            this.bucket = bucket;
            right = null;
            left = null;
        }

        public void setBucket(Bucket bucket) {
            this.bucket = bucket;
        }

        public Bucket getBucket() {
            return this.bucket;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getLeft() {
            return left;
        }

        public void setRight(Node right ) {
            this.right = right;
        }

        public Node getRight() {
            return right;
        }
    }

    public void initializeTree(Bucket[] bucketList)
    {
        // Place buckets to nodes from the bucket list with DFS logic.
        this.tree = new BinaryTree(bucketList[0]);
        Node node1 = new Node(bucketList[1]);
        this.tree.add(this.tree.getRoot(), node1, "left");
        Node node2 = new Node(bucketList[2]);
        this.tree.add(node1, node2, "left");
        Node node3 = new Node(bucketList[3]);
        this.tree.add(node2, node3, "left");
        Node node4 = new Node(bucketList[4]);
        this.tree.add(node2, node4, "right");
        Node node5 = new Node(bucketList[5]);
        this.tree.add(node1, node5, "right");
        Node node6 = new Node(bucketList[6]);
        this.tree.add(node5, node6, "left");
        Node node7 = new Node(bucketList[7]);
        this.tree.add(node5, node7, "right");
        Node node8 = new Node(bucketList[8]);
        this.tree.add(tree.getRoot(), node8, "right");
        Node node9 = new Node(bucketList[9]);
        this.tree.add(node8, node9, "left");
        Node node10 = new Node(bucketList[10]);
        this.tree.add(node9, node10, "left");
        Node node11 = new Node(bucketList[11]);
        this.tree.add(node9, node11, "right");
        Node node12 = new Node(bucketList[12]);
        this.tree.add(node8, node12, "right");
        Node node13 = new Node(bucketList[13]);
        this.tree.add(node12, node13, "left");
        Node node14 = new Node(bucketList[14]);
        this.tree.add(node12, node14, "right");
    }

    protected BinaryTree getTree()
    {
        return this.tree;
    }
}
