/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CSVIO;

import java.io.FileWriter;
import java.util.Arrays;

public class CSVWriter {
    String COMMA_DELIMITER = ",";
    String NEW_LINE_SEPARATOR = "\n";
    //String FILE_HEADER="1,2,3,4,5";
    
    public void tulisBobot2D(String lokasi, double[][] bobot) throws Exception {        
        FileWriter fileWriter = new FileWriter(lokasi);
        for (int i = 0; i < bobot.length; i++) {                    
            for (int h = 0; h < bobot[0].length; h++) {
                fileWriter.append(String.valueOf(bobot[i][h]));
                fileWriter.append(COMMA_DELIMITER);
            }            
            fileWriter.append(NEW_LINE_SEPARATOR);
        }
        fileWriter.flush();
        fileWriter.close();
    }

    public void tulisBobot1D(String lokasi, double[] bobot) throws Exception {        
        FileWriter fileWriter = new FileWriter(lokasi);
        for (int i = 0; i < bobot.length; i++) {                    
                fileWriter.append(String.valueOf(bobot[i]));
                fileWriter.append(COMMA_DELIMITER);
        }
        fileWriter.flush();
        fileWriter.close();
    }
}
