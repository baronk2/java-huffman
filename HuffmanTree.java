/*
Kevin Baron
4/30/13
CSE 143 Assignment #8
Huffman Tree
*/

import java.util.Queue;
import java.util.PriorityQueue;//helps create the desired structure of a Huffman tree
import java.io.PrintStream;//for writing Huffman codes and decoded text to files
import java.util.Scanner;//for reading in Huffman codes

public class HuffmanTree {
	
	private HuffmanNode overallRoot;//stores the root of the entire Huffman tree
	
	//pre : given an array of nonnegative integers which represents the frequency of occurence of
	//      various characters in a text file
	//post: overallRoot stores a reference to the root of a Huffman tree, with the most frequent
	//      characters closest to the root of the tree
	public HuffmanTree(int[] count) {
		//add all characters which occur at least once to a PriorityQueue as HuffmanNode leaves
		Queue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
		for (int i = 0; i < count.length; i++)
			if (count[i] > 0)
				q.add(new HuffmanNode(i, count[i]));
		//also add a single eof character whose value is one greater than the maximum acceptable character value
		q.add(new HuffmanNode(count.length, 1));
		//use the natural ordering of HuffmanNodes to group all the leaves in the PriorityQueue together,
		//starting with low-frequency characters, until one HuffmanNode is left.
		while (q.size() > 1) {
			HuffmanNode left = q.remove();
			HuffmanNode right = q.remove();
			//link two nodes together into a branch whose frquency is the sum of its constituents'
			q.add(new HuffmanNode(left.frequency + right.frequency, left, right));
		}//eo while
		//store a reference to the one remaining node
		overallRoot = q.remove();
	}//eo HuffmanTree(int[]) constructor
	
	//pre : reading from a file in legal, standard notation
	//post: a HuffmanTree has been constructed from the blueprints of a code/key file
	public HuffmanTree(Scanner input) {
		overallRoot = new HuffmanNode();
		while (input.hasNextLine()) {
			//grab the character value and the associated Huffman binary number
			int n = Integer.parseInt(input.nextLine());
			String code = input.nextLine();
			//navigate through any existing binary tree, using 0's and 1's to decide which direction to go until there 
			//are no more digits to read from. If no node exists where the next step needs to be taken, make a new node
			HuffmanNode current = overallRoot;
			for (int i = 0; i < code.length(); i++) {
				if (code.charAt(i) == '0') {
					if (current.left == null)
						current.left = new HuffmanNode();
					current = current.left;
				} else {//bit == '1'
					if (current.right == null)
						current.right = new HuffmanNode();
					current = current.right;
				}//eo if else
			}//eo for
			//the binary digits have led to a final destination; assign the character which was scanned in earlier
			current.ch = n;
		}//eo while
	}//eo HuffmanTree(Scanner) constructor
	
	//post: the Huffman tree to which overallRoot stores a reference has been recorded to an exterior file
	public void write(PrintStream output) {
		write(overallRoot, "", output);
	}//eo write(PrintStream)
	
	//pre : the given String contains only 0's and 1's
	//post: all of this root's subtrees have been printed in standard format
	private void write(HuffmanNode root, String binary, PrintStream output) {
		//base case: a leaf has been identified by its null left (and therefore also right) branch.
		//print the leaf's character value and the String binary
		if (root.left == null)
			output.print(root.ch + "\n" + binary + "\n");
		//recursive case: a branch has been identified. continue left by passing the binary code down with
		//a 0 (if going left) or a 1 (if going right) attached to the end
		else {
			write(root.left, binary + "0", output);
			write(root.right, binary + "1", output);
		}//eo else
	}//eo write(HuffmanNode, PrintStream)
	
	//pre : eof is one greater than the maximum possible value of all other characters in a code
	//post: a decoded version of the text has been printed and should exactly match the original text
	public void decode(BitInputStream input, PrintStream output, int eof) {
		HuffmanNode current = overallRoot;
		//navigate through the Huffman tree one bit at a time until a leaf is reached
		while (current.left != null) {
			if (input.readBit() == 0)
				current = current.left;
			else
				current = current.right;
		}//eo while
		//keep decoding and printing characters until the eof character is decoded and identified
		while (current.ch != eof) {
			output.write(current.ch);
			current = overallRoot;//important to reset to the top of the tree for each new character
			while (current.left != null) {
				if (input.readBit() == 0)
					current = current.left;
				else
					current = current.right;
			}//eo while
		}//eo while
	}//eo decode
	
}//eo HuffmanTree class