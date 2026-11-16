package com.mycompany.prog5121poe;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for MessageReport and MessageStore functionality. */
public class MessageReportTest {

    // Helper method to quickly create test Message objects
    private Message message(int number, String id, String recipient, String text) {
        return new Message(id, number, recipient, text);
    }

    // Verifies that added sent messages are stored in the sentMessages array
    @Test
    public void sentArrayContainsSentMessages() {
        MessageReport report = new MessageReport();
        report.addSent(message(0, "0011111111", "+27834557896", "Did you get the cake?"));
        report.addSent(message(3, "0044444444", "0838884567", "It is dinner time !"));
        assertEquals(2, report.getSentMessages().length);
        assertEquals("Did you get the cake?", report.getSentMessages()[0].getMessageText());
        assertEquals("It is dinner time !", report.getSentMessages()[1].getMessageText());
    }

    // Verifies that disregarded messages stay separate from sent messages
    @Test
    public void disregardedMessagesAreKeptSeparate() {
        MessageReport report = new MessageReport();
        report.addSent(message(0, "0011111111", "+27834557896", "Sent message"));
        report.addDisregarded(message(2, "0033333333", "+27834484567", "Discarded message"));
        assertEquals(1, report.getSentMessages().length);
        assertEquals(1, report.getDisregardedMessages().length);
        assertFalse(report.recentSentMessages().contains("Discarded message"));
    }

    // Verifies that longestStoredMessage finds the stored message with the most characters
    @Test
    public void longestStoredMessageIsReturned() {
        MessageReport report = new MessageReport();
        report.addStored(message(1, "0022222222", "+27838884567", "Where are you? You are late! I have asked you to be on time."));
        report.addStored(message(4, "0055555555", "+27838884567", "Ok, I am leaving without you."));
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                report.longestStoredMessage().getMessageText());
    }

    // Verifies searching by message ID returns the matching recipient and text
    @Test
    public void searchByIDReturnsRecipientAndMessage() {
        MessageReport report = new MessageReport();
        Message target = message(3, "0044444444", "0838884567", "It is dinner time !");
        report.addSent(target);
        Message found = report.searchByID("0044444444");
        assertNotNull(found);
        assertEquals("0838884567", found.getRecipient());
        assertEquals("It is dinner time !", found.getMessageText());
    }

    // Verifies searching by recipient returns only matching stored messages (ignoring sent ones)
    @Test
    public void recipientSearchReturnsAllStoredMessagesForRecipient() {
        MessageReport report = new MessageReport();
        report.addStored(message(1, "0022222222", "+27838884567", "Where are you? You are late!"));
        report.addStored(message(4, "0055555555", "+27838884567", "Ok, I am leaving without you."));
        report.addSent(message(0, "0011111111", "+27838884567", "This sent message must not appear."));
        String results = report.searchByRecipient("+27838884567");
        assertTrue(results.contains("Where are you?"));
        assertTrue(results.contains("Ok, I am leaving"));
        assertFalse(results.contains("This sent message must not appear."));
    }

    // Verifies that a stored message can be deleted using its message hash
    @Test
    public void deleteStoredMessageByHash() {
        MessageReport report = new MessageReport();
        Message stored = message(1, "0022222222", "+27838884567", "Where are you?");
        report.addStored(stored);
        assertTrue(report.deleteByHash(stored.getMessageHash()));
        assertEquals(0, report.getStoredMessages().length);
        assertFalse(report.deleteByHash(stored.getMessageHash()));
    }

    // Verifies that the sender and recipient summary includes expected names and numbers
    @Test
    public void storedSenderAndRecipientAreDisplayed() {
        MessageReport report = new MessageReport();
        report.addStored(message(1, "0022222222", "+27838884567", "Stored message"));
        String result = report.displayStoredSendersRecipients("Test Sender");
        assertTrue(result.contains("Test Sender"));
        assertTrue(result.contains("+27838884567"));
    }

    // Verifies that the full stored report includes ID, hash, recipient, and message text
    @Test
    public void fullStoredReportContainsMessageDetails() {
        MessageReport report = new MessageReport();
        Message stored = message(1, "0022222222", "+27838884567", "Stored report message");
        report.addStored(stored);
        String result = report.fullStoredReport();
        assertTrue(result.contains(stored.getMessageID()));
        assertTrue(result.contains(stored.getMessageHash()));
        assertTrue(result.contains(stored.getRecipient()));
        assertTrue(result.contains(stored.getMessageText()));
    }

    // Verifies that messages saved to a JSON file load properly into MessageReport
    @Test
    public void storedArrayCanBeLoadedFromJson() throws Exception {
        Path file = Files.createTempFile("quickchat-test", ".json");
        try {
            Message stored = message(1, "0022222222", "+27838884567", "Stored from JSON");
            MessageStore.save(file, new Message[] {stored});
            MessageReport report = new MessageReport();
            report.loadStored(MessageStore.load(file));
            assertEquals(1, report.getStoredMessages().length);
            assertEquals("0022222222", report.getMessageIDs()[0]);
        } finally {
            Files.deleteIfExists(file); // Clean up temp file after test
        }
    }

    // Verifies that special characters (quotes, newlines, backslashes) survive JSON saving and loading
    @Test
    public void jsonRoundTripPreservesQuotesAndNewLines() throws Exception {
        Path file = Files.createTempFile("quickchat-test", ".json");
        try {
            String body = "Hi, \"friend\"!\nNext line\\test";
            MessageStore.save(file, new Message[] {message(1, "0022222222", "+27838884567", body)});
            Message[] loaded = MessageStore.load(file);
            assertEquals(1, loaded.length);
            assertEquals(body, loaded[0].getMessageText());
        } finally {
            Files.deleteIfExists(file); // Clean up temp file after test
        }
    }

    // Verifies that deleting a message and saving updates the JSON file correctly
    @Test
    public void deletionIsSavedToJson() throws Exception {
        Path file = Files.createTempFile("quickchat-delete", ".json");
        try {
            Message stored = message(1, "0022222222", "+27838884567", "Delete me");
            MessageStore.save(file, new Message[] {stored});
            MessageReport report = new MessageReport();
            report.loadStored(MessageStore.load(file));
            assertTrue(report.deleteByHash(stored.getMessageHash()));
            MessageStore.save(file, report.getStoredMessages());
            assertEquals(0, MessageStore.load(file).length);
        } finally {
            Files.deleteIfExists(file); // Clean up temp file after test
        }
    }
}