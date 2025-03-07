import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Assignment2Part1AVLTree {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        AVLTree tree = new AVLTree();
        
        System.out.print("Enter the filename: ");
        String filename = input.nextLine();
        
        try {
            File file = new File(filename);
            Scanner fileReader = new Scanner(file);
            
            while (fileReader.hasNextLine()) {
                String isbn = fileReader.nextLine();
                String title = fileReader.nextLine();
                String author = fileReader.nextLine();
                
                System.out.println("...Inserting ISBN: " + isbn);
                tree.root = tree.insert(tree.root, isbn, title, author);
                tree.printTree(tree.root, "", false);
                System.out.println();
            }
            
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File '" + filename + "' not found.");
            input.close();
            return;
        }
        
        System.out.print("In Order Traversal: ");
        tree.inOrder(tree.root);
        System.out.println("\nFinal Tree");
        tree.printTree(tree.root, "", false);
        
        input.close();
    }
}

class BookNode {
    String ISBN, title, author;
    BookNode left, right;
    int height;
    
    BookNode(String ISBN, String title, String author) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        height = 0;
        left = null;
        right = null;
    }
}

class AVLTree {
    static int count;
    BookNode root;
    
    public BookNode insert(BookNode node, String isbn, String title, String author) {
        /* 1. Perform the normal BST insertion */
        if (node == null) {
            if (root == null) {
                System.out.println("Root is null");
                System.out.println("Inserting Book object ISBN " + isbn + " at root level");
            }
            return (new BookNode(isbn, title, author));
        }
        
        int compareResult = isbn.compareTo(node.ISBN);
        
        if (compareResult < 0) {
            System.out.println("Book object ISBN " + isbn + " < " + node.ISBN + " looking at left subtree");
            if (node.left == null) {
                System.out.println("Found null, inserting Book object ISBN " + isbn + " as left child of " + node.ISBN);
            }
            node.left = insert(node.left, isbn, title, author);
        }
        else if (compareResult > 0) {
            System.out.println("Book object ISBN " + isbn + " > " + node.ISBN + " looking at right subtree");
            if (node.right == null) {
                System.out.println("Found null, inserting Book object ISBN " + isbn + " as right child of " + node.ISBN);
            }
            node.right = insert(node.right, isbn, title, author);
        }
        else { // Duplicate ISBN not allowed
            return node;
        }

        /* 2. Update height of this ancestor node */
        node.height = 1 + max(getHeight(node.left), getHeight(node.right));

        /* 3. Get the balance factor of this ancestor node */
        int balance = getBalance(node);
        
        // Left Left Case
        if (balance > 1 && isbn.compareTo(node.left.ISBN) < 0) {
            System.out.println("...Checking: " + node.ISBN);
            System.out.println("Adjusting height");
            System.out.println("Detecting Potential Imbalance, AVL property is violated");
            System.out.println("Calling single rotation for a left-left case on " + node.left.ISBN + " and " + node.ISBN);
            return rightRotate(node);
        }
        
        // Right Right Case
        if (balance < -1 && isbn.compareTo(node.right.ISBN) > 0) {
            System.out.println("...Checking: " + node.ISBN);
            System.out.println("Adjusting height");
            System.out.println("Detecting Potential Imbalance, AVL property is violated");
            System.out.println("Calling single rotation for a right-right case on " + node.right.ISBN + " and " + node.ISBN);
            return leftRotate(node);
        }
        
        // Left Right Case
        if (balance > 1 && isbn.compareTo(node.left.ISBN) > 0) {
            System.out.println("...Checking: " + node.ISBN);
            System.out.println("Adjusting height");
            System.out.println("Detecting Potential Imbalance, AVL property is violated");
            System.out.println("Calling double rotation for a left-right case");
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && isbn.compareTo(node.right.ISBN) < 0) {
            System.out.println("...Checking: " + node.ISBN);
            System.out.println("Adjusting height");
            System.out.println("Detecting Potential Imbalance, AVL property is violated");
            System.out.println("Calling double rotation for a right-left case");
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        
        if (balance >= -1 && balance <= 1) {
            System.out.println("...Checking: " + node.ISBN);
            System.out.println("Adjusting height");
            System.out.println("Detecting Potential Imbalance, AVL property is not violated");
        }
        
        return node;
    }
    
    int getHeight(BookNode N) {
        if (N == null) 
            return -1;
        return N.height;
    }
    
    public int max(int a, int b) {
        return (a > b ? a : b);
    }
    
    public int getBalance(BookNode N) {
        if (N == null) 
            return 0;
        return getHeight(N.left) - getHeight(N.right);
    }
    
    // A utility function to left rotate subtree rooted with x
    BookNode leftRotate(BookNode x) {
        BookNode y = x.right;
        BookNode T2 = y.left;

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
    BookNode rightRotate(BookNode y) {
        BookNode x = y.left;
        BookNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;

        // Return new root
        return x;
    }
    
    public void printTree(BookNode node, String prefix, boolean isLeft) {
        if (node != null) {
            System.out.printf("%s%s%s(b: %d, h: %d)\n", 
                prefix, 
                (isLeft ? "L " : "R "), 
                node.ISBN, 
                getBalance(node), 
                getHeight(node));
            System.out.printf("%s  Title: %s\n", prefix, node.title);
            System.out.printf("%s  Author: %s\n", prefix, node.author);
            
            printTree(node.left, prefix + (isLeft ? "|  " : "   "), true);
            printTree(node.right, prefix + (isLeft ? "|  " : "   "), false);
        }
    }
    
    public void inOrder(BookNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.printf("%s: \"%s\" by %s\n", node.ISBN, node.title, node.author);
            inOrder(node.right);
        }
    }
}
