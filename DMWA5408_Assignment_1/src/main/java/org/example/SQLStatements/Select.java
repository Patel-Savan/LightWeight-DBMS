package org.example.SQLStatements;

import org.example.MainFunctions.DBProcessor;
import org.example.MainFunctions.WriteLog;
import org.example.fileHandler.DataIOHandler;
import org.example.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class for executing parsed select query
 */
public class Select{

    String table;
    List<String> targetColumns = new ArrayList<>();
    Map<String,String> conditions = new HashMap<>();

    /**
     * constructor for Select statement class
     * @param table table name from where data is to be shown
     * @param targetColumns column names whose value is required
     * @param conditions conditions which should be matched to view data
     */
    public Select(String table,List<String> targetColumns, Map<String,String> conditions){
        this.table = table;
        this.targetColumns = targetColumns;
        this.conditions = conditions;
    }

    /**
     * performs operations to fetch data from table file
     * @param query user input select query
     */
    public void ExecuteQuery(String query) {
        String path = Constants.DB_path + DBProcessor.getDatabase() + "/" + Constants.DB_data + table + Constants.Extension;
        List<List<String>> dataValues = new ArrayList<>();

        File file = new File(path);

        if(conditions.isEmpty()){
            try{
                dataValues = DataIOHandler.getData(file,targetColumns);
            }catch (Exception e){
                throw new RuntimeException("Cannot Read data from the table");
            }
        }
        else{
            try{
                dataValues = DataIOHandler.getDataOnCondition(file,targetColumns,conditions);
            }catch(Exception e){
                throw new RuntimeException("Cannot Read data from the table");
            }
        }

        WriteLog.WriteLogForSelect(query,targetColumns);
        for(String column : targetColumns)
            System.out.print(column + "\t\t");

        System.out.println();
        for(List<String> data : dataValues){
            for(String value : data)
                System.out.print(value + "\t\t");
            System.out.println();
        }
    }
}
