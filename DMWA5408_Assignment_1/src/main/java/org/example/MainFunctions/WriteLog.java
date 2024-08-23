package org.example.MainFunctions;

import org.example.utils.Constants;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * contains method for writing logs in log file for each query
 */
public class WriteLog {

    /**
     * creates a string for insert query to be inserted into log file
     * @param query insert query successfully executed
     * @param columns columns whose value was inserted
     * @param data actual data inserted into database
     */
    public static void writeLogForInsert(String query,List<String> columns, List<String> data) {

        String user = Authenticator.getCurrentUser();
        String columnLog = "( ";
        for (String column : columns)
            columnLog += column + " ";
        columnLog += ")";

        String inLog = "( ";
        for (String d : data)
            inLog += d + " ";

        inLog += ")";

        String datalog = columnLog + "\n" + inLog;
        String date = String.valueOf(new Date());
        String finalLog = user + Constants.Delimeter + datalog + Constants.Delimeter + query + Constants.Delimeter + date;
        WriteInLog(finalLog);
    }


    /**
     * creates a string for select query to be inserted into log file
     * @param query select query that is successfully executed
     * @param columns columns which were shown to user
     */
    public static void WriteLogForSelect(String query,List<String> columns){
        String user = Authenticator.getCurrentUser();

        String dataLog = "( ";

        for(String column : columns)
            dataLog += column + " ";

        dataLog += ")";
        String date = String.valueOf(new Date());
        String finalLog = user + Constants.Delimeter + dataLog + Constants.Delimeter + query + Constants.Delimeter + date;

        WriteInLog(finalLog);
    }

    /**
     * creates a string for create query to be inserted into log file
     * @param query create query that was successfully executed
     * @param status status showing if table was created
     */
    public static void WriteLogForCreate(String query, String status){

        String date = String.valueOf(new Date());
        String finalLog = Authenticator.getCurrentUser() + Constants.Delimeter + status + Constants.Delimeter + query + Constants.Delimeter + date;

        WriteInLog(finalLog);
    }

    /**
     * writes string created according to query into log file as log data
     * @param finalLog log string which is to be written into log file
     */
    public static void WriteInLog(String finalLog){
        String path = Constants.DB_path +  Constants.Log_file + Constants.Extension;

        try{
            File file = new File(path);
            FileWriter target = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(target);

            bw.write(finalLog);
            bw.newLine();

            bw.close();
        }catch(Exception e){
            throw new RuntimeException("Could not enter data in Log file");
        }
    }
}
