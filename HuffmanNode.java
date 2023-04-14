/*
Kevin Baron
4/30/13
CSE 143 Assignment #8
Huffman Node
*/

public class HuffmanNode implements Comparable<HuffmanNode> {
	
	public int ch;//keep track of what character this node represents, if any
	public int frequency;//used for natural ordering of HuffmanNodes
	public HuffmanNode left;
	public HuffmanNode right;
	
	//post: all fields have been set to the desired values
	//(not actually used by HuffmanTree, but instead by all other constructors)
	public HuffmanNode(int ch, int frequency, HuffmanNode left, HuffmanNode right) {
		this.ch = ch;
		this.frequency = frequency;
		this.left = left;
		this.right = right;
	}//eo HuffmanNode(int, int, HuffmanNode, HuffmanNode) constructor
	
	//creates the simplest HuffmanNode, with both character value and frequency invalid.
	//used for building a tree top-down, one step at a time
	public HuffmanNode() {
		this(-1, -1, null, null);
	}//eo HuffmanNode()
	
	//used for creating the leaves which get added to the PriorityQueue
	public HuffmanNode(int ch, int frequency) {
		this(ch, frequency, null, null);
	}//eo HuffmanNode(int, int)
	
	//used for creating branches (which require no character) out of leaves and other branches.
	//bottom-up building approach
	public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
		this(-1, frequency, left, right);
	}//eo HuffmanNode(int, HuffmanNode, HuffmanNode) constructor
	
	//post: returns positive if this node's frequency is greater than the other's, negative if less, zero if equal
	public int compareTo(HuffmanNode other) {
		return this.frequency - other.frequency;
	}//eo compareTo
	
}//eo HuffmanNode class