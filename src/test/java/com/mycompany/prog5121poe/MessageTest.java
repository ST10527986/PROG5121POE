
package com.mycompany.prog5121poe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Before
    public void setUp() {
        Message.resetSentMessagesForTesting();
    }

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

    // Tests that recently sent messages are displayed.
    @Test
    public void testRecentlySentMessages() {
        Message message = createTestMessage();

        message.SentMessage(1);

        String result = Message.printMessages();

        assertTrue(result.contains(
                "Hi Mike, can you join us for dinner tonight?"
        ));
        assertTrue(result.contains(message.getMessageID()));
        assertTrue(result.contains(message.getMessageHash()));
    }

    // Tests that a message is actually disregarded.
    @Test
    public void testMessageActuallyDisregarded() {
        Message message = createTestMessage();

        message.SentMessage(2);

        assertEquals(
                "Message successfully disregarded.",
                message.disregardMessage()
        );

        assertEquals("Disregarded", message.getStatus());
        assertEquals(0, Message.returnTotalMessagess());

        assertTrue(Message.printDisregardedMessages().contains(
                "Hi Mike, can you join us for dinner tonight?"
        ));
    }

    // Tests that disregarded messages cannot be sent or stored.
    @Test
    public void testDisregardedMessageCannotBeSentOrStored() {
        Message message = createTestMessage();

        message.disregardMessage();

        assertEquals(
                "A disregarded message cannot be sent or stored.",
                message.SentMessage(1)
        );

        assertEquals(
                "A disregarded message cannot be sent or stored.",
                message.SentMessage(3)
        );

        assertEquals(0, Message.returnTotalMessagess());
    }

    // Tests message hashing when punctuation is present.
    @Test
    public void testMessageHashWithPunctuation() {
        Message message = new Message(
                "0012345678",
                0,
                "+27718693002",
                "Hello, how are you today!"
        );

        assertEquals(
                "00:0:HELLOTODAY",
                message.createMessageHash()
        );
    }

    // Tests message hashing with different text.
    @Test
    public void testMessageHashWithDifferentText() {
        Message message = new Message(
                "9912345678",
                2,
                "+27718693002",
                "Please bring the documents, tomorrow."
        );

        assertEquals(
                "99:2:PLEASETOMORROW",
                message.createMessageHash()
        );
    }
}
