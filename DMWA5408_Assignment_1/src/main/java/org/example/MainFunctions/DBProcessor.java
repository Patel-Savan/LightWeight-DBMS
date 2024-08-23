package org.example.MainFunctions;

import org.example.Parser.CreateQuery;
import org.example.Parser.InsertQuery;
import org.example.Parser.SelectQuery;
import org.example.SQLStatements.Create;
import org.example.SQLStatements.Insert;
import org.example.SQLStatements.Select;
import org.example.utils.Constants;
import org.example.fileHandler.DirectoryHandler;

import java.util.Scanner;

/**
 * entry point for database operation
 */
public class DBProcessor {

    private static String database;

    /**
     * Setter method for database
     * @param db_name database name entered by user
     */
    public static void setDatabase(String db_name){
        database = db_name;
    }

    /**
     * getter method for database
     * @return name of database
     */
    public static String getDatabase(){
        return database;
    }

    /**
     * Database Operation starts here
     * Checks If database exist and takes SQL query for creating database from user If it doesn't exist
     * Takes SQL queries from user and performs operations accordingly
     */
    public static void run(){

        Scanner sc = new Scanner(System.in);

        String db_name = DirectoryHandler.chechkIfDatabaseExists(Constants.DB_path);



        if(db_name == null){
            while(true) {
                try {
                    System.out.println("Enter Query for creating a Database Schema ");
                    String query = sc.nextLine();
                    db_name = createDatabaseParse(query);
                    DirectoryHandler.createDirectory(Constants.DB_path + db_name);
                    DirectoryHandler.createDirectory(Constants.DB_path + db_name + "/" + Constants.DB_meta);
                    DirectoryHandler.createDirectory(Constants.DB_path + db_name + "/" + Constants.DB_data);
                    System.out.println("Successfully Created Database");
                    break;
                }catch(Exception e){
                    System.out.println("Database could not be created due to :" + e.getMessage() );
                }
            }
        }

        setDatabase(db_name);


        System.out.println("Enter your queries ending with ;");

        while(true) {

            System.out.print( db_name + " >");
            String query = "";
            query += sc.nextLine();

            query = query.toLowerCase().trim();

            if(query.equals("exit")){
                Authenticator.setCurrentUser("");
                break;
            }


            else if(query.equals("start transaction"))
                TransactionProcessor.begin();

            else if(query.startsWith("create") || query.startsWith("insert") || query.startsWith("select")){
                while (true) {
                    if (query.contains(";"))
                        break;

                    String input = sc.nextLine();
                    query += input + " ";

                }
                String[] finalQuery = query.trim().split(";");

                Execute(finalQuery[0]);

            }else{
                System.out.println("Invalid Input !");
            }
        }
    }

    public static void Execute(String query){
        query = query.toLowerCase();

        try{
            if(query.startsWith("create")){
                Create parsedQuery = CreateQuery.parse(query);
                parsedQuery.ExecuteQuery(query);
            }
            else if(query.startsWith("insert")){
                Insert parsedQuery = InsertQuery.parse(query);
                parsedQuery.ExecuteQuery(query);
            }else{
                Select parsedQuery = SelectQuery.parse(query);
                parsedQuery.ExecuteQuery(query);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    public static String createDatabaseParse(String query){
        String[] tokens = query.trim().split("\\s+");
        if(!tokens[0].equalsIgnoreCase("create") || !tokens[1].equalsIgnoreCase("schema") || tokens.length != 3)
            throw new RuntimeException("Invalid Query");

        return tokens[2];
    }
}
