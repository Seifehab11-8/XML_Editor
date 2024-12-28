package DSAproject;

public class HuffmanNode implements Comparable<HuffmanNode>{
	private int frequency;
	private char name;
	private HuffmanNode rightNode;
	private HuffmanNode leftNode;
	
	HuffmanNode(char name, int frequency) {
		this.setName(name);
		this.setFrequency(frequency);
		this.setLeftNode(this.setRightNode(null));
	}
	
	HuffmanNode() {
		
	}

	public HuffmanNode getRightNode() {
		return rightNode;
	}

	public HuffmanNode setRightNode(HuffmanNode rightNode) {
		this.rightNode = rightNode;
		return rightNode;
	}

	public HuffmanNode getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(HuffmanNode leftNode) {
		this.leftNode = leftNode;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public char getName() {
		return name;
	}

	public void setName(char name) {
		this.name = name;
	}

	@Override
	public int compareTo(HuffmanNode other) {
		return Integer.compare(this.frequency, other.frequency);
	}
}
