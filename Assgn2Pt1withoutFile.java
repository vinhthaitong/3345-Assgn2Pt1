import java.util.Scanner;
public class Assignment2Part1AVLTree{
	public static void main(String[] args) {
	    int key;
	    Scanner input = new Scanner(System.in);
	    AVLTree tree = new AVLTree();
	    //SplayTree tree = new SplayTree();
	    while (true) {
	        System.out.print("Enter a key (-1 to exit): ");
	        key = input.nextInt();
	        if (key < 0) {
                break;
            }else{
                System.out.println("...Inserting " + key);
            }
	        tree.root = tree.insert(tree.root, key);
	        tree.printTree(tree.root, "", false);
            System.out.println();
	    }
	    
	    System.out.print("In Order Traversal: ");
	    tree.inOrder(tree.root);
	    System.out.println("\nFinal Tree");
	    tree.printTree(tree.root, "", false);
	    
	}
}

class BookNode {
    String ISBN, title, author;
    BookNode left, right;
    int height;
    
    BookNode (String ISBN, String title, String author) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        height = 0;
        left= null;
        right = null;
    }
}


class TreeNode {
    int key, height;
    TreeNode left, right;
    
    TreeNode(int d) {
        key = d;
        height = 0;
        // left= null not required statement; b/c left and right are instance reference variables 
        // right = null not required statement 
    }
}

class AVLTree {
    static int count;
    TreeNode root;
    
    public TreeNode insert(TreeNode node, int key) {
        boolean calledViolation = false; 
        
        /* 1. Perform the normal BST insertion */
        if (node == null) {
            if (root == null){
                System.out.println("Root is null");
                System.out.println("Inserting book object " + key + " at root level");
            } 
            return (new TreeNode(key));
        }
            
        
        if (key < node.key) {
            System.out.println("Book object " + key + " < " + node.key + "looking at left subtree");
            if(node.left == null){
                System.out.println("Found null, inserting Book object " + key + " as left child of " + node.key);
            }
            node.left = insert(node.left, key);
        }  
        else if (key > node.key) {
            //System.out.printf("Inserting: %d, node.right.key: %d, node.key: %d\n", key, node.right.key, node.key);
            System.out.println("Book object " + key + " > " + node.key + " looking at right subtree");
            if(node.right == null){
                System.out.println("Found null, inserting Book object " + key + " as right child of " + node.key);
            }
            node.right = insert(node.right, key);
        }
        else // Duplicate keys not allowed
            return node;

        /* 2. Update height of this ancestor node */
        node.height = 1 + max(getHeight(node.left), getHeight(node.right));

        /* 3. Get the balance factor of this ancestor node to check whether
           this node became unbalanced */
        int balance = getBalance(node);
        //System.out.printf("In balance after inserting: %d and node.key: %d\n", key, node.key);
        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && key < node.left.key) {
            //System.out.printf("Left case %d: bal %d node %d key %d node.left.key: %d\n", count++, balance, node.key, key, node.left.key);
            System.out.println("...Checking: " + node.key);
            System.out.println("Adjusting height");
            System.out.println("Detecting Potential Imbalance, AVL property is violated");
            System.out.println("Calling single rotation for a left-lrft case on " + node.left.key + " and " + root.key);
            calledViolation = true;
            return rightRotate(node);
        }
        // Right Right Case
        if (balance < -1 && key > node.right.key) {
            //System.out.printf("Right case %d: bal %d node %d key %d node.right.key: %d\n", count++, balance, node.key, key, node.right.key);
            System.out.println("...Checking: " + node.key);
            System.out.println("Adjusting height");
            System.out.println("Detecting Potential Imbalance, AVL property is violated");
            System.out.println("Calling single rotation for a right-right case on " + node.right.key + " and " + root.key);
            calledViolation = true;
            return leftRotate(node);
        }
        // Left Right Case
        if (balance > 1 && key > node.left.key) {
            System.out.println("...Checking: " + node.key);
            System.out.println("Adjusting height");
            System.out.println("Detecting Potential Imbalance, AVL property is violated");
            System.out.println("Calling double rotation for a left-right case");
            calledViolation = true;
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // Right Left Case
        if (balance < -1 && key < node.right.key) {
            System.out.println("...Checking: " + node.key);
            System.out.println("Adjusting height");
            System.out.println("Detecting Potential Imbalance, AVL property is violated");
            System.out.println("Calling double rotation for a right-left case");
            calledViolation = true;
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        
        if (balance >= -1 && balance <= 1){
            System.out.println("...Checking: " + node.key);
            System.out.println("Adjusting height");
            System.out.print("Detecting Potential Imbalance, AVL property is not violated\n");
            calledViolation = true;
        }
        
        /* return the (unchanged) node pointer */
        //System.out.printf("returning node from insert %d\n", node.key);
        return node;
    }

    
    int getHeight(TreeNode N) {
        if (N == null) 
            return -1;
        return N.height;
    }
    
    public int max(int a, int b) { return (a > b ? a : b); }
    
    public int getBalance(TreeNode N) {
        if (N == null) return 0;
        return getHeight(N.left) - getHeight(N.right);
    }
    
    // A utility function to left rotate subtree rooted with x
    TreeNode leftRotate(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;

        // Return new root
        return y;
    }    
    
    // A utility function to right rotate subtree rooted with y
    TreeNode rightRotate(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;

        // Return new root
        return x;
    }
    
    public void printTree(TreeNode node, String prefix, boolean isLeft) {
        if (node != null) {
            //System.out.printf("%s%s%d(bal: %d, height: %d)\n", prefix, (isLeft ? "|â”€â”€" : "|__ "), node.key, getBalance(node), getHeight(node));
            System.out.printf("%s%s%d(b: %d, h: %d)\n", prefix, (isLeft ? "L " : "R "), node.key, getBalance(node), getHeight(node));
            printTree(node.left, prefix + (isLeft ? "|  " : "   "), true);
            printTree(node.right, prefix + (isLeft ? "|  " : "   "), false);
            
        }
    }
    
    public void inOrder(TreeNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.key + " ");
            inOrder(node.right);
        }
    }
}
