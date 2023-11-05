package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TreeAlgorithms {

    private static BinaryTree binaryTree = new BinaryTree();
    private static Tree234 tree234 = new Tree234();

    public static void test234Search() {
        BufferedReader br = null;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        try {
            br = new BufferedReader(new FileReader("short_names.txt"));
            String searchName = br.readLine();
            while (searchName != null) {
                long start = System.nanoTime();
                tree234.search(searchName);
                long elapsed = System.nanoTime() - start;
                min = Math.min(min, elapsed);
                max = Math.max(min, elapsed);
                sum += elapsed;
                searchName = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long average = sum / 100;
        System.out.println("--Test 23 Search--");
        System.out.println("Min Time: " + min);
        System.out.println("Max Time: " + max);
        System.out.println("Average Time: " + average);
    }

    public static void testBinarySearch() {
        BufferedReader br = null;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        try {
            br = new BufferedReader(new FileReader("short_names.txt"));
            String searchName = br.readLine();
            while (searchName != null) {
                long start = System.nanoTime();
                binaryTree.search(searchName);
                long elapsed = System.nanoTime() - start;
                min = Math.min(min, elapsed);
                max = Math.max(min, elapsed);
                sum += elapsed;
                searchName = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long average = sum / 100;
        System.out.println("--Binary Search--");
        System.out.println("Min Time: " + min);
        System.out.println("Max Time: " + max);
        System.out.println("Average Time: " + average);
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
                long start = System.nanoTime();
                for (String s : names) {
                    if (s.equals(test)) {
                        break;
                    }
                }
                long elapsed = System.nanoTime() - start;
                min = Math.min(min, elapsed);
                max = Math.max(min, elapsed);
                sum += elapsed;

                test = br.readLine();
            }

        } catch (IOException e) {
            System.out.println(e.toString());
        }

        long average = sum / 100;
        System.out.println("--Linear Search--");
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
                long start = System.nanoTime();
                binaryTree.binaryArraySearch(names.toArray(new String[0]), test, 0, names.size() - 1);
                long elapsed = System.nanoTime() - start;
                min = Math.min(min, elapsed);
                max = Math.max(min, elapsed);
                sum += elapsed;

                test = br.readLine();
            }

        } catch (IOException e) {
            System.out.println(e.toString());
        }

        long average = sum / 100;
        System.out.println("--Binary Array Search--");
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

        long min234 = Long.MAX_VALUE;
        long max234 = Long.MIN_VALUE;
        long sum234 = 0;

        int top10 = names.size() / 10;

        for (int i = 0; i < top10; i++) {
            long start = System.nanoTime();
            binaryTree.delete(names.get(i));
            long elapsed = System.nanoTime() - start;
            min = Math.min(min, elapsed);
            max = Math.max(min, elapsed);
            sum += elapsed;

            long start234 = System.nanoTime();
            tree234.delete(names.get(i));
            long elapsed234 = System.nanoTime() - start234;
            min234 = Math.min(min234, elapsed234);
            max234 = Math.max(min234, elapsed234);
            sum234 += elapsed234;
        }
        long average = sum / top10;
        System.out.println("--Binary Tree Delete Top 10%--");
        System.out.println("Min Time: " + min);
        System.out.println("Max Time: " + max);
        System.out.println("Average Time: " + average);

        long average234 = sum234 / top10;
        System.out.println("--234 Tree Delete Top 10%--");
        System.out.println("Min Time: " + min234);
        System.out.println("Max Time: " + max234);
        System.out.println("Average Time: " + average234);
    }

    public static void deleteBottom10(ArrayList<String> names) {
        try {
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            long sum = 0;

            long min234 = Long.MAX_VALUE;
            long max234 = Long.MIN_VALUE;
            long sum234 = 0;

            int tenPercent = names.size() / 10;
            int bottom10Start = names.size() - 1 - tenPercent;

            for (int i = bottom10Start; i < names.size(); i++) {

                long start = System.nanoTime();
                String name = names.get(i);
                binaryTree.delete(name);
                long elapsed = System.nanoTime() - start;
                min = Math.min(min, elapsed);
                max = Math.max(min, elapsed);
                sum += elapsed;

                long start234 = System.nanoTime();
                tree234.delete(name);
                long elapsed234 = System.nanoTime() - start234;
                min234 = Math.min(min234, elapsed234);
                max234 = Math.max(min234, elapsed234);
                sum234 += elapsed234;
            }

            long average = sum / tenPercent;
            System.out.println("--Binary Tree Delete Bottom 10%--");
            System.out.println("Min Time: " + min);
            System.out.println("Max Time: " + max);
            System.out.println("Average Time: " + average);

            long average234 = sum234 / tenPercent;
            System.out.println("--234 Tree Delete Bottom 10%--");
            System.out.println("Min Time: " + min234);
            System.out.println("Max Time: " + max234);
            System.out.println("Average Time: " + average234);
            System.out.println("Done with deleting bottom 10%");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // stores unsorted names into trees
    public static void buildTrees(String fileName) {
        binaryTree.clear();
        tree234.clear();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
            String test = br.readLine();
            System.out.println("We're in the middle of Tree234 insertion");

            while (test != null) {
                binaryTree.insert(test);
                tree234.insert(test);
                test = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {
        try {
            // #5
            buildTrees("unsorted_names_160k.txt");
            test234Search();
            testBinarySearch();
            ArrayList<String> sortedNames = convertToArray();
            testLinearSearch(sortedNames);
            testBinaryArraySearch(sortedNames);

            // #6
            deleteTop10(sortedNames);
            // build the tree again
            buildTrees("unsorted_names_160k.txt");
            deleteBottom10(sortedNames);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
