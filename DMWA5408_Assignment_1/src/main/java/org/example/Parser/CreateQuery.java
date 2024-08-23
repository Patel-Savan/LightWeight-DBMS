package org.example.Parser;

import org.example.MainFunctions.DBProcessor;
import org.example.SQLStatements.Create;
import org.example.fileHandler.DirectoryHandler;
import org.example.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * class for parsing create query
 */
public class CreateQuery {

    /**
     * breaks down create query into tokens and parses it to check for correctness
     * @param query create query to be checked
     * @return Object of Create statement on successful parsing
     */
    public static Create parse(String query){
        String[] tokens = query.trim().split("\\s+");

        if(tokens[1].equalsIgnoreCase("schema")){
            throw new RuntimeException("You can only have one database");
        }

        if(!tokens[1].equalsIgnoreCase("table")){
            throw new RuntimeException("Invalid Syntax for SQL Query");
        }

        String table;

        if(tokens[2].contains("("))
            table = tokens[2].substring(0,tokens[2].indexOf("("));

        else
            table = tokens[2];

        String path = Constants.DB_path + DBProcessor.getDatabase() + "/" + Constants.DB_data + table + Constants.Extension;

        boolean exist = DirectoryHandler.CheckIfDirectoryExists(path);

        if(exist)
            throw new RuntimeException("Table " + table + " already present in the database");

        String data = query.substring(query.indexOf("(")+1,query.lastIndexOf(")"));

        String[] columns = data.trim().split(",");

        List<String> metadata = new ArrayList<>();

        for(String column : columns){
            String[] meta = column.trim().split("\\s+");
            String columnData = String.join(Constants.Delimeter,meta);
            metadata.add(columnData);
        }

        return new Create(table,metadata);
    }
}
