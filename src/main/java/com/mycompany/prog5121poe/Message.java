/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121poe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/** Represents one QuickChat message and manages sent and stored messages.*/
public class Message {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final List<Message> SENT_MESSAGES = new ArrayList<>();
    private static final Path JSON_FILE = Paths.get("stored_messages.json");

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
                storeMessage();
                status = "Stored";
                return "Message successfully stored.";
            } catch (IOException e) {
                return "Unable to store message: " + e.getMessage();
            }
        }
        return "Invalid option.";
    }
    // Stores a JSON array without requiring a third-party JSON library.
    // JSON string escaping is implemented below for quotes, slashes and control characters.
    public void storeMessage() throws IOException {
        String entry = "  {\"messageID\":\"" + jsonEscape(messageID)
                + "\",\"messageHash\":\"" + jsonEscape(messageHash)
                + "\",\"recipient\":\"" + jsonEscape(recipient)
                + "\",\"message\":\"" + jsonEscape(messageText) + "\"}";
        String old = Files.exists(JSON_FILE)
                ? Files.readString(JSON_FILE, StandardCharsets.UTF_8).trim() : "[]";
        if (!old.startsWith("[") || !old.endsWith("]")) {
            throw new IOException("Stored JSON file has an invalid format.");
        }
        String contents = old.substring(1, old.length() - 1).trim();
        String updated = contents.isEmpty() ? "[\n" + entry + "\n]"
                : "[\n" + contents + ",\n" + entry + "\n]";
        Files.writeString(JSON_FILE, updated, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static String jsonEscape(String value) {
        StringBuilder out = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"': out.append("\\\""); break;
                case '\\': out.append("\\\\"); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                default:
                    if (c < 0x20) out.append(String.format("\\u%04x", (int)c));
                    else out.append(c);
            }
        }
        return out.toString();
    }

    public static String printMessages() {
        if (SENT_MESSAGES.isEmpty()) return "No messages have been sent.";
        StringBuilder result = new StringBuilder();
        for (Message m : SENT_MESSAGES) result.append(m.getDetails()).append("\n\n");
        return result.toString().trim();
    }

    public static int returnTotalMessagess() {
        return SENT_MESSAGES.size();
    }

    public static void resetSentMessagesForTesting() {
        SENT_MESSAGES.clear();
    }

    public String getDetails() {
        return "Message ID: " + messageID + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient + "\nMessage: " + messageText;
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
}


    