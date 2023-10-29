package src;

class Tree23 { // num children t-2t, num keys t-1 - 2t-1
    private static TreeNode root;

    static class TreeNode {
        String[] keys = new String[3]; // each node contains keys and has an array of children nodes
        TreeNode[] children = new TreeNode[4]; // don't know if can initialize it like this
        TreeNode parent = null;
        boolean isLeaf;
        int numKeys;

        public TreeNode(boolean isLeaf, String name) { // when creating a new node
            this.isLeaf = isLeaf;
            this.keys[0] = name;
            this.numKeys = 1;
        }
    }

    public static void insert(String name) {
        if (root == null) {
            root = new TreeNode(true, name);
        } else {
            insertHelper(name, root, null);
        }
    }

    private static void insertHelper(String name, TreeNode current, TreeNode parent) {

        int k = 0;
        while (k <= current.numKeys && name.compareTo(current.keys[k]) > 0) {
            k++; // k is the 0-based index where name should be inserted
        }

        if (current.numKeys == 3) {
            TreeNode leftNode = new TreeNode(current.isLeaf, current.keys[0]);
            TreeNode rightNode = new TreeNode(current.isLeaf, current.keys[2]);
            String median = current.keys[1];

            if (parent != null) { // if the parent exists, find where to insert it above
                int index = 0;
                while (index < parent.numKeys && median.compareTo(parent.keys[index]) > 0) {
                    index++;
                }
                if (parent.numKeys < 3) { // if not full, insert in correct place (or else need to recurse again)
                    parent.numKeys++;
                    for (int j = parent.numKeys - 1; j > k; j--) { // adjust the parent values
                        parent.keys[j] = parent.keys[j - 1];
                    }
                    parent.keys[k] = median;

                    for (int j = parent.numKeys; j > k; j--) { // adjust the children
                        parent.children[j] = parent.children[j - 1];
                    }
                    parent.children[index] = leftNode;
                    parent.children[index + 1] = rightNode;
                } else {
                    insertHelper(name, parent, parent.parent);
                }

            } else { // else create a new root
                TreeNode newRoot = new TreeNode(false, median);
                newRoot.children[0] = leftNode;
                newRoot.children[1] = rightNode;
                leftNode.parent = newRoot;
                rightNode.parent = newRoot;
                root = newRoot;
            }
        }
        while (!current.isLeaf) {
            int i = 0;
            while (i <= current.numKeys && name.compareTo(current.keys[i]) > 0) {
                i++;
            }
            if (current.children[i] != null) {
                current = current.children[i];
            } else { // special case where there is no leaf in that area
                TreeNode newTreeNode = new TreeNode(true, name);
                current.children[i] = newTreeNode;
                return;
            }
        }

        if (current.numKeys < 3) { // if not full, insert in correct place
            for (int j = current.numKeys; j > k; j--) {
                current.keys[j] = current.keys[j - 1];
            }
            current.keys[k] = name;
            current.numKeys++;
        }

        /////////////////////////////////////////////////////////////////////////////
        /*
         * else { // if full need to split key
         * TreeNode leftNode = new TreeNode(current.isLeaf, current.keys[0]);
         * TreeNode rightNode = new TreeNode(current.isLeaf, current.keys[2]);
         * String median = current.keys[1]; // how to recurse back up to connect to
         * parent?
         * 
         * // use split method here, now we need to do something about current key
         * array[]
         * 
         * if (name.compareTo(median) < 0) { // case 1: median is greater than insertion
         * name
         * int lKeys = leftNode.numKeys++;
         * if (name.compareTo(leftNode.keys[0]) < 0) {
         * leftNode.keys[1] = leftNode.keys[0];
         * leftNode.keys[0] = name;
         * } else {
         * leftNode.keys[1] = name;
         * }
         * } else { // case 1: median is greater than insertion name
         * int rKeys = rightNode.numKeys++;
         * if (name.compareTo(rightNode.keys[0]) < 0) {
         * rightNode.keys[1] = rightNode.keys[0];
         * rightNode.keys[0] = name;
         * } else {
         * rightNode.keys[1] = name;
         * }
         * }
         * 
         * current.children[0] = leftNode;
         * current.children[1] = rightNode;
         * current[numKeys++] =
         * }
         */

    }

    public static void traverse() {
        if (root == null) {
            System.out.println("No names found");
        } else {
            traverseHelper(root);
        }
    }

    private static void traverseHelper(TreeNode current) {
        if (current != null) {
            for (int i = 0; i < current.numKeys; i++) { // go through the keys
                if (!current.isLeaf) {
                    traverseHelper(current.children[i]); // but go to the children first as much as you can
                }
                System.out.println(current.keys[i] + " ");
            }
        }
    }
}