package src;

class BinaryTree {
    private static TreeNode root = null;

    static class TreeNode {
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

    public static void insert(String name) { // insert with iteration method and without returning the node
        if (root == null) {
            root = new TreeNode(name);
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

    public static void traverse() {
        if (root == null) {
            System.out.println("No names found");
        } else {
            traverseHelper(root);
        }
    }

    public static void traverseHelper(TreeNode node) { // helper method
        if (node == null) {
            return;
        }
        traverseHelper(node.left);
        System.out.println(node.val);
        traverseHelper(node.right);
    }
}
