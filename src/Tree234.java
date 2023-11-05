package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Tree234 { // num children t-2t, num keys t-1 - 2t-1
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

    public void clear() { // clear the tree to build again
        this.root = null;
    }

    /////////////////////////// INSERTION

    public void insert(String name) {
        if (this.root == null) {
            this.root = new TreeNode(true, name);
        } else {
            insertHelper(name, root);
        }
    }

    private void insertHelper(String name, TreeNode current) {
        if (current.numKeys == 3) { // case 1: node is full, so call split
            current = split(name, current, current.parent); // split current node
        }

        // case 2: there is adequate room to insert
        while (!current.isLeaf) { // traverse down
            int i = 0;
            while (i < current.numKeys && name.compareTo(current.keys[i]) > 0) {
                i++;
            }

            if (current.children[i] != null) { // if current is found within the node
                current = current.children[i]; // insert new name into current[i]
                if (current.numKeys == 3) { // if inserting the new value makes the node full, we should
                    current = split(name, current, current.parent); // split it to make it easier for deletion later
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
        // case 3: inserting makes the current leaf full
        if (current.numKeys == 3) { // for leaf case, split it one more time
            current = split(name, current, current.parent);
        }
        // case 4: inserting into non-full leaf
        if (current.numKeys < 3) { // if not full, insert in correct place
            for (int j = current.numKeys; j > k; j--) {
                current.keys[j] = current.keys[j - 1]; // since its 0 based, this will work
            }
            current.keys[k] = name;
            current.numKeys++;
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

                    shiftInsert(parent, index, median); // insert median in parent

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

    /////////////////////////// TRAVERSE

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
        int i = getKeyPos(current, name);
        if (i < current.numKeys && name.equals(current.keys[i]))
            return current;
        if (current.isLeaf)
            return null;
        else {
            return searchHelper(current.children[i], name);
        }
    }

    /////////////////////////// RECURSIVE METHOD FOR DELETE
    public void delete(String target) {
        if (root == null)
            return;
        else
            deletion(root, target);
    }

    public void deletion(TreeNode current, String target) {
        boolean found = false;
        if (current == null) {
            System.out.println("Not found.");
            return;
        }

        int keyPos = 0;
        while (keyPos < current.numKeys) {
            // if found within current
            if (current.keys[keyPos].equals(target)) {
                found = true;
                break;
            } else if (current.keys[keyPos].compareTo(target) > 0) {
                break; // IF TARGET < KEY VALS
            }
            keyPos++;
        }
        if (found) { // special case last node deleted
            if (target.equals(current.keys[keyPos]) && current.numKeys == 1 && current == root && root.isLeaf) {
                root = null;
                return;
            }
            if (current.isLeaf && current.numKeys >= 2) { // Case 1: it is a leaf AND contains at least 2 keys
                deleteKeys(current, keyPos);
                return;
            } else { // Case 2: it's found within an internal node
                TreeNode predecessor = null;
                TreeNode successor = null;
                TreeNode leftChild = current.children[keyPos];
                TreeNode rightChild = current.children[keyPos + 1];
                if (leftChild != null && leftChild.numKeys >= 2) { // if left child has at least two keys
                    predecessor = getPredecessor(current, target); // if right child has at least two keys
                } else if (rightChild != null && rightChild.numKeys >= 2) {
                    successor = getSuccessor(current, target);
                }

                if (predecessor != null) { // replace with predecessor
                    String predKey = predecessor.keys[predecessor.numKeys - 1];
                    current.keys[keyPos] = predKey;
                    deletion(predecessor, predKey);
                } else if (successor != null) { // replace with successor
                    String successorKey = successor.keys[0];
                    current.keys[keyPos] = successorKey;
                    deletion(successor, successorKey);
                } else if (leftChild != null && rightChild != null) { // merge left and right child if not enough keys
                    merge(current.keys[keyPos], current, keyPos, leftChild,
                            rightChild);
                    deletion(leftChild, target);
                }
                return;
            }
        } else { // if not found
            if (current.isLeaf) {
                System.out.println("Cannot recurse any further, search name not in tree");
                return;
            } else { // fixing internal nodes along way down to delete
                TreeNode leftSibling = null;
                TreeNode rightSibling = null;
                TreeNode currentChild = current.children[keyPos];
                if (keyPos != 0)
                    leftSibling = current.children[keyPos - 1];
                if (keyPos != current.numKeys)
                    rightSibling = current.children[keyPos + 1];

                if (currentChild != null && currentChild.numKeys < 2 && currentChild.numKeys > 0) { // if child only 1
                                                                                                    // key
                    if (leftSibling != null && leftSibling.numKeys >= 2) { // rotate right, if left child has at least 2
                                                                           // keys
                        rotateRight(current, keyPos, keyPos - 1);
                    } else if (rightSibling != null && rightSibling.numKeys >= 2) { // right child at least 2 keys
                        rotateLeft(current, keyPos, keyPos);
                    } else if (leftSibling != null) { // merge with the left sibling
                        merge(current.keys[keyPos - 1], current, keyPos - 1, leftSibling,
                                currentChild);
                        keyPos--; // since deleted right node, need to move 1 back
                    } else if (rightSibling != null) { // merge with the right sibling
                        mergeRight(current.keys[keyPos], current, keyPos, currentChild, rightSibling);
                    }
                }
                // now recurse by finding which child branch to follow
                deletion(current.children[keyPos], target);
            }
        }

    }

    // ROTATION / MERGING

    public void rotateRight(TreeNode parent, int childIndex, int parentIndex) {
        TreeNode rightChild = parent.children[childIndex];
        TreeNode leftSib = parent.children[childIndex - 1];
        shiftInsert(rightChild, 0, parent.keys[parentIndex]); // shift parent value to right node
        parent.keys[parentIndex] = leftSib.keys[leftSib.numKeys - 1]; // replace parent value with leftNode key
        shiftChildren(rightChild, 0, leftSib.children[leftSib.numKeys]); // shift over the the child
        deleteChildren(leftSib, leftSib.numKeys); // delete the left's children
        deleteKeys(leftSib, leftSib.numKeys - 1); // delete the left key
    }

    public void rotateLeft(TreeNode parent, int childIndex, int parentIndex) {
        TreeNode leftChild = parent.children[childIndex];
        TreeNode rightNode = parent.children[childIndex + 1];
        shiftInsert(leftChild, leftChild.numKeys, parent.keys[parentIndex]);
        parent.keys[parentIndex] = rightNode.keys[0];
        shiftChildren(leftChild, leftChild.numKeys, rightNode.children[0]);
        deleteChildren(rightNode, 0);
        deleteKeys(rightNode, 0); // delete the right key
    }

    public void merge(String middle, TreeNode parent, int parentIndex, TreeNode leftChild, TreeNode rightChild) {
        if (leftChild != null) { // adding "middle" string to leftChild end
            shiftInsert(leftChild, leftChild.numKeys, middle);

            for (int i = 0; i < rightChild.numKeys; i++) { // merging right keys w/ left keys
                leftChild.keys[leftChild.numKeys + i] = rightChild.keys[i];
            }
            // Move children from the right node to the left node if dealing with
            // internal nodes
            if (!rightChild.isLeaf) {
                for (int i = 0; i <= rightChild.numKeys; i++) {
                    leftChild.children[leftChild.numKeys + i] = rightChild.children[i];
                    if (rightChild.children[i] != null) {
                        rightChild.children[i].parent = leftChild; // Update the parent reference
                    }
                }
                leftChild.isLeaf = false;
            }

            leftChild.numKeys += rightChild.numKeys;
            deleteChildren(parent, parentIndex + 1); // removing right child from children
            deleteKeys(parent, parentIndex); // parent's middle node is removed too
        }
        if (parent.numKeys == 0) { // if the number of keys is 0, need to adjust parent
            leftChild.parent = parent.parent;
            if (parent == root)
                root = leftChild; // if the tree is super short and the root is now the merged left node
        }
    }

    public void mergeRight(String middle, TreeNode parent, int parentIndex, TreeNode leftChild, TreeNode rightChild) {
        if (rightChild != null) { // adding "middle" string to rightChild beginning
            shiftInsert(rightChild, 0, middle);

            rightChild.numKeys += leftChild.numKeys;
            int keystoShift = leftChild.numKeys;

            // Shift keys in the right child to help merge, and fill the rest with the left
            // keys
            for (int i = rightChild.numKeys - 1; i >= 0; i--) {
                if (i >= keystoShift)
                    rightChild.keys[i] = rightChild.keys[i - keystoShift];
                else
                    rightChild.keys[i] = leftChild.keys[i];
            }
            // there is only a possibility of two children, because default case only occurs
            // if # left keys = 1
            if (leftChild.children[1] != null) {
                shiftChildren(rightChild, 0, leftChild.children[1]);
                deleteChildren(leftChild, 1);
            }
            if (leftChild.children[0] != null) {
                shiftChildren(rightChild, 0, leftChild.children[0]);
                deleteChildren(leftChild, 0);
            }

            deleteChildren(parent, parentIndex); // removing left child from children
            deleteKeys(parent, parentIndex); // parent's middle node is removed too
        }
        if (parent.numKeys == 0) { // if the number of keys is 0, need to adjust parent
            rightChild.parent = parent.parent;
            if (parent == root)
                root = rightChild; // if the tree is super short and the root is now the merged left node
        }
    }

    // HELPER METHODS
    // make room to insert key
    public void shiftInsert(TreeNode node, int addIndex, String name) {
        if (node.numKeys < 3) {
            for (int i = node.numKeys; i > addIndex; i--) {
                node.keys[i] = node.keys[i - 1];
            }
        }
        node.keys[addIndex] = name;
        node.numKeys++;
    }

    // delete key
    public void deleteKeys(TreeNode node, int dindex) {
        // shift all the values from the right of the deleted index down 1
        for (int i = dindex; i < node.numKeys - 1; i++) {
            node.keys[i] = node.keys[i + 1];
        }
        node.keys[node.numKeys - 1] = null;
        node.numKeys--;
    }

    // make room to insert new children
    public void shiftChildren(TreeNode node, int addIndex, TreeNode newChild) {
        if (node.isLeaf) {
            return;
        }
        for (int j = node.numKeys - 1; j >= addIndex; j--) { // adjust the children
            node.children[j + 1] = node.children[j];
        }
        node.children[addIndex] = newChild; // place the children and update its parent
        node.children[addIndex].parent = node;
    }

    // delete children
    public void deleteChildren(TreeNode node, int dindex) {
        if (node.isLeaf) {
            return;
        }
        for (int i = dindex; i < node.numKeys; i++) {
            node.children[i] = node.children[i + 1];
        }
        node.children[node.numKeys] = null;
    }

    public TreeNode getPredecessor(TreeNode node, String curr) {
        int checkLeft = getKeyPos(node, curr);

        if (node.children[checkLeft] != null) { // recurse down the left subtree's right most area
            TreeNode current = node.children[checkLeft];
            while (current.children[current.numKeys] != null) { // go to the right
                current = current.children[current.numKeys];
            }
            return current;
        }
        return null; // no valid predecessor
    }

    public TreeNode getSuccessor(TreeNode node, String curr) {
        int checkRight = getKeyPos(node, curr) + 1;

        if (node.children[checkRight] != null) { // recurse down the right subtree's left most area
            TreeNode current = node.children[checkRight];
            while (current.children[0] != null) {
                current = current.children[0];
            }
            return current;
        }
        return null; // no valid successor
    }

    public int getKeyPos(TreeNode current, String key) {
        int keyPos = 0;
        while (keyPos < current.numKeys && current.keys[keyPos].compareTo(key) < 0)
            keyPos++;
        return keyPos;
    }

    public int getChildPos(TreeNode current, TreeNode child) {
        int childPos = 0;
        while (current.children[childPos] != null)
            childPos++;
        return childPos;
    }

    public static void main(String[] args) {
        Tree234 tree234 = new Tree234();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("short_names.txt"));
            String searchName = br.readLine();
            while (searchName != null) {
                tree234.insert(searchName);
                searchName = br.readLine();
            }
            System.out.println("Full traversal of short tree");
            tree234.traverse();

            tree234.delete("MORGAN");
            System.out.println("Post-deletion traversal of short tree");
            tree234.traverse();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}