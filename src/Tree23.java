package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

    private void insertHelper(String name, TreeNode current) {
        if (current.numKeys == 3) { //case 1: node is full, so call split
            current = split(name, current, current.parent); // split current node
        }

        //case 2: there is adequate room to insert
        while (!current.isLeaf) { // traverse down
            int i = 0;
            while (i < current.numKeys && name.compareTo(current.keys[i]) > 0) {
                i++;
            }

            if (current.children[i] != null) { //if current is found within the node
                current = current.children[i]; //insert new name into current[i]
                if (current.numKeys == 3) { //if inserting the new value makes the node full, we should
                    current = split(name, current, current.parent); //split it to make it easier for deletion later
                }
            } 
            else { // special case where there is no leaf in that area
                TreeNode newTreeNode = new TreeNode(true, name);
                current.children[i] = newTreeNode;
                return;
            }
        }
            int k = 0;
            while (k < current.numKeys && name.compareTo(current.keys[k]) > 0) {
                k++; // k is the 0-based index where name should be inserted
        }
        //case 3: inserting makes the current leaf full
        if (current.numKeys == 3) { // for leaf case, split it one more time
            current = split(name, current, current.parent);
        }
        //case 4: inserting into non-full leaf
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

    public void shiftKeys(TreeNode node, int dindex) {
        // shift all the values from the right of the deleted index down 1
        for (int i = dindex; i < 3; i++) {
            node.keys[i] = node.keys[i + 1];
        }
        node.keys[node.keys.length - 1] = null;
        node.numKeys--;
    }

    ///////////////////////////RECURSIVE METHOD FOR DELETE
    public void deletion(TreeNode current, String target) {
        boolean found = false;

        int keyPos = 0;
        while (keyPos < current.numKeys) {
            //if found within current
            if (current.keys[keyPos].equals(target)) {
                found = true;
                break;
            }
            else if (current.keys[keyPos].compareTo(target) > 0) break; //IF TARGET < KEY VALS
            keyPos++;
        }
        if (found) {
            if (current.isLeaf && current.numKeys >=2) {  //Case 1: it is a leaf AND contains at least 2 keys
                shiftKeys(current, keyPos); 
                return; 
            }
            else { //Case 2: it's found within an internal node
                //get predecessor
                TreeNode predecessor = current.children[keyPos];
                if (predecessor!= null && predecessor.numKeys >= 2) { //USING 2 BECAUSE 2 IS OUR T
                    String predKey = predecessor.keys[predecessor.numKeys-1];
                    current.keys[keyPos] = predKey; 
                    //deleteLeafKey(predKey, predecessor); //recursive method 
                    deletion(predecessor, predKey); 
                }
                else {
                    TreeNode successor = current.children[keyPos+1];
                    if (successor!= null &&successor.numKeys >= 2) {
                        String successorKey = successor.keys[0];
                        current.keys[keyPos] = successorKey;
                        // deleteLeafKey(successorKey, successor); //recursive method 
                        deletion(successor, successorKey); 
                    }
                    else {
                        merge(current, keyPos, predecessor, successor);
                        //deleteLeafKey(target, predecessor);
                        deletion(predecessor, target);
                    }
                }
                return;
            }
        }
        else { //if not found
            if (current.isLeaf) System.out.println("Cannot recurse any further, search name not in tree");
            else { //fixing internal nodes along way down to delete
                if (current.children[keyPos].numKeys < 2) {
                    if (keyPos != 0 && current.children[keyPos-1].numKeys >= 2) { //if key position is greater than 1 but not found left child has at least 2 keys
                        stealLeft(current, keyPos);  
                        //or rotate it?
                    }
                    else if (keyPos != current.numKeys && current.children[keyPos+1].numKeys >= 2) {
                        stealRight(current, keyPos);
                        //rotate it?
                    }
                    else {
                        if (keyPos == current.numKeys) {
                            merge(current, --keyPos, current.children[keyPos], current.children[keyPos+1]);
                        }
                    }
                }
                //now recurse by finding which child branch to follow
                deletion(current.children[keyPos], target);
            }
        }
    }
        
    public int getKeyPos(TreeNode current, String key) {
        int keyPos = 0;
        while (keyPos < current.numKeys && current.keys[keyPos].compareTo(key) > 0) keyPos++;
        return keyPos;
    }

    public int getChildPos(TreeNode current, TreeNode child) {
        int childPos = 0;
        while (current.children[childPos] != null) childPos++;
        return childPos;
    }
    
        public void deleteLeafKey(String key, TreeNode leaf) {
        //check if leaf is root
        if (leaf == root || leaf.numKeys >= 2) return;  //actually you might need to delete here, could just run the recursive method again?

        //if not root, get keyPos
        int keyPos = getKeyPos(leaf, key);
        shiftKeys(leaf, keyPos); //deletes at keyPos, updates count
    
        TreeNode parent = leaf.parent;
        //find what child index node is for parent
        int i = getChildPos(parent, leaf);
    
        //case 1: if the leaf node's left sibling is not null and the left sib has 2-3 keys, rotate right
        if (i > 0 && parent.children[i-1] != null && parent.children[i-1].numKeys >= 2) rotateRight(parent, i);
        //case 2: if the leaf node's right sib is not null and the right sib has 2-3 keys, rotate left
        else if (i < parent.numKeys && parent.children[i+1] != null && parent.children[i+1].numKeys >= 2) rotateLeft(parent, i);
        //if the leaf node's position is equal to parent's key at i (leaf is left child), merge values from
        //left sib and i
        else
            if (i == parent.numKeys) {
                i--;
            }
        merge(parent, i, parent.children[i], parent.children[i+1]);
    }
   
    public void rotateRight(TreeNode parent, int childIndex) {
        TreeNode node = parent.children[childIndex];
        TreeNode leftNode = parent.children[childIndex-1];
        node.keys[node.numKeys++] = parent.keys[childIndex-1];
        parent.keys[childIndex-1] = leftNode.keys[leftNode.numKeys-1];
        shiftKeys(leftNode, leftNode.numKeys-1);
        if (!node.isLeaf) {
            node.children[node.numKeys++] = leftNode.children[leftNode.numKeys-1];
            leftNode.children[leftNode.numKeys-1] = null;
        }
    }

    public void rotateLeft(TreeNode parent, int childIndex) {
        TreeNode node = parent.children[childIndex];
        TreeNode rightNode = parent.children[childIndex+1];
        node.keys[node.numKeys++] = parent.keys[childIndex];
        parent.keys[childIndex] = rightNode.keys[0];
        shiftKeys(rightNode, 0);
        rightNode.numKeys--;
        if (!node.isLeaf) {
            node.children[node.numKeys++] = rightNode.children[0];
            rightNode.children[0] = null;
        }
    }

    // merge(parent, i, parent.children[i], parent.children[i+1]);

    public void merge(TreeNode parent, int childIndex, TreeNode predecessor, TreeNode successor) {
        if (predecessor != null && successor != null) {
            for (int i = 0; i < successor.numKeys; i++) { //merging successor keys w/ predecessor keys
            predecessor.keys[predecessor.numKeys+i] = successor.keys[i];
            }
            predecessor.numKeys += successor.numKeys;
            parent.children[childIndex+1] = null; //removing successor child
            shiftKeys(parent, childIndex);  //===UNSURE WHAT TO DO ABOUT REMOVED PARENT KEY, FORGOT IF IT MERGES TOO
            parent.numKeys--;
        }
        if (parent == root) {
            root = predecessor; //if the tree is super short and the root is now the merged predecessor array
        }
    }

    public void stealLeft(TreeNode parent, int index) {
        TreeNode node = parent.children[index];
        TreeNode leftSib = parent.children[index-1];

        //now steal from left sibling >:)
        for (int i = 0; i < leftSib.numKeys; i++) {
            node.keys[node.numKeys+i] = leftSib.keys[i];
            node.numKeys++;
        }
        parent.keys[index-1] = leftSib.keys[leftSib.numKeys-1];
        leftSib.numKeys--;
        if (!node.isLeaf) {
            int nodeChildren = 0;
            int sibChildren = 0;
            for (int i = 0; i < 5; i++) {
                if (node.children[i] != null) nodeChildren++;
                if (leftSib.children[i] != null) sibChildren++;
            }
            node.children[nodeChildren] = leftSib.children[sibChildren-1];
            leftSib.children[sibChildren-1] = null;
        }
    }

    public void stealRight(TreeNode parent, int index) {
        TreeNode node = parent.children[index];
        TreeNode rightSib = parent.children[index+1];

        //now steal from right sibling >:)
        for (int i = 0; i < parent.numKeys; i++) {
            node.keys[node.numKeys+i] = parent.keys[i];
            node.numKeys++;
        }
        parent.keys[index] = rightSib.keys[0];
        rightSib.numKeys--;
        if (!node.isLeaf) {
            int nodeChildren = 0;
            int sibChildren = 0;
            for (int i = 0; i < 5; i++) {
                if (node.children[i] != null) nodeChildren++;
                if (rightSib.children[i] != null) sibChildren++;
            }
            node.children[nodeChildren] = rightSib.children[0];
            rightSib.children[0] = null;
        }
    }

    public static void main(String[] args) {
        Tree23 tree23 = new Tree23();
        BufferedReader br = null;
        BufferedReader post = null;

        try {
            br = new BufferedReader(new FileReader("short_names.txt"));
            String searchName = br.readLine();
            while (searchName != null) {
                tree23.insert(searchName);
                searchName = br.readLine();
            }
            System.out.println("Full traversal of short tree");
            tree23.traverse();

            post = new BufferedReader(new FileReader("short_names.txt"));
            String deletePost = post.readLine();
            int i = 0;
            while (i != 1) {
                tree23.deletion(tree23.root, deletePost);
                i++;
                deletePost = post.readLine();
            }
            System.out.println("\n\nShort tree post deletion");
            tree23.traverse();

        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
}
