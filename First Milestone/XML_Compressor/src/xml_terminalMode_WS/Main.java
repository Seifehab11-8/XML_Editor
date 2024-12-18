package xml_terminalMode_WS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean check;
		Compression c1 = new Compression();
		HashMap <String, Integer> cc;
		
		check = c1.compressXML("C:\\Users\\semaz\\OneDrive\\Documents\\XML project WS\\xml_terminalMode_WS\\text.xml.txt");
		c1.compressJSON("C:\\Users\\semaz\\OneDrive\\Documents\\XML project WS\\xml_terminalMode_WS\\jsonText.json.txt");
//		c1.compressJSON("C:\\Users\\semaz\\Downloads\\jsonText.json.txt");
		if(check)
		{
			
		}
		else {
			System.out.println("F");
		}
		c1.printHuffmanCodes();
//		Iterator<String> iterator = ((Compression) c1).getBufferedList().iterator();
//		while (iterator.hasNext()) {
//		    System.out.println(iterator.next());
//		}
		
//		cc = c1.getTokenCounts();
//		System.out.println(cc.size());
//		// Print all keys and values 
//		for (Map.Entry<String, Integer> entry : cc.entrySet()) 
//		{ 
//			System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//		}
	}

}
