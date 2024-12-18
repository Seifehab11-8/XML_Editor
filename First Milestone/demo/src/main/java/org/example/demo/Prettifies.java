package org.example.demo;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
public class Prettifies {

    static String indent(int num){
        String ind = "";
        for(int i=0;i<num;i++){
            ind += "\t";
        }
        return ind;
    }
    public  String Pretty(String FilePath) {
        Stack<String> stack = new Stack<>();
        File file = new File(FilePath);
        String out = "";
        int depth = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.matches("^\\s*<(\\p{Alnum}+)>$")){
                    String key = line.replaceAll("(<?>?)", "");
                    stack.push(key);
                    out+=indent(depth)+line+"\n";
                    depth++;

                }
                else if(line.matches("^\\s*</(\\p{Alnum}+)>$")){
                    String key = line.replaceAll("(<?>?)", "");
                    depth--;
                    if(stack.peek()==key){
                        stack.pop();
                    }else{
                    }
                    out+=indent(depth)+line+"\n";

                }
                else if(line.matches("^\\s*<(\\p{Alnum}+)>\\p{ASCII}*<(/\\1)>")){
                    String indent = indent(depth);
                    String indentplus = indent+"\t";
                    line = line.replaceAll("<(\\p{Alnum}+)>(\\p{ASCII}*)</\\1>","<$1>\n"+indentplus+"$2\n"+indent+"<$1>");
                    out+=indent+line+"\n";
                }
            }
            return out;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
        return "File is Not Valid";
    }
}
