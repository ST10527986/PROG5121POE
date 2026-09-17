package za.ac.iie.prog5121;

public class login {

    // Stores the user's registration details.
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Initialises the user's registration details when a Login object is created.
    public login(String username, String password, String cellPhoneNumber,
                 String firstName, String lastName) {

        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    // Checks if the username contains an underscore and is no more than 5 characters long.
public boolean checkUserName() {
    
    return username.contains("_") && username.length() <= 5;
}
// Checks that the password has at least 8 characters,
// a capital letter, a number, and a special character.
public boolean checkPasswordComplexity() {

    if (password.length() < 8) {
        return false;
    }

    boolean hasCapitalLetter = false;
    boolean hasNumber = false;
    boolean hasSpecialCharacter = false;

    for (int i = 0; i < password.length(); i++) {

        char character = password.charAt(i);

        if (Character.isUpperCase(character)) {
            hasCapitalLetter = true;
        }

        if (Character.isDigit(character)) {
            hasNumber = true;
        }

        if (!Character.isLetterOrDigit(character)) {
            hasSpecialCharacter = true;
        }
    }

    return hasCapitalLetter && hasNumber && hasSpecialCharacter;
}
// Checks whether the cellphone number is in the required
    // South African international format.
    public boolean checkCellPhoneNumber() {

        /*
         * Regular expression:
         * ^        = beginning of the number
         * \\+27    = South African international code (+27)
         * [0-9]    = allows numbers from 0 to 9
         * {9}      = requires exactly 9 digits after +27
         * $        = end of the number
         */
         // Source: Oracle, Java SE Documentation, Pattern Class.
        String phoneRegex = "^\\+27[0-9]{9}$";

        return cellPhoneNumber.matches(phoneRegex);
    }
    // Registers the user by checking all three required details.
    public String registerUser() {

        // Checks whether the username is valid.
        if (!checkUserName()) {

            return "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more "
                    + "than five characters in length.";

        // Checks whether the password is valid.
        } else if (!checkPasswordComplexity()) {

            return "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, a "
                    + "capital letter, a number, and a special character.";

        // Checks whether the cellphone number is valid.
        } else if (!checkCellPhoneNumber()) {

            return "Cell number is incorrectly formatted or does not contain "
                    + "an international code; please correct the number and "
                    + "try again.";

        // If all three details are valid, registration is successful.
        } else {

            return "User registered successfully.";
        }
        
    }
       // Compares the username and password entered during login
    // with the details stored during registration.
    public boolean loginUser(String enteredUsername, String enteredPassword) {

        return enteredUsername.equals(username)
                && enteredPassword.equals(password);
    }

    // Returns the appropriate message after the login attempt.
    public String returnLoginStatus(boolean loginStatus) {

        // Displays a welcome message if login was successful.
        if (loginStatus) {

            return "Welcome " + firstName + " " + lastName
                    + ", it is great to see you again.";

        // Displays an error message if login failed.
        } else {

            return "Username or password incorrect, please try again.";
        }
    }
}
    
    