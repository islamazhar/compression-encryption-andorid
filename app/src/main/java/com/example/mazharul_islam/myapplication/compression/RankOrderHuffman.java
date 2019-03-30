package com.example.mazharul_islam.myapplication.compression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.mazharul_islam.myapplication.encryption.ECC;
import com.example.mazharul_islam.myapplication.utilities.Element;
import com.example.mazharul_islam.myapplication.utilities.RedBlackBST;

public class RankOrderHuffman {

		// alphabet size of extended ASCII
		private static final int R = 256;
		String filename = null;
		static String[] st = null;
		static Integer length = 0;
		static Integer maxLevel= 25;
		static byte[] plainText = null;
		static byte[] cipherText = null;
		static ArrayList<RedBlackBST<Element>> ht = null;
		static ArrayList<RedBlackBST<Element>> ht1 = null;
		static ArrayList<Map<Integer,String>> Map1= null;
		static Map<String, Integer> Map2=null;
		static ECC ecc = null;
		// Do not instantiate.
		public RankOrderHuffman() { 
			ht = new ArrayList<RedBlackBST<Element>>();
			ht1 = new ArrayList<RedBlackBST<Element>>();
			Map1 = new ArrayList<Map<Integer,String>>();
			for(int i=0;i<maxLevel;i++) {
				ht.add(new RedBlackBST<Element>());
				ht1.add(new RedBlackBST<Element>());
				Map1.add(new HashMap<Integer,String>());
			}
			ecc = new ECC();
			Map2 = new HashMap<String, Integer>();
			
		}

		// Huffman trie node
		private static class Node implements Comparable<Node> {
			private final char ch;
			private final int freq;
			private final Node left, right;

			Node(char ch, int freq, Node left, Node right) {
				this.ch    = ch;
				this.freq  = freq;
				this.left  = left;
				this.right = right;
			}

			// is the node a leaf node?
			private boolean isLeaf() {
				assert ((left == null) && (right == null)) || ((left != null) && (right != null));
				return (left == null) && (right == null);
			}

			// compare, based on frequency
			public int compareTo(Node that) {
				return this.freq - that.freq;
			}
		}

		/**
		 * Reads a sequence of 8-bit bytes from standard input; compresses them
		 * using Huffman codes with an 8-bit alphabet; and writes the results
		 * to standard output.
		 */
		public static long compress(String filename) {
			// read the input
			String s = BinaryStdIn.readString();
			char[] input = s.toCharArray();

			// tabulate frequency counts
			int[] freq = new int[R];
			for (int i = 0; i < input.length; i++) {
				freq[input[i]]++;
			}

			// build Huffman trie
			Node root = buildTrie(freq);

			// build code table
			st = new String[R];
			buildCode(st, root, "");

			// print trie for decoder
			
			plainText = new byte[length+1];
			length = 0;
			writeTrie(root); // interesting place apply encryption on the root and then write.
			
			BinaryStdOut.close();
			BinaryStdOut.takeInputFile(filename);
			// print number of bytes in original uncompressed message
			
			BinaryStdOut.write(input.length);
			long ss = System.currentTimeMillis();
			
			/*
			for(int i=0;i<plainText.length;i++) {
				System.out.print(plainText[i]);
			}
			System.out.println("");
			*/
			
			
			for (int i = 0; i < input.length; i++) {
				freq[input[i]]= 0;
			}
			
			for (int i = 0; i < input.length; i++) {
				
				String code = st[input[i]];
				////////////////////////////////// 
				//changes should be here for rank order Huffman tree.
				Integer c = freq[input[i]];
				Element e = new Element(c,input[i]);
				Integer len = code.length();
				RedBlackBST<Element> h = ht.get(len);
				//System.out.println(h.toString());
				Integer pos = h.rank(e);
				code = Map1.get(len).get(pos);
				//System.out.println(Map1.size()+" "+pos);
				////////////////////////////////////////
				
				
				for (int j = 0; j < code.length(); j++) {
					if (code.charAt(j) == '0') {
						BinaryStdOut.write(false);
					}
					else if (code.charAt(j) == '1') {
						BinaryStdOut.write(true);
					}
					else throw new IllegalStateException("Illegal state");
				}
				h.delete(e);
				e.count++;
				freq[input[i]]++;
				h.put(e);
			}
		//	cipherText = ecc.encryption(plainText);
			long e = System.currentTimeMillis();
			
			// close output stream
			BinaryStdOut.close();
			return e-ss;
		}

