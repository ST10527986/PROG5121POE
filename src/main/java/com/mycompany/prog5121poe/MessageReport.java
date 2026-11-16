package com.mycompany.prog5121poe;

import java.util.Arrays;

/** Part 3 arrays and operations for QuickChat messages. */
public class MessageReport {
    // Arrays to store messages by category
    private Message[] sentMessages = new Message[0];
    private Message[] disregardedMessages = new Message[0];
    private Message[] storedMessages = new Message[0];
    
    // Arrays to track all message hashes and IDs
    private String[] messageHashes = new String[0];
    private String[] messageIDs = new String[0];

    // Adds a Message to the end of a Message array
    private static Message[] append(Message[] source, Message value) {
        Message[] result = Arrays.copyOf(source, source.length + 1);
        result[source.length] = value;
        return result;
    }

    // Adds a String to the end of a String array
    private static String[] append(String[] source, String value) {
        String[] result = Arrays.copyOf(source, source.length + 1);
        result[source.length] = value;
        return result;
    }

    // Saves a message's hash and ID into the tracking arrays
    private void track(Message message) {
        messageHashes = append(messageHashes, message.getMessageHash());
        messageIDs = append(messageIDs, message.getMessageID());
    }

    // Add a message to the sent array
    public void addSent(Message message) {
        sentMessages = append(sentMessages, message);
        track(message);
    }

    // Add a message to the disregarded array
    public void addDisregarded(Message message) {
        disregardedMessages = append(disregardedMessages, message);
        track(message);
    }

    // Add a message to the stored array
    public void addStored(Message message) {
        storedMessages = append(storedMessages, message);
        track(message);
    }

    // Getters that return safe copies of the arrays
    public Message[] getSentMessages() { return Arrays.copyOf(sentMessages, sentMessages.length); }
    public Message[] getDisregardedMessages() { return Arrays.copyOf(disregardedMessages, disregardedMessages.length); }
    public Message[] getStoredMessages() { return Arrays.copyOf(storedMessages, storedMessages.length); }
    public String[] getMessageHashes() { return Arrays.copyOf(messageHashes, messageHashes.length); }
    public String[] getMessageIDs() { return Arrays.copyOf(messageIDs, messageIDs.length); }

    // Loads stored messages from JSON and updates the tracking arrays
    public void loadStored(Message[] messages) {
        storedMessages = Arrays.copyOf(messages, messages.length);
        rebuildIndexes();
    }

    // Clears and refills the hash and ID arrays from all three message arrays
    private void rebuildIndexes() {
        messageHashes = new String[0];
        messageIDs = new String[0];
        for (Message message : sentMessages) track(message);
        for (Message message : disregardedMessages) track(message);
        for (Message message : storedMessages) track(message);
    }

    // Returns formatted sent messages
    public String recentSentMessages() { return format(sentMessages); }

    // Lists the sender and recipient for every stored message
    public String displayStoredSendersRecipients(String senderName) {
        if (storedMessages.length == 0) return "No stored messages.";
        StringBuilder output = new StringBuilder();
        for (Message message : storedMessages) {
            output.append("Sender: ").append(senderName)
                  .append(" | Recipient: ").append(message.getRecipient()).append('\n');
        }
        return output.toString();
    }

    // Finds and returns the stored message with the longest text
    public Message longestStoredMessage() {
        Message longest = null;
        for (Message message : storedMessages) {
            if (longest == null || message.getMessageText().length() > longest.getMessageText().length()) {
                longest = message;
            }
        }
        return longest;
    }

    // Searches all three message arrays for a matching message ID
    public Message searchByID(String id) {
        for (Message[] group : new Message[][] {sentMessages, disregardedMessages, storedMessages}) {
            for (Message message : group) {
                if (message.getMessageID().equals(id)) return message;
            }
        }
        return null;
    }

    // Searches stored messages for a specific recipient
    public String searchByRecipient(String recipient) {
        StringBuilder output = new StringBuilder();
        for (Message message : storedMessages) {
            if (message.getRecipient().equals(recipient)) {
                output.append(message.getDetails()).append("\n\n");
            }
        }
        return output.length() == 0 ? "No matching stored messages." : output.toString().trim();
    }

    // Deletes a stored message by its hash and updates the tracking arrays
    public boolean deleteByHash(String hash) {
        for (int i = 0; i < storedMessages.length; i++) {
            if (storedMessages[i].getMessageHash().equalsIgnoreCase(hash)) {
                // Create a smaller array without the deleted message
                Message[] remaining = new Message[storedMessages.length - 1];
                System.arraycopy(storedMessages, 0, remaining, 0, i);
                System.arraycopy(storedMessages, i + 1, remaining, i, storedMessages.length - i - 1);
                
                storedMessages = remaining;
                rebuildIndexes();
                return true;
            }
        }
        return false;
    }

    // Report getters for stored and sent messages
    public String fullStoredReport() { return format(storedMessages); }
    public String fullSentReport() { return format(sentMessages); }

    // Formats an array of messages into a readable string
    private String format(Message[] messages) {
        if (messages.length == 0) return "No messages to display.";
        StringBuilder output = new StringBuilder();
        for (Message message : messages) {
            output.append(message.getDetails()).append("\n--------------------\n");
        }
        return output.toString();
    }
}