package org.example.Parser;

import org.example.MainFunctions.DBProcessor;
import org.example.SQLStatements.Select;
import org.example.fileHandler.DataIOHandler;
import org.example.fileHandler.DirectoryHandler;
import org.example.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class for parsing select query
 */
public class SelectQuery {
    /**
     * breaks down select query into tokens and parses it to check for correctness
     * @param query select query to be checked
     * @return object of Select statement on successful parsing
     */
    public static Select parse(String query){

        if(!checkForSQLSyntax(query))
            throw new RuntimeException("Invalid Syntax for SQL Query");

        if(!checkIfTableExist(query))
            throw new RuntimeException("Table does not exist");

        String[] tokens = query.trim().split("\\s+");
        Map<String,String> conditions = new HashMap<>();

        List<String> target = new ArrayList<>();

        int i=0;
        int len = query.length();

        while(!tokens[i].equalsIgnoreCase("from")){
            i++;
        }

        String table = tokens[++i];
        String path = Constants.DB_path + DBProcessor.getDatabase() + "/" + Constants.DB_meta + table + Constants.Extension;

        File file = new File(path);

        String lower_query = query.toLowerCase();

        if(tokens[1].equalsIgnoreCase("*") && !lower_query.contains("where")){
            target = DataIOHandler.getAllTableColumns(file);
        }
        else if(!lower_query.contains("where")){
            String fetchColumn = lower_query.substring(lower_query.indexOf("t")+1,lower_query.indexOf("from"));
            String[] columns = fetchColumn.trim().split(",");

            for(String column : columns){
                target.add(column.trim());
            }
        }
        else if(!tokens[1].equalsIgnoreCase("*")){

            int start = lower_query.indexOf("where");

            String fetchColumn = lower_query.substring(lower_query.indexOf("t")+1,lower_query.indexOf("from"));
            String[] columns = fetchColumn.trim().split(",");

            for(String column : columns){
                target.add(column.trim());
            }

            for(int count=0;count<=5;count++){
                start++;
            }

            if(start>=len)
                throw new RuntimeException("Invalid Syntax for select Query");

            String condition = query.substring(start);

            String[] data = condition.trim().split(",");

            for(String d : data){
                String[] filters = d.trim().split("=");
                String value = filters[1];
                if(value.contains("\""))
                    value = filters[1].substring(1,filters[1].length()-1);
                conditions.put(filters[0],value);
            }
        }
        else{
            target = DataIOHandler.getAllTableColumns(file);

            int start = lower_query.indexOf("where");

            for(int count=0;count<=5;count++){
                start++;
            }

            if(start>=len)
                throw new RuntimeException("Invalid Syntax for select Query");

            String condition = query.substring(start);
            System.out.println(condition);
            String[] data = condition.trim().split(",");

            for(String d : data){
                String[] filters = d.trim().split("=");
                String value = filters[1];
                if(value.contains("\""))
                    value = filters[1].substring(1,filters[1].length()-1);
                conditions.put(filters[0],value);
            }
        }

        return new Select(table,target,conditions);
    }

    /**
     * checks if table that user is trying to view data from exists or not
     * @param query select query from user
     * @return true if table exist otherwise false
     */
    public static boolean checkIfTableExist(String query){

        String[] tokens = query.trim().split("\\s+");
        int i=0;

        while(!tokens[i].equalsIgnoreCase("from")){
            i++;
        }

        String table = tokens[++i];
        String path = Constants.DB_path + DBProcessor.getDatabase() + "/" + Constants.DB_meta + table + Constants.Extension;

        return DirectoryHandler.CheckIfDirectoryExists(path);
    }

    /**
     * checks if query is syntactically correct
     * @param query select query to be checked
     * @return true if query is correct otherwise false
     */
    public static boolean checkForSQLSyntax(String query){

        String select1 = "(?i)^SELECT\\s+.*\\s+FROM\\s+.*\\s+WHERE\\s+.*$";
        Pattern selectPattern1 = Pattern.compile(select1);
        Matcher matcher1 = selectPattern1.matcher(query);

        if(!matcher1.matches()){
            String select2 = "(?i)^SELECT\\s+.*\\s+FROM\\s+.*$";
            Pattern selectPattern2 = Pattern.compile(select2);
            Matcher matcher2 = selectPattern2.matcher(query);
            if(!matcher2.matches()){
                return false;
            }
        }
        return true;
    }
}
