package com.aurora500;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyFileWriter {
    //Write 1d double array into csv
    public void writeCSV(String filePath, double[] array) throws IOException {

        List<String[]> stringArr = new ArrayList<String[]>();
        for(int i=0; i<array.length; i++){
            String[] temp = {array[i]+""};
            stringArr.add(temp);
        }

        File file = new File(filePath);
        FileWriter outputFile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputFile);
        writer.writeAll(stringArr);
    }

    // Write 2D array into image
    public void writeCSV2D(String filePath, double[][] array) throws IOException {
        File file = new File(filePath);
        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile);

        String[] data = new String[array[0].length];
        for(int i=0; i<array.length; i++){
            for (int j=0; j<array[i].length;j++){
                data[j] = array[i][j]+"";
            }
            writer.writeNext(data);
        }
    }
}
