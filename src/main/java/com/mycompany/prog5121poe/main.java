package com.mycompany.prog5121poe;


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
            
 // Captures the username entered during login.
            System.out.print("Enter your username: ");
            String loginUsername = input.nextLine();

            // Captures the password entered during login.
            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            // Checks whether the entered login details match
            // the details used during registration.
            boolean loginStatus = user.loginUser(
                    loginUsername,
                    loginPassword
            );

            // Displays the appropriate login status message.
            System.out.println(user.returnLoginStatus(loginStatus));
            
            if (loginStatus) {
                runQuickChat(input);
            }
        }

        // Closes the Scanner when the program is finished.
        input.close();
    }
        private static void runQuickChat(Scanner input) {
        System.out.println("\nWelcome to QuickChat.");
        int limit = readNonNegativeInt(input, "How many messages would you like to enter? ");
        int entered = 0;
        int option = 0;
        while (option != 3) {
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit");
            option = readNonNegativeInt(input, "Choose an option: ");
            switch (option) {
                case 1:
                    if (entered >= limit) {
                        System.out.println("You have reached your message limit.");
                        break;
                    }
                    for (int number = entered; number < limit; number++) {
                        System.out.println("\n--- MESSAGE " + (number + 1) + " ---");
                        System.out.print("Recipient (+27...): ");
                        String recipient = input.nextLine();
                        System.out.print("Message: ");
                        String content = input.nextLine();
                        Message message = new Message(number, recipient, content);
                        System.out.println("Message ID generated: " + message.getMessageID());
                        System.out.println(message.checkRecipientCell());
                        System.out.println(message.checkMessageLength());
                        if (!message.checkRecipientCell().equals("Cell phone number successfully captured.")
                                || !message.checkMessageLength().equals("Message ready to send.")) {
                            System.out.println("Please correct the message details.");
                            number--;
                            continue;
                        }
                        int action = readNonNegativeInt(input,
                                "1) Send Message\n2) Disregard Message\n3) Store Message to send later\nChoose: ");
                        while (action < 1 || action > 3) {
                            action = readNonNegativeInt(input, "Choose 1, 2, or 3: ");
                        }
                        System.out.println(message.SentMessage(action));
                        if (action == 2) {
                            int confirmation = readNonNegativeInt(input, "Press 0 to delete the message: ");
                            while (confirmation != 0) confirmation = readNonNegativeInt(input, "Press 0: ");
                            System.out.println("Message disregarded.");
                        } else if (action == 1) {
                            System.out.println(message.getDetails());
                            System.out.println("Message sent");
                        }
                        entered = number + 1;
                    }
                    System.out.println("Total messages sent: " + Message.returnTotalMessagess());
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    System.out.println("Total messages sent: " + Message.returnTotalMessagess());
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Choose 1, 2 or 3.");
            }
        }
    }

    private static int readNonNegativeInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            try {
                int value = Integer.parseInt(text);
                if (value >= 0) return value;
            } catch (NumberFormatException ignored) { }
            System.out.println("Please enter a valid non-negative whole number.");
        }
    }
}