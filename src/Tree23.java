package src;

class Tree23 { // num children t-2t, num keys t-1 - 2t-1
    private TreeNode root;

    class TreeNode {
        String[] keys = new String[3]; // each node contains keys and has an array of children nodes
        TreeNode[] children = new TreeNode[4];
        TreeNode parent = null;
        boolean isLeaf;
        int numKeys;

        public TreeNode(boolean isLeaf, String name) { // when creating a new node
            this.isLeaf = isLeaf;
            this.keys[0] = name;
            this.numKeys = 1;
        }
    }

    public void insert(String name) {
        if (this.root == null) {
            this.root = new TreeNode(true, name);
        } else {
            insertHelper(name, root);
        }
    }

    private TreeNode split(String name, TreeNode current, TreeNode parent) { // extra method to split
        TreeNode tempNode = null; // to be reassigned later
        while (current.numKeys == 3) {
            TreeNode leftNode = new TreeNode(current.isLeaf, current.keys[0]);
            TreeNode rightNode = new TreeNode(current.isLeaf, current.keys[2]);
            String median = current.keys[1];

            if (!leftNode.isLeaf) { // update the children node if its not a leaf too!
                leftNode.children[0] = current.children[0];
                leftNode.children[1] = current.children[1];
                leftNode.children[0].parent = leftNode;
                leftNode.children[1].parent = leftNode;
            }

            if (!rightNode.isLeaf) {
                rightNode.children[0] = current.children[2];
                rightNode.children[1] = current.children[3];
                rightNode.children[0].parent = rightNode;
                rightNode.children[1].parent = rightNode;
            }

            if (parent != null) { // if the parent exists, find where to insert it above
                int index = 0;
                while (index < parent.numKeys && median.compareTo(parent.keys[index]) > 0) {
                    index++;
                }
                if (parent.numKeys < 3) { // if not full, insert in correct place
                    for (int j = parent.numKeys; j > index; j--) { // adjust the children
                        parent.children[j + 1] = parent.children[j];
                    }
                    parent.children[index] = leftNode;
                    parent.children[index + 1] = rightNode;

                    for (int j = parent.numKeys; j > index; j--) { // adjust the parent values
                        parent.keys[j] = parent.keys[j - 1];
                    }
                    parent.keys[index] = median;
                    parent.numKeys++;

                    leftNode.parent = parent;
                    rightNode.parent = parent;
                } else {
                    current = parent; // go up
                }

            } else { // else create a new root
                TreeNode newRoot = new TreeNode(false, median);
                newRoot.children[0] = leftNode;
                newRoot.children[1] = rightNode;
                leftNode.parent = newRoot;
                rightNode.parent = newRoot;
                root = newRoot;
            }

            if (name.compareTo(median) < 0) { // update the current pointer
                tempNode = leftNode;
            } else {
                tempNode = rightNode;
            }
            current = tempNode;
        }
        return tempNode;
    }

    private void insertHelper(String name, TreeNode current) {

        if (current.numKeys == 3) {
            current = split(name, current, current.parent); // update current
        }

        while (!current.isLeaf) { // traverse down
            int i = 0;
            while (i < current.numKeys && name.compareTo(current.keys[i]) > 0) {
                i++;
            }
            if (current.children[i] != null) {
                current = current.children[i];
                if (current.numKeys == 3) {
                    current = split(name, current, current.parent); // update current
                }
            } else { // special case where there is no leaf in that area
                TreeNode newTreeNode = new TreeNode(true, name);
                current.children[i] = newTreeNode;
                return;
            }
        }

        int k = 0;
        while (k < current.numKeys && name.compareTo(current.keys[k]) > 0) {
            k++; // k is the 0-based index where name should be inserted
        }

        if (current.numKeys == 3) { // for leaf case, split it one more time
            current = split(name, current, current.parent);
        }

        if (current.numKeys < 3) { // if not full, insert in correct place
            for (int j = current.numKeys; j > k; j--) {
                current.keys[j] = current.keys[j - 1]; // since its 0 based, this will work
            }
            current.keys[k] = name;
            current.numKeys++;
        }
    }

    public void traverse() {
        if (this.root == null) {
            System.out.println("No names found");
        } else {
            traverseHelper(this.root);
        }
    }

    private void traverseHelper(TreeNode current) {
        if (current != null) {
            int i;
            for (i = 0; i < current.numKeys; i++) { // go through the keys
                if (!current.isLeaf) {
                    traverseHelper(current.children[i]); // but go to the children first as much as you can
                }
                System.out.println(current.keys[i] + " ");
            }
            if (!current.isLeaf) {
                traverseHelper(current.children[i]); // traverse the right children
            }
        }
    }

    public int search(String name) {
        if (this.root == null) {
            return -1;
        } else {
            return searchHelper(root, name);
        }
    }

    public int searchHelper(TreeNode current, String name) {
        int i = 0;
        while (i < current.numKeys && name.compareTo(current.keys[i]) > 0)
            i++;
        if (i < current.numKeys && name.equals(current.keys[i]))
            return i;
        if (current.isLeaf)
            return -1;
        else {
            return searchHelper(current.children[i], name);
        }
    }
}