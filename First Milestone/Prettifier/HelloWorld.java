import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
public class HelloWorld {
    static String indent(int num){
        String ind = "";
        for(int i=0;i<num;i++){
            ind += "\t";
        }
        return ind;
    }
    public static void main(String[] args) {
        if(!args[0].equals("prettify")){
            System.out.println("Only prettify is supported!");
            return;
        }
        System.out.println("Requested prettifying!"+args[0]);
        System.out.println(args[1]);
        File file = new File(args[1]);
        String out = "";
        int depth = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.matches("^\\s*<(\\p{Alnum}+)>$")){
                    String key = line.replaceAll("(<?>?)", "");
                    out+=indent(depth)+line+"\n";
                    depth++;

                }
                else if(line.matches("^\\s*</(\\p{Alnum}+)>$")){
                    String key = line.replaceAll("(<?>?)", "");
                    depth--;
                    out+=indent(depth)+line+"\n";

                }
                else if(line.matches("^\\s*<(\\p{Alnum}+)>\\p{ASCII}*<(/\\1)>")){
                    String indent = indent(depth);
                    String indentplus = indent+"\t";
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