		// build the Huffman trie given frequencies
		private static Node buildTrie(int[] freq) {

			// initialze priority queue with singleton trees
			MinPQ<Node> pq = new MinPQ<Node>();
			for (char i = 0; i < R; i++)
				if (freq[i] > 0)
					pq.insert(new Node(i, freq[i], null, null));

			// special case in case there is only one character with a nonzero frequency
			if (pq.size() == 1) {
				if (freq['\0'] == 0) pq.insert(new Node('\0', 0, null, null));
				else                 pq.insert(new Node('\1', 0, null, null));
			}

			// merge two smallest trees
			while (pq.size() > 1) {
				Node left  = pq.delMin();
				Node right = pq.delMin();
				Node parent = new Node('\0', left.freq + right.freq, left, right);
				pq.insert(parent);
			}
			return pq.delMin();
		}


		// write bitstring-encoded trie to standard output
		private static void writeTrie(Node x) {
			if (x.isLeaf()) {
				BinaryStdOut.write(true);
				plainText[length++] = 1;
				BinaryStdOut.write(x.ch, 8);
				return;
			}
			plainText[length++] = 0;
			BinaryStdOut.write(false);
			writeTrie(x.left);
			writeTrie(x.right);
		}

		// make a lookup table from symbols and their encodings
		private static void buildCode(String[] st, Node x, String s) {
			if (!x.isLeaf()) {
				buildCode(st, x.left,  s + '0');
				buildCode(st, x.right, s + '1');
			}
			else {
				st[x.ch] = s;
				
				
				//////////////////////////////////// 
				//extra for order rank tree
				Integer len = s.length();
				RedBlackBST<Element> redblackTree = ht.get(len);
				redblackTree.put(new Element(0,x.ch));
				redblackTree = ht1.get(len);
				redblackTree.put(new Element(0,x.ch)); // for decoding
				Integer siz = redblackTree.size(); 
				Map2.put(s, siz-1);
				length +=len;
				Map<Integer, String> Map3 = Map1.get(len);
				Map3.put(siz-1,s);
				//////////////////////////////
			}
		}

		/**
		 * Reads a sequence of bits that represents a Huffman-compressed message from
		 * standard input; expands them; and writes the results to standard output.
		 */
		public static long expand(String filename) {

			// read in Huffman trie from input stream
			Node root = readTrie(); 
			BinaryStdIn.close();
			BinaryStdIn.takeInputFile(filename);

			// number of bytes to write
			int length = BinaryStdIn.readInt();

			
			// decode using the Huffman trie
			long s = System.currentTimeMillis();
			for (int i = 0; i < length; i++) {
				Node x = root;
				while (!x.isLeaf()) {
					boolean bit = BinaryStdIn.readBoolean();
					if (bit) x = x.right;
					else     x = x.left;
				}
				String code = st[x.ch];
				Integer len = code.length();
				Integer c = Map2.get(code);
				RedBlackBST<Element> h = ht1.get(len);
				Element e = h.select(c);
				BinaryStdOut.write(e.value, 8);
				h.delete(e);
				e.count++;
				h.put(e);
			}
			//ecc.decryption(cipherText);
			long e = System.currentTimeMillis();
			BinaryStdOut.close();
			return e-s;
		}


		private static Node readTrie() {
			boolean isLeaf = BinaryStdIn.readBoolean();
			if (isLeaf) {
				return new Node(BinaryStdIn.readChar(), -1, null, null);
			}
			else {
				return new Node('\0', -1, readTrie(), readTrie());
			}
		}


		
		public double compress(String inputFileNamme, String compressed ) {
			String tree = compressed+".tree";
			BinaryStdIn.takeInputFile(inputFileNamme);
			BinaryStdOut.takeInputFile(tree);
			double t = 0;
			t += compress(compressed);

			BinaryStdIn.takeInputFile(tree);
			byte[] plainText = BinaryStdIn.readString().getBytes();
			BinaryStdIn.close();
			System.err.println(plainText);
			long cur = System.currentTimeMillis();
			cipherText = ecc.encryption(plainText);
			t += (System.currentTimeMillis()-cur);
			BinaryStdIn.close();


			return t;
		}
		public double deCompress(String compressed, String outputFile ) {
			String tree = compressed+".tree";
			BinaryStdIn.takeInputFile(tree);
			BinaryStdOut.takeInputFile(outputFile);
			double t = 0;
			t += expand(compressed);
			//t += Huffman.expand(compressed);
			long cur = System.currentTimeMillis();
			ecc.decryption(cipherText);
			t += (System.currentTimeMillis()-cur);
			return t;
		}
		public static void main(String[] args){
			RankOrderHuffman rankOrderHuffman = new RankOrderHuffman();
			double t1= rankOrderHuffman.compress("test-data/sizes/8192.txt", "test-data/sizes/8192.zip");
			t1 += rankOrderHuffman.deCompress("test-data/sizes/8192.zip", "test-data/sizes/8192.again.again.txt");
			Huffman huffman = new Huffman();
			double t2= huffman.compress("test-data/sizes/8192.txt", "test-data/sizes/8192.zip");
			t2 += huffman.deCompress("test-data/sizes/8192.zip", "test-data/sizes/8192.again.again.txt");
			System.out.println(t1+ " "+t2);
		}
	}
