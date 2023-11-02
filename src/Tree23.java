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

    public TreeNode search(String name) {
        if (this.root == null) {
            return null;
        } else {
            return searchHelper(root, name);
        }
    }

    public TreeNode searchHelper(TreeNode current, String name) {
        int i = 0;
        while (i < current.numKeys && name.compareTo(current.keys[i]) > 0)
            i++;
        if (i < current.numKeys && name.equals(current.keys[i]))
            return current;
        if (current.isLeaf)
            return null;
        else {
            return searchHelper(current.children[i], name);
        }
    }

    public void swapPredecessor(TreeNode node, int nameInd) {
        String predecessor = null;
        TreeNode leftChild = node.children[nameInd];
        if (leftChild != null) {
            predecessor = leftChild.keys[leftChild.numKeys - 1];
            node.keys[nameInd] = predecessor; // replace with predecessor
            shiftKeys(leftChild, leftChild.numKeys - 1);
        } else if (nameInd - 1 >= 0) {
            predecessor = node.keys[nameInd - 1];
            node.keys[nameInd] = predecessor;
            shiftKeys(node, nameInd - 1);
        }
    }

    public String swapSuccessor(TreeNode node, int nameInd) {
        String successor = null;
        TreeNode rightChild = node.children[nameInd + 1];
        if (rightChild != null) {
            successor = rightChild.keys[0];
            node.keys[nameInd] = successor; // replace with predecessor
            shiftKeys(rightChild, 0);
        } else if (nameInd + 1 < node.numKeys) {
            successor = node.keys[nameInd + 1];
            node.keys[nameInd] = successor;
            shiftKeys(node, nameInd + 1);
        }
        return successor;
    }

    public void shiftKeys(TreeNode node, int dindex) {
        // shift all the values from the right of the deleted index down 1
        for (int i = dindex; i < 3; i++) {
            node.keys[i] = node.keys[i + 1];
        }
        node.keys[node.keys.length - 1] = null;
        node.numKeys--;
    }

    public void merge(TreeNode location, int keyLocation) { 
        if (location == null || location.parent == null) {   //not able to merge
            return; 
        }
        TreeNode leftSib = keyLocation - 1 >= 0 ? location.parent.children[keyLocation - 1]: null; 
        TreeNode rightSib = keyLocation + 1 < 4 ? location.parent.children[keyLocation + 1]: null; 

        if (location.numKeys == 1) { // where sum of numkey from parent and left child is 2
            if (location.parent.numKeys <= 1) {  //if parent does not have enough keys, take from sibling and its parent 
                if (leftSib!= null && leftSib.numKeys > 1 ) {
                    location.keys[2] = location.keys[0];
                    location.keys[0] = leftSib.keys[0];
                    location.keys[1] = location.parent.keys[keyLocation - 1];
                    location.numKeys++;
                    return; 
                }
                else if (rightSib!= null && rightSib.numKeys > 1) {
                    jsadfljsdajfklsfa
                    return; 
                }
            }
        }
        merge(location.parent, keyLocation); 
    }

    public boolean delete(String name) {
        TreeNode location = search(name);
        int i = 0; // Find where the key is in the TreeNode
        while (i < location.numKeys && name.compareTo(location.keys[i]) > 0)
            i++;
        int keyLocation = i;

        // LEFT AND RIGHT CHILDREN
        TreeNode left = location.children[keyLocation];
        TreeNode right = location.children[keyLocation + 1];

        // 3 cases for deleting in a 2-3 tree
        // 1. element is a leaf node that contains at least 1 keys
        if (location.isLeaf) {
            // LEAF NUM KEYS > 1
            if (location.numKeys > 1) {
                location.keys[keyLocation] = null;
                location.numKeys--;
            }
            // 2. LEAF NUM KEYS == 1
            TreeNode leftSib = keyLocation - 1 >= 0 ? location.parent.children[keyLocation - 1]: null; 
            TreeNode rightSib = keyLocation + 1 < 4 ? location.parent.children[keyLocation + 1]: null; 

            if (location.numKeys == 1) { // where sum of numkey from parent and left child is 2
                if (location.parent.numKeys <= 1) {  //if parent does not have enough keys, take from sibling and its parent 
                    if (leftSib.numKeys > 1) {
                        leftSib.keys[leftSib.numKeys-1]
                    }   
                    else if (rightSib.numKeys > 1) {

                    } 
                }
                if (leftSib!= null && leftSib.numKeys > 1 ) {
                    location.keys[2] = location.keys[0];
                    location.keys[0] = left.keys[0];
                    location.keys[1] = location.parent.keys[keyLocation - 1];
                    location.numKeys++;
                }
                else if (rightSib!= null && rightSib.numKeys > 1) {
                    jsadfljsdajfklsfa
                }
            }
        }
        // 2. element is an internal node:
        // left child has 2 keys: replace w predecesor and delete it
        if (location.children[keyLocation].numKeys == 2) {
            swapPredecessor(location, keyLocation);
        }
        // right child has 2 keys: replace w successor and delete it
        if (location.children[keyLocation + 1].numKeys == 2) {
            swapSuccessor(location, keyLocation);
        }
        // both children have 1 key -- merge them / both children have t-1 keys
        if (left.numKeys == 1 && right.numKeys == 1) {
            left.keys[1] = right.keys[0];
            left.numKeys++;
            right = null;
            shiftKeys(location, keyLocation);
        }

        return false;
    }

}