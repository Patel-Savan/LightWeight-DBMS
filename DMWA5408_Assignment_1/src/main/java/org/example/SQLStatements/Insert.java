package org.example.SQLStatements;

import org.example.MainFunctions.DBProcessor;
import org.example.MainFunctions.WriteLog;
import org.example.fileHandler.DataIOHandler;
import org.example.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * class for executing parsed insert query
 */
public class Insert {
    String table;
    List<String> data;
    List<String> columns;


    /**
     * constructor for Insert statement class
     * @param table table in which data is to be inserted
     * @param data data to be inserted
     * @param columns columns who data values is to be inserted
     */
    public Insert(String table,List<String> data, List<String> columns){
        this.table = table;
        this.data = data;
        this.columns = columns;
    }

    /**
     * performs operation for entering data into table file
     * @param query user input insert query
     */
    public void ExecuteQuery(String query) {
        String dataPath = Constants.DB_path + DBProcessor.getDatabase() + "/" + Constants.DB_data + table + Constants.Extension;
        String metaPath = Constants.DB_path + DBProcessor.getDatabase() + "/" + Constants.DB_meta + table + Constants.Extension;
        File datafile = new File(dataPath);
        File metafile = new File(metaPath);

        List<String> originalColumns = DataIOHandler.getAllTableColumns(metafile);
        List<String> correctValues = new ArrayList<>();

        for(String column : originalColumns){
            int i = columns.indexOf(column);
            if(i == -1)
                correctValues.add("\t"+Constants.Delimeter);
            else{
                String value = data.get(i);
                correctValues.add(value + Constants.Delimeter);
            }
        }

        try {
            DataIOHandler.WriteInFile(datafile,correctValues,true);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        WriteLog.writeLogForInsert(query,columns,data);
        System.out.println("Successfully inserted data into table " + table);
    }
}
