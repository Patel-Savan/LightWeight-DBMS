package org.example;
import org.example.MainFunctions.DBProcessor;
import org.example.MainFunctions.Authenticator;

/**
 * entry point for program
 */
public class Main {
    /**
     *Runs two functions
     * First one is to authenticate user that is either signup or login
     * Second is to execute SQL queries
     * @param args
     */
    public static void main(String[] args) {
        Authenticator.authenticate();
        DBProcessor.run();
    }
}