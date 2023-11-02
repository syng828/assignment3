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

    public static void testLinearSearch(ArrayList<String> names) {
        BufferedReader br = null;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        try {
            br = new BufferedReader(new FileReader("short_names.txt"));
            String test = br.readLine();

            while (test != null) {
                long start = System.currentTimeMillis();
                for (String s : names) {
                    if (s.equals(test)) {
                        break;
                    }
                }
                long elapsed = System.currentTimeMillis() - start;
                min = Math.min(min, elapsed);
                max = Math.max(min, elapsed);
                sum += elapsed;

                test = br.readLine();
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

    public static void testBinaryArraySearch(ArrayList<String> names) {
        BufferedReader br = null;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        try {
            br = new BufferedReader(new FileReader("short_names.txt"));
            String test = br.readLine();

            while (test != null) {
                long start = System.currentTimeMillis();
                binaryTree.binaryArraySearch(names.toArray(new String[0]), test, 0, names.size() - 1);
                long elapsed = System.currentTimeMillis() - start;
                min = Math.min(min, elapsed);
                max = Math.max(min, elapsed);
                sum += elapsed;

                test = br.readLine();
            }

        } catch (IOException e) {
            System.out.println(e.toString());
        }

        long average = sum / 100;
        System.out.println("--Binary Array Search--"); // why only 0ms?
        System.out.println("Min Time: " + min);
        System.out.println("Max Time: " + max);
        System.out.println("Average Time: " + average);
    }

    public static ArrayList<String> convertToArray() {
        BufferedReader br = null;
        ArrayList<String> names = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader("sorted_names_160k.txt"));
            String test = br.readLine();
            while (test != null) {
                names.add(test);
                test = br.readLine();
            }
            br.close();
            return names;
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public static void deleteTop10(ArrayList<String> names) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        int top10 = names.size() / 10;

        for (int i = 0; i < top10; i++) {
            long start = System.currentTimeMillis();
            binaryTree.delete(names.get(i));
            // add in 2 3 4 trees
            long elapsed = System.currentTimeMillis() - start;
            min = Math.min(min, elapsed);
            max = Math.max(min, elapsed);
            sum += elapsed;
        }
        long average = sum / top10;
        System.out.println("--Delete Top 10%--"); // why only 0ms?
        System.out.println("Min Time: " + min);
        System.out.println("Max Time: " + max);
        System.out.println("Average Time: " + average);
    }

    public static void deleteBottom10(ArrayList<String> names) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        int tenPercent = names.size() / 10;
        int bottom10Start = names.size() - 1 - tenPercent;

        for (int i = bottom10Start; i < names.size(); i++) {
            long start = System.currentTimeMillis();
            binaryTree.delete(names.get(i));
            // add in 2 3 4 trees
            long elapsed = System.currentTimeMillis() - start;
            min = Math.min(min, elapsed);
            max = Math.max(min, elapsed);
            sum += elapsed;
        }
        long average = sum / tenPercent;
        System.out.println("--Delete Bottom 10%--"); // why only 0ms?
        System.out.println("Min Time: " + min);
        System.out.println("Max Time: " + max);
        System.out.println("Average Time: " + average);
    }

    public static void main(String[] args) {
        BufferedReader br = null;

        // stores unsorted names into trees
        try {
            br = new BufferedReader(new FileReader("unsorted_names_160k.txt"));
            String test = br.readLine();
            System.out.println("We're in the middle of Tree23 insertion");

            while (test != null) {
                // binaryTree.insert(test);
                tree23.insert(test);
                test = br.readLine();
            }
            // test23Search();
            // testBinarySearch();
            br.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        ArrayList<String> sortedNames = convertToArray();
        // testBinaryArraySearch(sortedNames);
        // testLinearSearch(sortedNames);
        // add testBinaryArraySearch
        // deleteTop10(sortedNames);
        // binaryTree.traverse();

        // testBinaryArraySearch(sortedNames);

    }
}
