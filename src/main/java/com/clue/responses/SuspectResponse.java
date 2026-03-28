package com.clue.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SuspectResponse {
    private String name;
    private JsonNode chatHistory;

    public SuspectResponse(String name, String chatHistoryStr){
        this.name = name;

        ObjectMapper mapper = new ObjectMapper();
        try {
            String correctJSON = "[" + (chatHistoryStr != null ? chatHistoryStr : "") + "]";

            this.chatHistory = mapper.readTree(correctJSON);

        } catch (Exception e) {
            e.printStackTrace();
            this.chatHistory = mapper.createArrayNode();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(JsonNode chatHistory) {
        this.chatHistory = chatHistory;
    }
}
