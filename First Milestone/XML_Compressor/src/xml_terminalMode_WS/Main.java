package xml_terminalMode_WS;

import java.util.Iterator;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean check;
		Compression c1 = new Compression();
		
		check = c1.compressXML("C:\\Users\\semaz\\OneDrive\\Documents\\XML project WS\\xml_terminalMode_WS\\text.xml.txt");
		if(check)
		{
			
		}
		else {
			System.out.println("F");
		}
		Iterator<String> iterator = ((Compression) c1).getBufferedList().iterator();
		while (iterator.hasNext()) {
		    System.out.println(iterator.next());
		}
	}

}
