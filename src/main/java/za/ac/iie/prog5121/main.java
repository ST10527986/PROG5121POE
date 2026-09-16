package za.ac.iie.prog5121;

import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        // Creates a Scanner object to receive input from the user.
        Scanner input = new Scanner(System.in);

        // Captures the user's first name.
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        // Captures the user's last name.
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        // Captures the username the user wants to register.
        System.out.print("Enter a username: ");
        String username = input.nextLine();

        // Captures the password the user wants to register.
        System.out.print("Enter a password: ");
        String password = input.nextLine();

        // Captures the user's South African cellphone number.
        System.out.print("Enter your South African cell phone number: ");
        String cellPhoneNumber = input.nextLine();
        
 // Creates a Login object using the information provided.
        login user = new login(
                username,
                password,
                cellPhoneNumber,
                firstName,
                lastName
        );

        // Checks whether the username is correctly formatted.
        if (user.checkUserName()) {

            System.out.println("Username successfully captured.");

        } else {

            System.out.println(
                    "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more "
                    + "than five characters in length."
            );
        }

        // Checks whether the password meets the complexity requirements.
        if (user.checkPasswordComplexity()) {

            System.out.println("Password successfully captured.");

        } else {

            System.out.println(
                    "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, a "
                    + "capital letter, a number, and a special character."
            );
        }

        // Checks whether the cellphone number is correctly formatted.
        if (user.checkCellPhoneNumber()) {

            System.out.println("Cell phone number successfully added.");

        } else {

            System.out.println(
                    "Cell phone number incorrectly formatted or does not "
                    + "contain international code."
            );
        }

        // Calls registerUser() to determine whether all registration
        // requirements have been successfully completed.
        String registrationMessage = user.registerUser();

        // Displays the registration result.
        System.out.println(registrationMessage);

        // Only allows the user to continue to login if all
        // registration requirements have been met.
        if (user.checkUserName()
                && user.checkPasswordComplexity()
                && user.checkCellPhoneNumber()) {

            System.out.println("\n--- LOGIN ---");