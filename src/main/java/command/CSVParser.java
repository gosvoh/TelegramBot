package command;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import au.com.bytecode.opencsv.*;

public class CSVParser {
    private HashMap<String,String> capitalsOfTheWorldRus = new HashMap<String,String>();

    public CSVParser(){}

    public HashMap<String,String> getCapitalsOfTheWorldRus(){
        return this.capitalsOfTheWorldRus;
    }

    public void parseCSVIntoMap(String filePath) throws IOException {
        File csvData = new File(filePath);
        CSVReader reader = new CSVReader(new FileReader(csvData),',',' ','2');
        String[] nextLine;
        String[] tmp;

        while((nextLine = reader.readNext()) != null){
            if (nextLine != null){
                tmp=Arrays.toString(nextLine).split(",");
                capitalsOfTheWorldRus.put(tmp[0],tmp[1]);
            }
        }
        System.out.println(capitalsOfTheWorldRus.toString());
    }
}
