package com.clue.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

public class ChatHistoryResponse extends BasicResponse{
    private JsonNode chatHistory;


    public ChatHistoryResponse(boolean success, Integer errorCode, String chatHistoryStr) {
        super(success, errorCode);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String correctJSON = "[" + (chatHistoryStr != null ? chatHistoryStr : "") + "]";

            this.chatHistory = mapper.readTree(correctJSON);

        } catch (Exception e) {
            e.printStackTrace();
            this.chatHistory = mapper.createArrayNode();
        }
    }

    public JsonNode getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(JsonNode chatHistory) {
        this.chatHistory = chatHistory;
    }
}
