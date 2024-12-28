package DSAproject;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
public class prettify {
    static String indent(int num){
        String ind = "";
        for(int i=0;i<num;i++){
            ind += "\t";
        }
        return ind;
    }
    public void prettifyFile(String inputFilePath, String outputFilePath) throws IOException {
        
        File file = new File(inputFilePath);
        File fileout = new File(outputFilePath);
        String out = "";
        int depth = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //Start line-by-line checking
                //<key>
                if(line.matches("^\\s*<(\\p{Alnum}+)>$")){ 
                    String key = line.replaceAll("(<?>?)", "");
                    out+=indent(depth)+line+"\n";
                    depth++;

                }
                //</key>
                else if(line.matches("^\\s*</(\\p{Alnum}+)>$")){
                    String key = line.replaceAll("(<?>?)", "");
                    depth--;
                    out+=indent(depth)+line+"\n";

                }
                //<key>value<key>
                else if(line.matches("^\\s*<(\\p{Alnum}+)>\\p{ASCII}*<(/\\1)>")){
                    String indent = indent(depth);
                    String indentplus = indent+"\t";
                    line = line.replaceAll("<(\\p{Alnum}+)>(\\p{ASCII}*)</\\1>","<$1>\n"+indentplus+"$2\n"+indent+"<$1>");
                    out+=indent+line+"\n";
                }
                else if(line.matches("^\\s*[^<]\\p{ASCII}+")){
                    line.replace("^\\s*","");
                    out+=indent(depth)+"\t"+line+"\n";
                }
            }
            //print to output file
            try (PrintWriter writer = new PrintWriter(fileout)) {
                writer.write(out);
                System.out.println("Content successfully written to the file: " + fileout.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("An error occurred while writing to the file: " + e.getMessage());
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}
