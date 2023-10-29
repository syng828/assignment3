package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TreeAlgorithms {
    public static void main(String[] args) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("shortnames.txt"));
            String test = br.readLine();
            System.out.println("We're in the middle of Tree23 insertion and traverse");
            while (test != null) {
                BinaryTree.insert(test);
                Tree23.insert(test);
                test = br.readLine();
            }
            BinaryTree.traverse();
            System.out.println("");
            Tree23.traverse();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
