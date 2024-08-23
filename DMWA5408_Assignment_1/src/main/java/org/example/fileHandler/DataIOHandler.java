package org.example.fileHandler;

import org.example.utils.Constants;

import java.io.*;
import java.util.*;

/**
 * Contains method for handling read and write operation on data
 */
public class DataIOHandler {
    /**
     * Writes detail of newly registered user into USERS file
     * @param credential concated string of username,email and password
     * @param path path of USER file where information is to be stored
     */
    public static void writeNewUser(String credential,String path){
        File file = new File(path);

        try{
            FileWriter target = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(target);

            bw.write(credential);
            bw.newLine();

            bw.close();
        }catch (Exception e){
            throw new RuntimeException("Could not enter user data");
        }
    }

    /**
     * checks if user trying to log in is actually present in database
     * @param credential user input credential
     * @param path path of file where to check for credential
     * @return success if user has correct credential, false If not and User not exist if there is no such user present
     */
    public static String checkForCredential(String credential,String path){

        File file = new File(path);
        String[] inputCredential = credential.split(Constants.Delimeter);

        boolean isRegistered = false;

        try{
            FileReader target = new FileReader(file);
            BufferedReader br = new BufferedReader(target);

            while(true){
                String user = br.readLine();
                if(user == null)
                    break;

                String[] storedCredential = user.split(Constants.Delimeter);
                if(storedCredential[1].equals(inputCredential[1])){

                    isRegistered = true;
                    if(storedCredential[2].equals(inputCredential[2]))
                        return "Success";
                }
            }
        }catch (Exception e){
            throw new RuntimeException("Cannot read user data");
        }
        if(isRegistered)
            return "Fail";

        return "UserDoesNotExist";
    }

    /**
     * Writes metadata of table when table is newly created
     * @param file file where metadata is to be written
     * @param data List of string which has metadata of table created
     * @throws IOException if some exception occurs while writing metadata
     */
    public static void WriteMetaDataInFile(File file, List<String> data) throws IOException {

        try{

            FileWriter target = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(target);

            for(String d : data){
                bw.write(d);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }catch(Exception e) {
            throw new IOException("Meta data could not be entered");
        }
    }

    /**
     * writes data in file for the table
     * @param file object of table file
     * @param data data to be inserted into table file
     * @param append specifies if new data is to be appended or overwrite over old data
     * @throws IOException If some exception occurs while writing data in table file
     */
    public static void WriteInFile(File file, List<String> data,boolean append) throws IOException {

        try{

            FileWriter target = new FileWriter(file,append);
            BufferedWriter bw = new BufferedWriter(target);

            for(String d : data){
                bw.write(d);
            }
            bw.newLine();
            bw.flush();
            bw.close();
        }catch(Exception e) {
            throw new IOException("Data could not be entered");
        }
    }

    /**
     * returns all the columns for given table
     * @param file file object where to look for columns
     * @return List of string containing all table names
     */
    public static List<String> getAllTableColumns(File file) {

        List<String> Columns = new ArrayList<>();
        try{
            FileReader target = new FileReader(file);
            BufferedReader br = new BufferedReader(target);

            while(true){
                String data = br.readLine();
                if(data == null)
                    break;
                String[] token = data.split(Constants.Delimeter);
                Columns.add(token[0]);
            }
        }catch(Exception e){
            throw new RuntimeException("Cannot read Data");
        }
        return Columns;
    }

    /**
     * gets data from the table file
     * @param file file object of table where to look for data
     * @param targetColumns column names whose value is required
     * @return List of List of String where each string is a data and each List of String is a record
     */
    public static List<List<String>> getData(File file, List<String> targetColumns) {
        List<List<String>>  result = new ArrayList<>();
        String columns = "";

        try{
            FileReader target = new FileReader(file);
            BufferedReader br = new BufferedReader(target);

            columns +=  br.readLine();

            List<String> columnsOrder = Arrays.asList(columns.trim().split(Constants.Delimeter));
            List<Integer> indexes = new ArrayList<>();

            for(String column : targetColumns){
                int i = columnsOrder.indexOf(column);
                if(i != -1)
                    indexes.add(i);
            }

            while(true){
                String data = br.readLine();
                if(data == null)
                    break;

                List<String> values = new ArrayList<>();
                String[] dataValues = data.trim().split(Constants.Delimeter);

                for(int i : indexes){
                    values.add(dataValues[i]);
                }

                result.add(values);
            }
        }catch (Exception e){
            throw new RuntimeException("Cannot Read data from the table");
        }
        return result;
    }

    /**
     * gets table data which matches some specified condition
     * @param file file object where to look for data
     * @param targetColumns column names whose value is required
     * @param conditions condition which is required to show data
     * @return List of List of String where each String is a data and each List of String is a record
     */
    public static List<List<String>> getDataOnCondition(File file, List<String> targetColumns, Map<String, String> conditions) {
        List<List<String>> result = new ArrayList<>();
        String columns = "";
        Map<Integer,String> RequiredValues = new HashMap<>();

        try{
            FileReader target = new FileReader(file);
            BufferedReader br = new BufferedReader(target);

            columns +=  br.readLine();

            List<String> columnsOrder = Arrays.asList(columns.trim().split(Constants.Delimeter));
            List<Integer> indexes = new ArrayList<>();

            for (Map.Entry<String, String> entry : conditions.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                int index = columnsOrder.indexOf(key);

                RequiredValues.put(index,value);
            }


            for(String column : targetColumns){
                int i = columnsOrder.indexOf(column);
                if(i != -1)
                    indexes.add(i);
            }

            while(true){
                String data = br.readLine();
                if(data == null)
                    break;

                List<String> values = new ArrayList<>();
                String[] dataValues = data.trim().split(Constants.Delimeter);

                boolean flag = true;

                for(Map.Entry<Integer,String> entry : RequiredValues.entrySet()){
                    int index = entry.getKey();
                    String value = entry.getValue();

                    if(!dataValues[index].equalsIgnoreCase(value)){
                        flag = false;
                        break;
                    }
                }

                if(flag){
                    for(int i : indexes){
                        values.add(dataValues[i]);
                    }
                    result.add(values);
                }
            }
        }catch (Exception e){
            throw new RuntimeException("Cannot Read data from the table");
        }
        return result;
    }

}
