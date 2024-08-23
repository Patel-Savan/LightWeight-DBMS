package org.example.MainFunctions;

import org.example.Parser.CreateQuery;
import org.example.Parser.InsertQuery;
import org.example.Parser.SelectQuery;
import org.example.SQLStatements.Create;
import org.example.SQLStatements.Insert;
import org.example.SQLStatements.Select;

import java.util.*;

/**
 * entry point for each transaction
 */
public class TransactionProcessor {

    static Scanner sc = new Scanner(System.in);
    public static List<String> queries = new ArrayList<>();

    /**
     * This function marks the beginning of transaction when user wants to start the transaction
     * takes query from user and stores it until rollback or commit is encountered
     */
    public static void begin(){

        while(true) {
            System.out.print(DBProcessor.getDatabase() + " >");
            String query = "";
            query += sc.nextLine();

            query = query.toLowerCase().trim();

            if (query.equals("rollback")) {
                queries.clear();
                break;
            }
            else if(query.startsWith("commit")){
                executeTransaction();
                break;
            }
            else if(query.startsWith("create")){
                while(true){
                    if(query.contains(";"))
                        break;

                    String input = sc.nextLine();
                    query += input;
                }
                String[] finalQuery = query.trim().split(";");

                try {
                    Create parsedQuery = CreateQuery.parse(query);
                    parsedQuery.ExecuteQuery(query);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            else if(query.startsWith("insert") || query.startsWith("select")){
                while(true){
                    if(query.contains(";"))
                        break;

                    String input = sc.nextLine();
                    query += input;
                }

                String[] finalQuery = query.trim().split(";");
                queries.add(finalQuery[0]);
            }
        }
    }

    /**
     * Executed when commit is encountered
     * First checks for syntax of all queries
     * Executes all queries from stored List if all the queries are syntactically correct
     */
    public static void executeTransaction(){

        for(String query : queries){
            try{
                if(query.startsWith("select"))
                {
                    if(!SelectQuery.checkForSQLSyntax(query))
                        throw new RuntimeException("Invalid syntax for Select Query");
                    if(!SelectQuery.checkIfTableExist(query))
                        throw new RuntimeException("Given Table does not exist");

                }
                else{
                    if(!InsertQuery.checkForSQLSyntax(query))
                        throw new RuntimeException("Invalid Syntax for Insert Query");
                    if(!InsertQuery.checkIfTableExist(query))
                        throw new RuntimeException("Given Table does not exist");
                }

            }catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println("Transaction failed");
                return;
            }
        }

        for(String query : queries){
            try{
                if(query.startsWith("select")){
                    Select parsedQuery = SelectQuery.parse(query);
                    parsedQuery.ExecuteQuery(query);
                }else{
                    Insert parsedQuery = InsertQuery.parse(query);
                    parsedQuery.ExecuteQuery(query);
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println("Transaction failed");
            }
        }
    }
}
