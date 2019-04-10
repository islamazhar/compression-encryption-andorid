package com.example.mazharul_islam.myapplication.compression;


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
	//static Integer maxLevel= 25;
	static byte[] plainText = null;
	//static byte[] cipherText = null;
	static RedBlackBST<Element> ht = null;
	static RedBlackBST<Element> ht1 = null;
	static Map<Integer,String> Map1= null;
	static Map<String, Integer> Map2=null;
	static ECC ecc = null;
	// Do not instantiate.
	public RankOrderHuffman() {
		ht = new RedBlackBST<Element>();
		ht1 = new RedBlackBST<Element>();
		Map1 = new HashMap<Integer,String>();
		Map2 = new HashMap<String, Integer>();
		ecc = new ECC();
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
			/*
			try {
				ht1 = (RedBlackBST) ht.clone();
				System.out.println(ht1.toString());
			}catch ( CloneNotSupportedException ex){
				ex.printStackTrace();
			}
			*/
		// print trie for decoder

		plainText = new byte[length+1];
		length = 0;
		writeTrie(root); // interesting place apply encryption on the root and then write.

		BinaryStdOut.close();
		BinaryStdOut.takeInputFile(filename);
		// print number of bytes in original uncompressed message

		BinaryStdOut.write(input.length);


			/*
			for(int i=0;i<plainText.length;i++) {
				System.out.print(plainText[i]);
			}
			System.out.println("");
			*/


		for (int i = 0; i < input.length; i++) {
			freq[input[i]]= 0;
		}
		long ss = System.currentTimeMillis();

		for (int i = 0; i < input.length; i++) {

			String code = st[input[i]];
			//////////////////////////////////
			//changes should be here for rank order Huffman tree.
			Integer c = freq[input[i]];
			Integer len = code.length();
			Element e = new Element(c,input[i],len);
			//System.out.println(h.toString());
			Integer pos = ht.rank(e);
			code = Map1.get(pos);
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
			ht.delete(e);
			e.curFrequency++;
			freq[input[i]]++;
			ht.put(e);
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
			///extra for HEliOS
			////////////////////////////////////
			Integer len = s.length();
			Element e  = new Element(0,x.ch,len);
			ht.put(e);
			ht1.put(new Element(0,x.ch,len)); // for decoding
			Integer siz = ht.size();
			Map2.put(s, siz-1);
			length +=len;
			Map1.put(siz-1,s);
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
			Element e = ht1.select(c);
			BinaryStdOut.write(e.value, 8);
			ht1.delete(e);
			e.curFrequency++;
			ht1.put(e);
		}
		//ecc.decryption(cipherText);
		long e = System.currentTimeMillis();
		BinaryStdIn.close();
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
			long cur = System.currentTimeMillis();
			ecc.encryption(tree,tree+".encrypted");
			t += (System.currentTimeMillis()-cur);
			return t;
		}
		public double deCompress(String compressed, String outputFile ) {
            double t = 0;
		    t += ecc.decryption(compressed+".tree.encrypted",compressed+".tree");
			BinaryStdIn.takeInputFile(compressed+".tree");
			BinaryStdOut.takeInputFile(outputFile);
			t += expand(compressed);


			return t;
		}

	}
