package xml_terminalMode_WS;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanEncoder {
	private HashMap<Character, Integer> freqMap;
	private HashMap<Character, String> huffmanCodeMap;
	private HuffmanNode root;
	
	HuffmanEncoder(){
		freqMap = new HashMap<>();
		huffmanCodeMap = new HashMap<>();
	}
	
	private void createFrequencyMap(List<String> bufferList) {
		for(String str: bufferList) {
			for(int i = 0; i < str.length(); i++) {
				if(freqMap.containsKey(str.charAt(i))) {
					freqMap.put(str.charAt(i), freqMap.get(str.charAt(i))+1);
				}else {
					freqMap.put(str.charAt(i), 1);
				}
			}
		}
	}
	
	private void createHuffmanTree() {
		PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
		//Copy data to priority queue
		for(Map.Entry<Character, Integer> entry: freqMap.entrySet()) {
			pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
		}
		
		while(pq.size() > 1) {
			HuffmanNode left = pq.poll();
			HuffmanNode right = pq.poll();
			HuffmanNode tempNode = new HuffmanNode('\0', left.getFrequency()+ right.getFrequency());
			tempNode.setLeftNode(left);
			tempNode.setRightNode(right);
			pq.add(tempNode);
		}
		
		root = pq.poll();
	}
	
	private void createHuffmanCodes(HuffmanNode node, String code) {
		if(node == null) return;
		
		if(node.getName() != '\0') {
			huffmanCodeMap.put(node.getName(), code);
		}
		createHuffmanCodes(node.getLeftNode(), code + '0');
		createHuffmanCodes(node.getRightNode(), code + '1');
	}
	
	public List<BitSet> encodeList(List<String> bufferList) {
		List<String> byteRepresentation = new ArrayList<>();
		List<BitSet> encodedFile = new ArrayList<>();
		
		createFrequencyMap(bufferList);
		createHuffmanTree();
		createHuffmanCodes(root, "");
		
		for(String str: bufferList) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < str.length(); i++) {
				sb.append(huffmanCodeMap.get(str.charAt(i)));
			}
			byteRepresentation.add(sb.toString());
		}
		
		for(String str: byteRepresentation) {
			BitSet bst = new BitSet(str.length());
			for(int i = 0; i < str.length(); i++) {
				if(str.charAt(i) == '1') {
					bst.set(i);
				}
			}
			encodedFile.add(bst);
		}
	
		
		return encodedFile;
	}
	
	public HashMap<Character, String> getHuffmanMap() {
		return huffmanCodeMap;
	}
}
