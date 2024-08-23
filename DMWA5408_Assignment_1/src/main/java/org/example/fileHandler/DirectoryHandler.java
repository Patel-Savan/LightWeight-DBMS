package org.example.fileHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * contains method for handling checking and creating of  database folder
 */
public class DirectoryHandler {

    /**
     * Checks If specified folder exist or not
     * @param path path where to check for folder
     * @return true if folder exist otherwise false
     */
    public static boolean CheckIfDirectoryExists(String path){
        File file = new File(path);
        return file.exists();
    }

    /**
     * created directory
     * @param path path where to create file or folder
     */
    public static void createDirectory(String path){
        File file = new File(path);

        try {
            if (path.contains(".txt"))
                file.createNewFile();
            else
                file.mkdir();
        }catch (Exception e) {
            throw new RuntimeException("Could not create Directory for Database");
        }
    }

    /**
     * checks if database exist or not
     * @param path path where to look for database
     * @return name of the database if exist otherwise null
     */
    public static String chechkIfDatabaseExists(String path){
        File file = new File(path);

        String[] files = file.list();

        if(files != null){
            for(String folder : files){
                File newFile = new File(file,folder);
                if(newFile.isDirectory())
                    return folder;
            }
        }
        return null;
    }
}
