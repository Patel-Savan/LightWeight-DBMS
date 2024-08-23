package org.example.MainFunctions;

import org.example.fileHandler.DataIOHandler;
import org.example.fileHandler.DirectoryHandler;
import org.example.utils.Constants;
import org.example.utils.Hash;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * contains method for authenticating user
 */
public class Authenticator {

    private static String currentUser;
    private static final Scanner sc = new Scanner(System.in);
    private static final String path = Constants.DB_path + Constants.User + Constants.Extension;

    /**
     * Setter method for currentUser
     * @param user user email that has been logged in
     */
    public static void setCurrentUser(String user){
        currentUser = user;
    }

    /**
     * getter method for currentUser
     * @return email of currently logged in user
     */
    public static String getCurrentUser(){
        return currentUser;
    }

    /**
     * Creates a folder for database if it doesn't exist
     * takes users input to either select login or signup and runs method accordingly
     */
    public static void authenticate() {

        boolean exists = DirectoryHandler.CheckIfDirectoryExists(Constants.DB_path);

        if(!exists){
            DirectoryHandler.createDirectory(Constants.DB_path);
        }

        boolean exist = DirectoryHandler.CheckIfDirectoryExists(path);

        if(!exist) {
            DirectoryHandler.createDirectory(path);
        }

        while(true){
            System.out.println("Enter 0 for signup and 1 for login");
            System.out.print("DBMS>");
            int choice = sc.nextInt();
            if(choice==0){
                boolean isRegistered = signup();
                if(isRegistered){
                    System.out.println("Registered Successfully");
                    System.out.println("Please login to continue");
                    choice = 1;
                }
                else
                    System.out.println("Failed to Register");
            }

            if(choice==1){
                boolean isLoggedIn = login();

                if(isLoggedIn){
                    System.out.println("Logged In Successfully");
                    break;
                }
                else
                    System.out.println("Failed to Login");
            }else{
                System.out.println("Invalid Input");
            }
        }
    }

    /**
     * Takes user,email and password from user for registration
     * verifies user by matching captcha and stores user Information in USERS file
     * @return true if user is successfully registered otherwise false
     */
    private static boolean signup(){
        String user;
        String email;
        String password;
        String systemCaptcha;
        String inputCaptcha;

        System.out.print("Enter your Username :");
        user = sc.next();
        System.out.print("Enter your Email :");
        email = sc.next();
        System.out.print("Enter your Password :");
        password = sc.next();
        systemCaptcha = generateCaptcha();
        System.out.println(systemCaptcha);
        System.out.print("Enter above captcha :");
        inputCaptcha = sc.next();

        if(!inputCaptcha.equals(systemCaptcha)){
            System.out.println("Wrong captcha !");
            return false;
        }

        password = Hash.hashPassword(password);

        if(password == null)
            return false;

        String credential = user + Constants.Delimeter + email + Constants.Delimeter + password;

        try{
            DataIOHandler.writeNewUser(credential,path);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    /**
     * takes name,email and password from user and checks validity by matching value from USERS file
     * verifies user by matching captcha
     * @return true if matching value is found(successful login) otherwise false
     */
    private static boolean login(){

        String user;
        String email;
        String password;
        String systemCaptcha;
        String inputCaptcha;

        String path_log = Constants.DB_path + Constants.Log_file + Constants.Extension;

        if(!DirectoryHandler.CheckIfDirectoryExists(path_log))
            DirectoryHandler.createDirectory(path_log);

        System.out.print("Enter your Username :");
        user = sc.next();
        System.out.print("Enter your Email :");
        email = sc.next();
        System.out.print("Enter your Password :");
        password = sc.next();
        systemCaptcha = generateCaptcha();
        System.out.println(systemCaptcha);
        System.out.print("Enter above captcha :");
        inputCaptcha = sc.next();

        if(!inputCaptcha.equals(systemCaptcha)){
            System.out.println("Wrong captcha !");
            return false;
        }

        if(password == null)
            return false;

        password = Hash.hashPassword(password);
        String credential = user + Constants.Delimeter + email + Constants.Delimeter +  password;

        String result = DataIOHandler.checkForCredential(credential,path);

        if(result.equalsIgnoreCase("fail")){
            System.out.println("Incorrect Password");
            return false;
        }

        else if(result.equalsIgnoreCase("userdoesnotexist")){
            System.out.println("User does not exist");
            return false;
        }

        setCurrentUser(email);
        return true;
    }

    /**
     * Generates captcha with help of Random function from Math library
     * Took reference for generating captcha from GeeksForGeeks
     * @return a random string as captcha
     */
    private static String generateCaptcha(){
        int n = 5;

        char[] alph = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

        String captcha = "";

        for(int i=0;i<n;i++){
            int index = (int)(Math.random()*25);
            captcha += alph[index];
        }
        return captcha;
    }
}
