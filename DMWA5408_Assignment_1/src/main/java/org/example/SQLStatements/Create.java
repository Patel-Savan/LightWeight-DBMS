package org.example.SQLStatements;

import org.example.MainFunctions.DBProcessor;
import org.example.MainFunctions.WriteLog;
import org.example.fileHandler.DataIOHandler;
import org.example.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * class for executing parsed create query
 */
public class Create {
    String table;
    List<String> metadata;

    /**
     * Constructor for Create Statement class
     * @param table table name to be created
     * @param metadata metadata for the created table
     */
    public Create(String table,List<String> metadata){
        this.table = table;
        this.metadata = metadata;
    }

    /**
     * Creates a file for data and metadata of created table
     * @param query create query to be executed
     */

    public void ExecuteQuery(String query) {
        String path = Constants.DB_path + DBProcessor.getDatabase() + "/";

        String fileName = table + Constants.Extension;
        List<String> columnNames  = new ArrayList<>();

        for(String data : metadata){
            String[] columns = data.trim().split(Constants.Delimeter);
            String column = columns[0];
            columnNames.add(column + Constants.Delimeter);
        }

        File meta = new File(path + "metadata/" + fileName);
        File data = new File(path + "data/" + fileName);

        try{
            meta.createNewFile();
            data.createNewFile();

            DataIOHandler.WriteMetaDataInFile(meta,metadata);
            DataIOHandler.WriteInFile(data,columnNames,true);

        }catch(Exception e){
            throw new RuntimeException("Table could not be created due to following exception : " + e.getMessage());
        }

        WriteLog.WriteLogForCreate(query,"SUCCESS");
        System.out.println("Successfully created table " + table);
    }
}
