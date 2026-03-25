package com.clue.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SuspectEntity extends BaseEntity{
    private String name;
    private String backGround;
    private String pov;
    private String chatHistory;


    private static final ObjectMapper mapper = new ObjectMapper();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackGround() {
        return backGround;
    }

    public void setBackGround(String backGround) {
        this.backGround = backGround;
    }

    public String getPov() {
        return pov;
    }

    public void setPov(String pov) {
        this.pov = pov;
    }

    public String getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(String chatHistory) {
        this.chatHistory = chatHistory;
    }

    public void addChatHistory(String role, String message) {
        try {
            ObjectNode partNode = mapper.createObjectNode();
            partNode.put("text", message);

            ArrayNode partsArray = mapper.createArrayNode();
            partsArray.add(partNode);

            ObjectNode messageNode = mapper.createObjectNode();
            messageNode.put("role", role);
            messageNode.set("parts", partsArray);

            String safeJsonEntry = mapper.writeValueAsString(messageNode);

            if (this.chatHistory == null || this.chatHistory.trim().isEmpty()) {
                this.chatHistory = safeJsonEntry;
            } else {
                this.chatHistory += ",\n" + safeJsonEntry;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process chat history JSON", e);
        }
    }
}
