/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.minifying;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLMinifier {

    public static void minifyFile(String inputFilePath, String outputFilePath) throws FileNotFoundException, IOException {
        FileInputStream input_file = new FileInputStream(inputFilePath);
        List<Byte> byteList = new ArrayList<>();//dynamic array
        int data;
        while((data=input_file.read())!=-1)
        {
            if(data!=( 32) && data!=(10)&& data!=(13))//32 is asci for space ,10 and 13 is asci for new line
            {
               byteList.add((byte)data);
            }
        }
        input_file.close();
        FileOutputStream output_file=new FileOutputStream(outputFilePath);
        for(int i=0;i<byteList.size();i++){
            output_file.write(byteList.get(i));
        }
        output_file.flush();//to make sure stream is empty
        output_file.close();
    }
    
}
