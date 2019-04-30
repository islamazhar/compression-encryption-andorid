package com.example.mazharul_islam.myapplication.compression;

import com.example.mazharul_islam.myapplication.encryption.ECC;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CHT {

    // CHT params
    private static final int R = 256;
    private static double x0 = 0.5;
    private static double p = 0.4;
    private static double alpha = 33.00;
    private static double beta = 33.00;
    private static int vm = 33;
    //private  static List<Node> huffmanNodes = null;
    // CHT params
    private static ECC ecc = null;


    public CHT() {
        //ecc = new ECC();
    }



    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
        }
    }

    private static Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // special case in case there is only one character with a nonzero frequency
        if (pq.size() == 1) {
            if (freq['\0'] == 0) pq.insert(new Node('\0', 0, null, null));
            else pq.insert(new Node('\1', 0, null, null));
        }

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }

    private static void listNodes(Node x, List<Node> huffmanNodes) {
        if (!x.isLeaf()) {
            listNodes(x.left, huffmanNodes);
            listNodes(x.right, huffmanNodes);
        } else {
            huffmanNodes.add(x);
        }
    }

    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }

    public static long compress() {
        long t1 = System.currentTimeMillis();
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++) {
            freq[input[i]]++;
        }

        Node root = buildTrie(freq);
        String[] st = new String[R];
        buildCode(st, root, "");
        writeTrie(root);
        BinaryStdOut.write(input.length);

        List<Node> huffmanNodes = new ArrayList<>();
        listNodes(root, huffmanNodes);
        //for(Node x : huffmanNodes) {
        //  System.out.println(x);
        //}

        for (int i = 0; i < input.length; i++) {
            char ch = input[i];
            int n = 1 + vm % 10;
            double xn = chaoticMap(x0, n);
            Double r = Math.floor(xn * Math.pow(2.00, alpha) + vm * Math.pow(2.00, beta)) % huffmanNodes.size();
            Integer ri = r.intValue();


            x0 = xn;
            vm = ch;


            Node h = huffmanNodes.get(ri);
            h.swap();
            //System.out.println(ri);
            buildCode(st, root, "");
            //System.out.println(ch);
            String code = st[ch];

            for (int j = 0; j < code.length(); j++) {

                if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                } else if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                } else {
                    throw new IllegalStateException("Illegal state");
                }
            }
        }
        long t2 = System.currentTimeMillis();

        return t2 - t1;

    }

    private static double chaoticMap(double x0, int n) {
        double x = x0;
        for (int i = 0; i < n; i++) {
            if (x <= p) {
                x = x / p;
            } else {
                x = (1 - x) / (1 - p);
            }
        }
        return x;
    }

    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }

    public static long expand() {

        // read in Huffman trie from input stream
        long t1 = System.currentTimeMillis();
        Node root = readTrie();
        // number of bytes to write
        int length = BinaryStdIn.readInt();
        // decode using the Huffman trie
        double xn = 0;
        String[] st = new String[R];
        List<Node> huffmanNodes = new ArrayList<>();
        listNodes(root, huffmanNodes);
        //for(Node x : huffmanNodes) {
        //  System.out.println(x);
        //}

        x0 = 0.5;
        vm = 33;

        for (int i = 0; i < length; i++) {

            int n = 1 + vm % 10;
            xn = chaoticMap(x0, n);
            Double r = Math.floor(xn * Math.pow(2.00, alpha) + vm * Math.pow(2.00, beta)) % huffmanNodes.size();
            Integer ri = r.intValue();


            Node h = huffmanNodes.get(ri);
            h.swap();
            buildCode(st, root, "");

            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) x = x.right;
                else x = x.left;
            }

            BinaryStdOut.write(x.ch, 8);
            x0 = xn;
            vm = x.ch;
        }

        long t2 = System.currentTimeMillis();

        return t2 - t1;
    }
    public long compress(String inputFileNamme, String compressedFileName) {

        BinaryStdIn.takeInputFile(inputFileNamme);
        BinaryStdOut.takeInputFile(compressedFileName);
        long compressTime = 0;
        compressTime += CHT.compress();
        BinaryStdIn.close();
        BinaryStdOut.close();
        return compressTime;
    }

    public long deCompress(String compressedFileName, String outputFileName) {
        BinaryStdIn.takeInputFile(compressedFileName);
        BinaryStdOut.takeInputFile(outputFileName);
        long decompressTime = CHT.expand();
        BinaryStdIn.close();
        BinaryStdOut.close();
        return decompressTime;
    }


    public static void main(String[] args) {

        String folderName = "/Users/mazharul.islam/Desktop/compression-files/large";
        String [] fileNames = {"bible.txt", "E.coli", "world192.txt","a.txt","aaa.txt", "alphabet.txt", "random.txt","pic"};
        for(String fileName: fileNames){
            //for(Integer siz = 860000;siz<=860000;siz=siz+860000)  {
            String source = folderName+"/"+ fileName;
            //System.out.println(source);
            String compressedFile = source+".cht";
            String outFile = source+".cht.again.txt";

            CHT cht = new CHT();
            double tim1 = cht.compress(source, compressedFile);
            double tim2 = cht.deCompress(compressedFile, outFile);
            System.out.println(fileName+","+"CHT,"+tim1+","+new File(source).length());
            System.out.println(fileName+","+"CHT,"+tim2+","+new File(source).length());
        }

    }
}

