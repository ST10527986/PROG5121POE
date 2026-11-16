/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121poe;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/** Represents one QuickChat message and manages sent and stored messages.*/
public class Message {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final List<Message> SENT_MESSAGES = new ArrayList<>();
    private static final List<Message> DISREGARDED_MESSAGES = new ArrayList<>();
   

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String status = "Draft";

    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    // This constructor makes the generated hash deterministic in unit tests.
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    private static String generateMessageID() {
        StringBuilder result = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            result.append(RANDOM.nextInt(10));
        }
        return result.toString();
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.matches("[0-9]{10}");
    }

    // South African international format: +27 followed by nine digits.
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("\\+27[0-9]{9}")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText != null && !messageText.trim().isEmpty() && messageText.length() <= 250) {
            return "Message ready to send.";
        }
        if (messageText == null || messageText.trim().isEmpty()) {
            return "Please enter a message.";
        }
        return "Message exceeds 250 characters by " + (messageText.length() - 250)
                + "; please reduce the size.";
    }

    public String createMessageHash() {
        if (!checkMessageID() || messageText == null || messageText.trim().isEmpty()) {
            return "";
        }
        String[] words = messageText.trim().split("\\s+");
        String first = words[0].replaceAll("[^\\p{L}\\p{N}]", "");
        String last = words[words.length - 1].replaceAll("[^\\p{L}\\p{N}]", "");
        return (messageID.substring(0, 2) + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    public String SentMessage(int choice) {
        if ("Disregarded".equals(status)) {
    return "A disregarded message cannot be sent or stored.";
}
        if ("Stored".equals(status) && choice == 1) {
    return "A stored message cannot be sent.";
}

if ("Sent".equals(status) && choice == 3) {
    return "A sent message cannot be stored.";
}
        if (choice == 1) {
            if (!checkMessageID() || !checkMessageLength().equals("Message ready to send.")
                    || !checkRecipientCell().equals("Cell phone number successfully captured.")) {
                return "Message cannot be sent because its details are invalid.";
            }
            if (!"Sent".equals(status)) {
                status = "Sent";
                SENT_MESSAGES.add(this);
            }
            return "Message successfully sent.";
        }
        if (choice == 2) {
            return "Press 0 to delete the message.";
        }
        if (choice == 3) {
            if (!checkMessageID() || !checkMessageLength().equals("Message ready to send.")
                    || !checkRecipientCell().equals("Cell phone number successfully captured.")) {
                return "Message cannot be stored because its details are invalid.";
            }
            try {
    MessageStore.storeMessage(this);
    status = "Stored";
    return "Message successfully stored.";
} catch (IOException e) {
    return "Unable to store message: " + e.getMessage();
}
        }
        return "Invalid option.";
    }
    public String disregardMessage() {

    // Prevents a message that has already been sent or stored
    // from being disregarded.
    if ("Sent".equals(status) || "Stored".equals(status)) {
        return "Message cannot be disregarded because it has already been processed.";
    }
    

    // Updates the message status and records it as disregarded.
    if (!"Disregarded".equals(status)) {
        status = "Disregarded";
        DISREGARDED_MESSAGES.add(this);
    }

    return "Message successfully disregarded.";
}
    

    public static String printMessages() {
        if (SENT_MESSAGES.isEmpty()) return "No messages have been sent.";
        StringBuilder result = new StringBuilder();
        for (Message m : SENT_MESSAGES) result.append(m.getDetails()).append("\n\n");
        return result.toString().trim();
    }
    public static String printDisregardedMessages() {

    if (DISREGARDED_MESSAGES.isEmpty()) {
        return "No messages have been disregarded.";
    }

    StringBuilder result = new StringBuilder();

    for (Message message : DISREGARDED_MESSAGES) {
        result.append(message.getDetails()).append("\n\n");
    }

    return result.toString().trim();
}

    public static int returnTotalMessagess() {
        return SENT_MESSAGES.size();
    }

    public static void resetSentMessagesForTesting() {
    SENT_MESSAGES.clear();
    DISREGARDED_MESSAGES.clear();
}

    public String getDetails() {
        return "Message ID: " + messageID + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient + "\nMessage: " + messageText;
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
    
    public void markAsStored() {
    status = "Stored";
}
    
    public int getMessageNumber() {return messageNumber;}
    public String getRecipient() {return recipient;}
    public String getMessageText() {return messageText;}

}


    