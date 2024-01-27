package superstore.UI;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Scan {

    // When called, it takes a string from user, and checks if this string exist in "option" array
    public static String getValidString (List<String> options) {
        Scanner scan = new Scanner(System.in);
        String userInput = scan.next();
        clear();
        String validInput = "";
        if (isNumber(userInput)) {
            int userNumber = convertToInt(userInput);
            validInput = getTargetOption(options, userNumber);
        }
        else {
            validInput = loopUntilValid(options, userInput);
        }
        return validInput.toLowerCase();
    }

    // Checks if user entered a number instead of option name
    private static boolean isNumber (String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Convert the string number into int
    private static int convertToInt (String input){
        try {
            int userNumber = Integer.parseInt(input);
            return userNumber;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Convert the string number into double
    private static double convertToDouble (String input){
        try {
            double userNumber = Double.parseDouble(input);
            return userNumber;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Loops until it gets the valid string from "options" array
    private static String getTargetOption (List<String> options, int userNumber) {
        String input = "";
        if (userNumber <= 0 || userNumber > options.size()) {
            input = loopUntilValid(options, input);
        }
        else {
            input = options.get(userNumber-1);
        }
        return input;
    }

    // Input validation
    private static String loopUntilValid (List<String> options, String userInput) {
        Scanner scan = new Scanner(System.in);
        while (!isValid(options, userInput)){
            Displayer.displayOptions(options);
            System.out.print("Enter a valid option: ");
            userInput = scan.next();
            clear();
            if (isNumber(userInput)) {
                int userNumber = convertToInt(userInput);
                userInput = getTargetOption(options, userNumber);
            }
        }
        return userInput;
    }

    // Checks if input exists in "options" array
    private static boolean isValid (List<String> options, String input) {
        boolean validity = false;
        for (String option : options) {
            if (input.equalsIgnoreCase(option) && !validity){
                validity = true;
            }
        }
        return validity;
    }

    // prompts valid number with minimal value
    public static double getValidNumber (double min) {
        Scanner scan = new Scanner(System.in);
        String input = scan.next();
        while (!isNumber(input)) {
            System.out.print("Enter a valid number: ");
            input = scan.next();
        }
        double number = convertToDouble(input);
        try {
            while (number < min) {
            System.out.println("Error!");
            System.out.println("Enter a valid number: ");
            number = scan.nextDouble();
            scan.nextLine();
        }
        }
        catch (InputMismatchException e) {
            System.out.println("You entered a string instead of number !");
        }
        return number;
    }

    // prompts valid number with minimal and maximum value
    public static double getValidNumber (double min, double max) {
        Scanner scan = new Scanner(System.in);
        String input = scan.next();
        while (!isNumber(input)) {
            System.out.print("Enter a valid number: ");
            input = scan.next();
        }
        double number = convertToDouble(input);
        try {
            while (number < min || number > max) {
            System.out.println("Error!");
            System.out.println("Enter a valid number: ");
            number = scan.nextDouble();
            scan.nextLine();
        }
        }
        catch (InputMismatchException e) {
            System.out.println("You entered a string instead of number !");
        }
        return number;
    }

    // Prompts user for regular string with no validation
    public static String getString () {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }


    public static void clear () {
        System.out.print("\033[H\033[2J");
        System.out.flush(); 
    }

    public static void enter () {
        Scanner scan = new Scanner(System.in);
        System.out.print("Press enter to continue...");
        scan.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush(); 
    }

}