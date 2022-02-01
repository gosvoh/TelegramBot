package command;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import au.com.bytecode.opencsv.*;

public class CSVParser {
    private HashMap<String,String> dataMap = new HashMap<String,String>();

    public CSVParser(){}

    public HashMap<String,String> getDataMap(){
        return this.dataMap;
    }

    public void parseCSVIntoMap(String filePath) throws IOException {
        File csvData = new File(filePath);
        CSVReader reader = new CSVReader(new FileReader(csvData),',','\0','\0');
        String[] nextLine;
        String[] tmp;

        while((nextLine = reader.readNext()) != null){
            if (nextLine != null){
                tmp=Arrays.toString(nextLine).split(",");
                tmp[0].trim().toLowerCase();
                tmp[1].trim().toLowerCase();
                this.dataMap.put(tmp[1].replace(']', '\0').trim(),tmp[0].replace('[', '\0').trim());
            }
        }
    }
}
