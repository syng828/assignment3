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

    public int search() {
        return -1; // add method
    }
}
