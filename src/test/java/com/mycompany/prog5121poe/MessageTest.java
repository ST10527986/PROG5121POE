
package com.mycompany.prog5121poe;

import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    // Creates a sample message for testing.
    private Message createTestMessage() {
        return new Message(
                "0012345678",
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );
    }

    // Tests that a message with fewer than 250 characters is valid.
    @Test
    public void testMessageLengthValid() {
        Message message = createTestMessage();

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength()
        );
    }

    // Tests that a message exceeding 250 characters is rejected.
    @Test
    public void testMessageLengthInvalid() {
        String longMessage = "A".repeat(255);

        Message message = new Message(
                "0012345678",
                0,
                "+27718693002",
                longMessage
        );

        assertEquals(
                "Message exceeds 250 characters by 5; please reduce the size.",
                message.checkMessageLength()
        );
    }

    // Tests a correctly formatted recipient number.
    @Test
    public void testRecipientValid() {
        Message message = createTestMessage();

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    // Tests an incorrectly formatted recipient number.
    @Test
    public void testRecipientInvalid() {
        Message message = new Message(
                "0012345678",
                0,
                "08575975889",
                "Hi Keegan, did you receive the payment?"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct "
                + "the number and try again.",
                message.checkRecipientCell()
        );
    }

    // Tests that the message ID contains exactly ten digits.
    @Test
    public void testMessageID() {
        Message message = createTestMessage();

        assertTrue(message.checkMessageID());
    }

    // Tests that an invalid message ID is rejected.
    @Test
    public void testMessageIDInvalid() {
        Message message = new Message(
                "123456789012",
                0,
                "+27718693002",
                "Hello"
        );

        assertFalse(message.checkMessageID());
    }

    // Tests the hash generated from the message ID,
    // message number, and first and last words.
    @Test
    public void testMessageHash() {
        Message message = createTestMessage();

        assertEquals(
                "00:0:HITONIGHT",
                message.createMessageHash()
        );
    }

    // Tests the Send Message option.
    @Test
    public void testSendMessage() {
        Message message = createTestMessage();

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );
    }

    // Tests the Disregard Message option.
    @Test
    public void testDisregardMessage() {
        Message message = createTestMessage();

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(2)
        );
    }

    // Tests the Store Message option.
    @Test
    public void testStoreMessage() {
        Message message = createTestMessage();

        assertEquals(
                "Message successfully stored.",
                message.SentMessage(3)
        );
    }
}
