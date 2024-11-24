package xml_terminalMode_WS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compression {
	private List<String> bufferList;
	
	Compression(){
		bufferList = new ArrayList<>();
	} // default constructor
	
	boolean processFile(String path) //stores all the data inside the XML file inside a queue
	{
		String buff;
		// putting the reader inside try(readFile) ensures the reader is closed upon exiting the try block
		try (BufferedReader b_reader = new BufferedReader(new FileReader(path))) {
			buff = b_reader.readLine();
			
			while(buff != null)
			{
				bufferList.add(buff);
				buff = b_reader.readLine();
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	
	private String removeSpace(String next) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while(i < next.length())
		{
			if(next.charAt(i) == ' ')
				i++;
			else
				break;
		}
		while(i < next.length())
		{
			sb.append(next.charAt(i));
			i++;
		}
		return sb.toString();
	}
	
	private String xmlTokenizer(String next)
	{
		StringBuilder sb = new StringBuilder();
		int i = 0;
		int duplicateCounter = 1;
		while(i < next.length())
		{
			if(next.charAt(i) == '<' && next.charAt(i + 1) == '/')
			{
				sb.append('<');
				sb.append(next.charAt(i+1));
				sb.append(next.charAt(i+2));
				i = i + 2;
				while(next.charAt(i) != '>') i++;
				
				if(next.charAt(i - 1) == 's')
				{
					sb.append(next.charAt(i-1));
				}
				sb.append('>');
			}
			else if(next.charAt(i) == '<')
			{
				sb.append('<');
				sb.append(next.charAt(i+1));
				i = i + 1;
				while(next.charAt(i) != '>') i++;
				
				if(next.charAt(i - 1) == 's')
				{
					sb.append(next.charAt(i-1));
				}
				sb.append('>');
			}
			else
			{
				while(i < next.length() - 1)
				{
					if(next.charAt(i) == next.charAt(i+1))
						duplicateCounter++;
					else
					{
						sb.append(next.charAt(i));
						if(duplicateCounter != 1)
							sb.append(duplicateCounter);
						duplicateCounter = 1;
					}
						
					if(next.charAt(i+1) == '<')break;
					i++;
				}
				if(i == next.length() - 1)
					sb.append(next.charAt(i));	
			}
			
			i++;
		}
		return sb.toString();
	}
	
	boolean createCompressedFile(String type)
	{
		String filePath = String.format("./output%s.comp", type);
 
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { 
			for (String line : bufferList)	
			{ 
				writer.write(line); 
//				writer.newLine(); 
				// Write a new line to separate lines in the file 
			}
		}catch (IOException e) { 
			return false;
		}
		
		bufferList.removeAll(bufferList);
		return true;
	}

	boolean compressXML(String xmlPath)
	{
		int counter = 0;
		if(processFile(xmlPath))
		{
			for(String str : bufferList)
			{
				bufferList.set(counter, xmlTokenizer(removeSpace(str)));
				counter++;
			}
			
			return createCompressedFile("XML");
		}
		else
			return false;
		
	}
	
	boolean compressJSON(String jsonPath)
	{
		int counter = 0;
		if(processFile(jsonPath))
		{
			for(String str : bufferList)
			{
				bufferList.set(counter, jsonTokenizer(removeSpace(str)));
				counter++;
			}
			return createCompressedFile("JSON");
		}
		else
			return false;
	}
	
	private String jsonTokenizer(String next) {
		StringBuilder sb = new StringBuilder();
		int r = 0, tokenFlag = 0;
		int duplicateCounter = 1;
		
		while(r < next.length()) 
		{
			if(next.charAt(r) == ':')
			{
				tokenFlag = 1;
			}
			r++;
		}
		r = 0;
		while(r < next.length()) {
			if(tokenFlag == 0){
				
				while(r < next.length() - 1)
				{
					if(next.charAt(r) == next.charAt(r+1))
						duplicateCounter++;
					else
					{
						sb.append(next.charAt(r));
						if(duplicateCounter != 1)
							sb.append(duplicateCounter);
						duplicateCounter = 1;
					}
	
					r++;
				}
				if(r == next.length() - 1)
					sb.append(next.charAt(r));	
			}
			else {
				sb.append('"');
				sb.append(next.charAt(r+1));
				r++;
				while(next.charAt(r) != ':') r++;
				
				if(next.charAt(r - 2) == 's')
				{
					sb.append(next.charAt(r-2));
				}
				sb.append('"');
			}
			tokenFlag = 0;
			r++;
		}
		
		return sb.toString();
	}

	List<String> getBufferedList()
	{
		return bufferList;
		
	}
}
