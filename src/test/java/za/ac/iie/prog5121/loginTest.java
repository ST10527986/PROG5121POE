package za.ac.iie.prog5121;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class loginTest {

    // Tests whether a valid username is accepted.
    @Test
    public void testCheckUserNameValid() {

        login user = new login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.checkUserName());
    }

    // Tests whether an invalid username is rejected.
    @Test
    public void testCheckUserNameInvalid() {

        login user = new login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(user.checkUserName());
    }

    // Tests whether a valid password is accepted.
    @Test
    public void testCheckPasswordValid() {

        login user = new login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.checkPasswordComplexity());
    }

    // Tests whether an invalid password is rejected.
    @Test
    public void testCheckPasswordInvalid() {

        login user = new login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(user.checkPasswordComplexity());
    }

    // Tests whether a valid cellphone number is accepted.
    @Test
    public void testCheckCellPhoneValid() {

        login user = new login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.checkCellPhoneNumber());
    }

    // Tests whether an invalid cellphone number is rejected.
    @Test
    public void testCheckCellPhoneInvalid() {

        login user = new login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553",
                "Kyle",
                "Smith"
        );

        assertFalse(user.checkCellPhoneNumber());
    }

    // Tests whether login is successful with the correct details.
    @Test
    public void testLoginSuccessful() {

        login user = new login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    // Tests whether login fails with incorrect details.
    @Test
    public void testLoginFailed() {

        login user = new login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(user.loginUser("wrong_user", "wrong_password"));
    }

    // Tests the successful login status message.
    @Test
    public void testReturnLoginStatusSuccessful() {

        login user = new login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Welcome Kyle Smith, it is great to see you again.",
                user.returnLoginStatus(true)
        );
    }

    // Tests the unsuccessful login status message.
    @Test
    public void testReturnLoginStatusFailed() {

        login user = new login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Username or password incorrect, please try again.",
                user.returnLoginStatus(false)
        );
    }
}
