import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

// Main class
public class HelloWorld {
    static String indent(int num){
        String ind = "";
        for(int i=0;i<num;i++){
            ind += "\t";
        }
        return ind;
    }
    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        File file = new File("sample.txt");
        String out = "";
        int depth = 0;
        try (Scanner scanner = new Scanner(file)) {
            // Read the file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //System.out.println(line[1:line.length()-1]);
                //stack.push(line);
                //line = line.replaceAll("ro+t", "toor");
                if(line.matches("^<(\\p{Alnum}+)>$")){
                    String key = line.replaceAll("(<?>?)", "");
                    //System.out.println("start : "+ key);
                    stack.push(key);
                    out+=indent(depth)+line+"\n";
                    System.out.println(indent(depth)+line);
                    depth++;

                }
                else if(line.matches("^</(\\p{Alnum}+)>$")){
                    String key = line.replaceAll("(<?>?)", "");
                    //System.out.println("end : "+key);
                    depth--;
                    if(stack.peek()==key){
                        //System.out.println("indeed they are equal");
                        stack.pop();
                    }else{
                        //System.out.println("Not equal");
                    }
                    out+=indent(depth)+line+"\n";
                    System.out.println(indent(depth)+line);

                }
                else if(line.matches("<(\\p{Alnum}+)>\\p{ASCII}*<(/\\1)>")){
                    String indent = indent(depth);
                    String indentplus = indent+"\t";//indent(depth)+"\t";
                    line = line.replaceAll("<(\\p{Alnum}+)>(\\p{ASCII}*)</\\1>","<$1>\n"+indentplus+"$2\n"+indent+"<$1>");
                    out+=indent+line+"\n";
                }
            }
            System.out.println("-----------------");
            System.out.print(out);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}
