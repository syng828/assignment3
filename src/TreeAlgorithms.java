package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TreeAlgorithms {

    private static BinaryTree binaryTree = new BinaryTree();
    private static Tree23 tree23 = new Tree23();

    public static void test23Search() {
        BufferedReader br = null;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        try {
            br = new BufferedReader(new FileReader("short_names.txt"));
            String searchName = br.readLine();
            while (searchName != null) {
                long start = System.currentTimeMillis();
                tree23.search(searchName);
                long elapsed = System.currentTimeMillis() - start;
                min = Math.min(min, elapsed);
                max = Math.max(min, elapsed);
                sum += elapsed;
                searchName = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long average = sum / 100;
        System.out.println("--Test 23 Search--"); // why only 0ms?
        System.out.println("Min Time: " + min);
        System.out.println("Max Time: " + max);
        System.out.println("Average Time: " + average);
    }

    public static void testBinarySearch() {

    }

    public static void testLinearSearch() {
        BufferedReader br = null;
        BufferedReader secondBr = null;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        try {
            br = new BufferedReader(new FileReader("sorted_names_160k.txt"));
            secondBr = new BufferedReader(new FileReader("short_names.txt"));
            String test = br.readLine();
            String currentName = secondBr.readLine();

            while (currentName != null) {
                long start = System.currentTimeMillis();
                while (test != null && !test.equals(currentName)) {
                    test = br.readLine();
                }
                long elapsed = System.currentTimeMillis() - start;
                min = Math.min(min, elapsed);
                max = Math.max(min, elapsed);
                sum += elapsed;

                currentName = secondBr.readLine();
            }

        } catch (IOException e) {
            System.out.println(e.toString());
        }

        long average = sum / 100;
        System.out.println("-_Linear Search--"); // why only 0ms?
        System.out.println("Min Time: " + min);
        System.out.println("Max Time: " + max);
        System.out.println("Average Time: " + average);
    }

    public static void testBinaryArraySearch() {
    }

    public static void main(String[] args) {
        BufferedReader br = null;

        // stores unsorted names into trees
        try {
            br = new BufferedReader(new FileReader("unsorted_names_160k.txt"));
            String test = br.readLine();
            System.out.println("We're in the middle of Tree23 insertion");

            while (test != null) {
                binaryTree.insert(test);
                tree23.insert(test);
                test = br.readLine();
            }
            binaryTree.traverse();
            test23Search();
            // add testBinarySearch
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        testLinearSearch();
        // add testBinaryArraySearch

    }
}
