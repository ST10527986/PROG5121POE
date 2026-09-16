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