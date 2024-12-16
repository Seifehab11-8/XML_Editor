package xml_terminalMode_WS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Compression {
	private List<String> bufferList;
	private HashMap<String, Character> KeysToTokens;
	private HashMap<String, Integer> tokenCounts; // for json file
	private char randomChar = 'A';
	private char jsonRandChar = '0';
	
	Compression(){
		bufferList = new ArrayList<>();
		KeysToTokens = new HashMap<>();
		tokenCounts = new HashMap<>();
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
			System.out.println("File not found");
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
		StringBuilder token = new StringBuilder();
		int i = 0;
		while(i < next.length())
		{
			if(next.charAt(i) == '<') {
				token.delete(0, token.length());//make sure the builder is empty
				while(i < next.length()) {
					token.append(next.charAt(i));
					if(next.charAt(i) == '>')break;
					i++;
				}
				
				if(!KeysToTokens.containsKey(token.toString())) {
					KeysToTokens.put(token.toString(), randomChar);
					sb.append('<');
					sb.append(randomChar);
					randomChar++;
				}
				else {
					sb.append('<');
					sb.append(KeysToTokens.get(token.toString()));
				}
			}
			else {
				sb.append(next.charAt(i));
			}
			i++;
		}
		return sb.toString();
	}
	
	boolean createCompressedFile(String type)
	{
		String filePath = String.format("./output%s.comp", type);
		String KeyPath = String.format("./KeyFile%s.comp", type);
		boolean typeCheck = ("XML").equals(type);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { 
			for (String line : bufferList)	
			{ 
				writer.write(line); 
				writer.newLine(); 
				// Write a new line to separate lines in the file 
			}
		}catch (IOException e) { 
			return false;
		}
		
		try (BufferedWriter keyWriter = new BufferedWriter(new FileWriter(KeyPath))) { 
			for (Entry<String, Character> entry : KeysToTokens.entrySet()) 
			{ 
				if(typeCheck) {
					keyWriter.write("<" + entry.getValue()+ entry.getKey()); 
				}
				else {
					keyWriter.write("'" + entry.getValue()+ entry.getKey()); 
				}
				keyWriter.newLine(); 
				System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
			}
		}catch (IOException e) { 
			return false;
		}
		
		bufferList.removeAll(bufferList);
		KeysToTokens.clear();
		randomChar = 'A';
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
	boolean compressJSON(String jsonPath) {
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
	    StringBuilder token = new StringBuilder();
	    int i = 0, j = 0;
	    boolean tokenDetected = false;
	    while (i < next.length()) {
	        if (next.charAt(i) == '"') {
	            token.setLength(0); // Clear the token builder
	            token.append(next.charAt(i));
	            j = i + 1;

	            // Gather the token until the closing quote
	            while (j < next.length() && next.charAt(j) != '"') {
	                token.append(next.charAt(j));
	                j++;
	            }

	            // Append the closing quote if found
	            if (j < next.length()) {
	                token.append(next.charAt(j));
	                j++;
	            }

	            // Check for a colon following the token
	            if (j < next.length() && next.charAt(j) == ':') {
	                tokenDetected = true;
	                j++;
	            }

	            // Handle token replacement
	            if (tokenDetected) {
	                if (!KeysToTokens.containsKey(token.toString())) {
	                    KeysToTokens.put(token.toString(), randomChar);
	                    sb.append('\'');
	                    sb.append(randomChar);
	                    randomChar++;
	                } else {
	                	sb.append('\'');
	                    sb.append(KeysToTokens.get(token.toString()));
	                }
	                i = j; // Move the main index to continue after the token
	            } else {
	                sb.append(token);
	                i = j; // Move the main index to continue after the token
	            }
	            tokenDetected = false;
	        } else {
	            sb.append(next.charAt(i));
	            i++;
	        }
	    }
	    return sb.toString();
	}



	boolean compressJSONBytePair(String jsonPath)
	{
		if(processFile(jsonPath))
		{
			for(String str : bufferList)
			{
				jsonBytePairEncoder(removeSpace(str));
			}
			
			for (Map.Entry<String, Integer> entry : tokenCounts.entrySet()) 
			{ 
				KeysToTokens.put(entry.getKey(), jsonRandChar);
				jsonRandChar++;
				
				if(jsonRandChar > 250)break;
			}
			
			replaceAllSubstrings(bufferList, KeysToTokens);
			return createCompressedFile("JSON");
		}
		else
			return false;
	}
	public void replaceAllSubstrings(List<String> bufferList2, Map<String, Character> replacements) {
	    for (int i = 0; i < bufferList2.size(); i++) {
	        String currentString = bufferList2.get(i);
	        for (Map.Entry<String, Character> entry : replacements.entrySet()) {
	        	//System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
	            currentString = currentString.replace(entry.getKey(), entry.getValue().toString());
	        }
	        bufferList2.set(i, currentString);
	    }
	}


	public void jsonBytePairEncoder(String next) {
		StringBuilder sb = new StringBuilder();
		int i = 0, value;
		while(i < next.length()) {
			sb.append(next.charAt(i));
			
			if(sb.length() == 2) {
				if(tokenCounts.containsKey(sb.toString())) {
					value = tokenCounts.get(sb.toString()) + 1;
					tokenCounts.put(sb.toString(), value);
				}
				else {
					tokenCounts.put(sb.toString(), 1);
				}
				sb.delete(0, sb.length());
			}
			i++;
		}
	}
	
	
	public HashMap<String, Integer> getTokenCounts() {
		return tokenCounts;
	}

	List<String> getBufferedList()
	{
		return bufferList;
		
	}
}
