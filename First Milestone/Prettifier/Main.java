import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class Main  {
    public static void main(String args[]){
        prettify pretty = new prettify();
        if(!args[0].equals("prettify")){
            System.out.println("Only prettify is supported!");
            return;
        }
        System.out.println("Requested prettifying!"+args[0]);
        File file = null; //input file
        File fileout = null; //output file
        //ensure arguments order
        // prettify -i in -o out
        if(args[1].equals("-i") && args[3].equals("-o")){
            try{
                pretty.prettifyFile(args[2],args[4]);
            }
            catch(IOException e){
                System.out.println("prettifier err!");
                return;
            }
            
        }
        // prettify -o out -i in 
        else if(args[3].equals("-i") && args[1].equals("-o")){
            try{
                pretty.prettifyFile(args[4],args[2]);
            }
            catch(IOException e){
                System.out.println("prettifier err!");
                return;
            }
        }
        else{
            System.out.println("parameters not met!");
            return;
        }
    }
}