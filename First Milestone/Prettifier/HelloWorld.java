// File: HelloWorld.java
/*Packages: If you are writing a more complex application, organize your files into packages. For example:

package com.example.project;

Save the file in a directory structure like com/example/project/.
// Package declaration (optional, only if needed)
*/
//package com.example;

// Import statements (if needed)
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

// Main class
public class HelloWorld {
    // Main method - the entry point of the program
    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        File file = new File("sample.txt");
        try (Scanner scanner = new Scanner(file)) {
            // Read the file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //System.out.println(line[1:line.length()-1]);
                //stack.push(line);
                //line = line.replaceAll("ro+t", "toor");
                if(line.matches("<(\\p{Alnum}+)>")){
                    System.out.println("start : "+ line.replaceAll("(<?>?)", ""));
                }
                else if(line.matches("</(\\p{Alnum}+)>")){
                    System.out.println("end : "+line.replaceAll("<?>?", ""));
                }
                else if(line.matches("<(\\p{Alnum}*)>\\p{ASCII}*<(/\\1)>")){
                    System.out.println("Double : " + line);
                }
                //System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
        System.out.println(stack.peek()); // This prints "Hello, World!" to the console
    }
}
