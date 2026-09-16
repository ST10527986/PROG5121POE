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
}