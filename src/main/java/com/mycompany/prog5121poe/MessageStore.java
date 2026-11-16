package com.mycompany.prog5121poe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Reads and writes the stored_messages.json file without external libraries. */
public final class MessageStore {
    // Private constructor to prevent creating instances of this utility class
    private MessageStore() { }

    // Default file location for saved messages
    public static final Path DEFAULT_PATH = Path.of("stored_messages.json");

    // Converts an array of messages to JSON format and writes it to a file
    public static void save(Path path, Message[] messages) throws IOException {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            if (i > 0) json.append(",\n");
            json.append("  {\"messageID\":\"").append(escape(message.getMessageID()))
               .append("\",\"messageNumber\":").append(message.getMessageNumber())
               .append(",\"messageHash\":\"").append(escape(message.getMessageHash()))
               .append("\",\"recipient\":\"").append(escape(message.getRecipient()))
               .append("\",\"message\":\"").append(escape(message.getMessageText()))
               .append("\",\"status\":\"Stored\"}");
        }
        json.append("\n]\n");
        Files.writeString(path, json, StandardCharsets.UTF_8);
    }

    // Loads existing messages, adds a new one to the end, and saves the file
    public static void storeMessage(Message message) throws IOException {
        Message[] existing = load(DEFAULT_PATH);
        Message[] updated = java.util.Arrays.copyOf(existing, existing.length + 1);
        updated[existing.length] = message;
        save(DEFAULT_PATH, updated);
    }

    // Reads the JSON file and converts it back into an array of Message objects
    public static Message[] load(Path path) throws IOException {
        if (!Files.exists(path)) return new Message[0];
        String json = Files.readString(path, StandardCharsets.UTF_8);
        return new JsonParser(json).parse();
    }

    // Escapes special characters (like quotes and newlines) so the JSON stays valid
    private static String escape(String value) {
        StringBuilder result = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"': result.append("\\\""); break;
                case '\\': result.append("\\\\"); break;
                case '\n': result.append("\\n"); break;
                case '\r': result.append("\\r"); break;
                case '\t': result.append("\\t"); break;
                default:
                    if (c < 32) result.append(String.format("\\u%04x", (int) c));
                    else result.append(c);
            }
        }
        return result.toString();
    }

    // Custom helper class to read JSON text without external libraries
    private static final class JsonParser {
        private final String text;
        private int position;

        JsonParser(String text) { this.text = text; }

        // Skips spaces, tabs, and line breaks
        private void whitespace() {
            while (position < text.length() && Character.isWhitespace(text.charAt(position))) position++;
        }

        // Checks if the next character matches what is expected and advances past it
        private boolean take(char expected) {
            whitespace();
            if (position < text.length() && text.charAt(position) == expected) { position++; return true; }
            return false;
        }

        // Enforces that a specific character must appear next, or throws an error
        private void expect(char expected) throws IOException {
            if (!take(expected)) throw new IOException("Invalid JSON near character " + position + "; expected '" + expected + "'.");
        }

        // Reads a quoted JSON string and decodes escaped characters
        private String readString() throws IOException {
            expect('"');
            StringBuilder result = new StringBuilder();
            while (position < text.length()) {
                char c = text.charAt(position++);
                if (c == '"') return result.toString();
                if (c != '\\') { result.append(c); continue; }
                if (position >= text.length()) throw new IOException("Incomplete JSON escape.");
                char escaped = text.charAt(position++);
                switch (escaped) {
                    case '"': result.append('"'); break;
                    case '\\': result.append('\\'); break;
                    case '/': result.append('/'); break;
                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    case 'b': result.append('\b'); break;
                    case 'f': result.append('\f'); break;
                    case 'u':
                        if (position + 4 > text.length()) throw new IOException("Incomplete Unicode escape.");
                        try { result.append((char) Integer.parseInt(text.substring(position, position + 4), 16)); }
                        catch (NumberFormatException ex) { throw new IOException("Invalid Unicode escape.", ex); }
                        position += 4;
                        break;
                    default: throw new IOException("Invalid JSON escape: \\" + escaped);
                }
            }
            throw new IOException("Unclosed JSON string.");
        }

        // Reads unquoted numbers from the JSON
        private String readPrimitive() throws IOException {
            whitespace();
            int start = position;
            while (position < text.length() && (Character.isDigit(text.charAt(position)) || text.charAt(position) == '-')) position++;
            if (start == position) throw new IOException("Invalid JSON value near character " + position);
            return text.substring(start, position);
        }

        // Reads either a string or a number value
        private String readValue() throws IOException {
            whitespace();
            return position < text.length() && text.charAt(position) == '"' ? readString() : readPrimitive();
        }

        // Parses the full JSON array into an array of Message objects
        Message[] parse() throws IOException {
            List<Message> messages = new ArrayList<>();
            expect('[');
            if (take(']')) return new Message[0];
            do {
                // Read all key-value pairs inside a single JSON object
                expect('{');
                Map<String, String> fields = new HashMap<>();
                do {
                    String key = readString();
                    expect(':');
                    fields.put(key, readValue());
                } while (take(','));
                expect('}');

                // Extract required message fields
                String id = fields.get("messageID");
                String recipient = fields.get("recipient");
                String body = fields.get("message");
                if (id == null || recipient == null || body == null) throw new IOException("Stored message is missing a required field.");

                // Parse the message number if present
                int number = 0;
                try { if (fields.containsKey("messageNumber")) number = Integer.parseInt(fields.get("messageNumber")); }
                catch (NumberFormatException ex) { throw new IOException("Invalid message number.", ex); }

                Message message = new Message(id, number, recipient, body);
                message.markAsStored();
                messages.add(message);
            } while (take(','));
            expect(']');

            // Ensure there is no leftover text after the closing bracket
            whitespace();
            if (position != text.length()) throw new IOException("Unexpected content after JSON array.");
            return messages.toArray(new Message[0]);
        }
    }
}