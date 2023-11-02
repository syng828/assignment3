package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class BinaryTree {
    private TreeNode root = null;

    class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        public TreeNode(String val) {
            this.val = val;
        }

        public TreeNode(String val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public void insert(String name) { // insert with iteration method and without returning the node
        if (this.root == null) {
            this.root = new TreeNode(name);
        } else {
            TreeNode current = root;
            TreeNode prev = current;
            while (current != null) {
                prev = current;
                if (name.compareTo(current.val) < 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
            if (name.compareTo(prev.val) < 0) {
                prev.left = new TreeNode(name);
            } else {
                prev.right = new TreeNode(name);
            }
        }
    }

    public void traverse() {
        if (this.root == null) {
            return;
        }
        traverseHelper(this.root);
    }

    public void traverseHelper(TreeNode current) {
        if (current == null) {
            return;
        }
        traverseHelper(current.left);
        System.out.println(current.val);
        traverseHelper(current.right);

    }

    public boolean search(String name) {
        if (this.root == null)
            return false;
        else
            return searchHelper(name, this.root);
    }

    public boolean searchHelper(String name, TreeNode current) {
        if (current != null) {
            if (current.val.equals(name))
                return true;
            else if (current.val.compareTo(name) > 0)
                return searchHelper(name, current.left);
            else if (current.val.compareTo(name) < 0)
                return searchHelper(name, current.right);
        }
        return false;
    }

    public void delete(String name) {
        TreeNode parent = null;
        TreeNode current = root;

        while (current != null) {
            if (name.equals(current.val)) { // if there's no children set as null
                if (current.left == null && current.right == null) {
                    if (parent == null)
                        root = null;
                    else if (parent.left == current)
                        parent.left = null;
                    else {
                        parent.right = null;
                    }
                    break;
                } // if there's one children, just replace with child
                else if (current.left != null && current.right == null) {
                    if (parent == null)
                        root = current.left;
                    else if (parent.left == current)
                        parent.left = current.left;
                    else {
                        parent.right = current.left;
                    }
                    break;
                } else if (current.left == null && current.right != null) {
                    if (parent == null)
                        root = current.right;
                    else if (parent.left == current)
                        parent.left = current.right;
                    else {
                        parent.right = current.right;
                    }
                    break;
                } else { // if there are 2 children
                    TreeNode successor = getSuccessor(current);
                    current.val = successor.val; // after finding the sucessor swap it, and make sure to delete extra
                                                 // node
                    name = successor.val;
                    parent = current;
                    current = current.right;
                }
            } else { // continue searching if can't find it
                parent = current;
                if (current.val.compareTo(name) > 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
        }
    }

    private TreeNode getSuccessor(TreeNode current) {
        if (current == null || current.right == null) {
            return null;
        }
        TreeNode successor = current.right;
        while (successor.left != null) {
            successor = successor.left;
        }
        return successor;
    }

    public boolean binaryArraySearch(String[] array, String name, int low, int high) {
        int mid = (high + low) / 2;
        if (array[mid].equals(name)) {
            return true;
        } else if (array[mid].compareTo(name) > 0) {
            return binaryArraySearch(array, name, low, mid);
        } else if (array[mid].compareTo(name) < 0)
            return binaryArraySearch(array, name, mid + 1, high);
        return false;
    }
}
