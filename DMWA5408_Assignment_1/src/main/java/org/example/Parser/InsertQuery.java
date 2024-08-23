package org.example.Parser;

import org.example.MainFunctions.DBProcessor;
import org.example.SQLStatements.Insert;
import org.example.fileHandler.DataIOHandler;
import org.example.fileHandler.DirectoryHandler;
import org.example.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class for parsing insert query
 */
public class InsertQuery {
    /**
     * breaks down query into tokens and parses it to check for correctness
     * @param query insert query to be checked
     * @return object of Insert statement on successful parsing
     */
    public static Insert parse(String query){

        String[] tokens = query.trim().split("\\s+");

        String table = tokens[2];

        if(table.contains("("))
            table = table.substring(0,table.indexOf("("));

        if(!checkForSQLSyntax(query))
            throw new RuntimeException("Invalid Syntax for SQL Query");

        if(!checkIfTableExist(query))
            throw new RuntimeException("Table " + table + "Does not Exist");

        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();

        String match = tokens[3].toLowerCase();
        String path = Constants.DB_path + DBProcessor.getDatabase() + "/" + Constants.DB_meta + table + Constants.Extension;

        if(!tokens[2].contains("(") && match.contains("values") ){          // Matches If Insert Query is like Insert into table_name values("value_1","value_2");

            columns = DataIOHandler.getAllTableColumns(new File(path));

            try{
                String substring = query.substring(query.indexOf("(")+1,query.lastIndexOf(")"));
                String[] data = substring.trim().split(",");
                values = new ArrayList<>();

                for(String d : data) {
                    String individualData;
                    if (d.contains("\""))
                        individualData = d.substring(d.indexOf("\"") + 1, d.lastIndexOf("\""));
                    else
                        individualData = d;

                    values.add(individualData);
                }
            }catch(Exception e){
                throw new RuntimeException("Invalid Syntax for SQL query");

            }
            return new Insert(table,values,columns);
        }

        String dataColumns = query.substring(query.indexOf("(")+1,query.indexOf(")"));
        String[] columnNames = dataColumns.trim().split(",");
        List<String> originalColumns = DataIOHandler.getAllTableColumns(new File(path));

        if(columnNames.length > originalColumns.size())
            throw new RuntimeException("Invalid Query for table "+ table);

        for(String column : columnNames){
            columns.add(column);
        }

        String dataValues = query.substring(query.lastIndexOf("(")+1,query.lastIndexOf(")"));

        String[] data = dataValues.trim().split(",");
        for(String d : data){
            String dv = d.substring(d.indexOf("\"")+1,d.lastIndexOf("\""));
            values.add(dv);
        }
        return new Insert(table,values,columns);

    }

    /**
     * checks if table that user is trying to insert data into exist
     * @param query insert query from user
     * @return true if table exist otherwise false
     */
    public static boolean checkIfTableExist(String query){

        String[] tokens = query.trim().split("\\s+");

        String table = tokens[2];

        if(table.contains("("))
            table = table.substring(0,table.indexOf("("));

        String path = Constants.DB_path + DBProcessor.getDatabase() + "/" + Constants.DB_meta + table + Constants.Extension;

        return DirectoryHandler.CheckIfDirectoryExists(path);
    }

    /**
     * checks if query is syntactically correct
     * @param query insert query to be checked
     * @return true if query is correct otherwise false
     */
    public static boolean checkForSQLSyntax(String query){

        String[] tokens = query.trim().split("\\s+");

        if(!tokens[1].equalsIgnoreCase("into"))
            return false;

        String match = tokens[3].toLowerCase();

        if(!tokens[2].contains("(") && match.contains("values") )
            return true;

        String pattern = "(?i)^Insert\\s+into\\s+(\\w+)\\s*\\(([^)]+)\\)\\s*values\\s*\\(([^)]+)\\)$";

        Pattern insertPattern = Pattern.compile(pattern);
        Matcher matcher = insertPattern.matcher(query);

        return matcher.matches();
    }
}
