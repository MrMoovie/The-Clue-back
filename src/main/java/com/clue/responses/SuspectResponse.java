package com.clue.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SuspectResponse {

    private int id;
    private String name;
    private JsonNode chatHistory;
    private static final ObjectMapper mapper = new ObjectMapper();

    public SuspectResponse(int id, String name, String chatHistoryStr) {
        this.id = id;
        this.name = name;

        try {
            if (chatHistoryStr == null || chatHistoryStr.trim().isEmpty()) {
                this.chatHistory = mapper.createArrayNode();
                return;
            }

            String jsonToParse = chatHistoryStr.trim();
            if (!jsonToParse.startsWith("[")) {
                jsonToParse = "[" + jsonToParse + "]";
            }

            JsonNode rawHistory = mapper.readTree(jsonToParse);
            ArrayNode cleanHistory = mapper.createArrayNode();
            if (rawHistory.isArray()) {
                for (JsonNode node : rawHistory) {
                    String role = node.path("role").asText("bot");
                    String text = "";
                    JsonNode parts = node.path("parts");
                    if (parts.isArray() && parts.size() > 0) {
                        text = parts.get(0).path("text").asText("");
                    }
                    ObjectNode cleanMsg = mapper.createObjectNode();
                    cleanMsg.put("sender", role);
                    cleanMsg.put("text", text);

                    cleanHistory.add(cleanMsg);
                }
            }

            this.chatHistory = cleanHistory;

        } catch (Exception e) {
            e.printStackTrace();
            this.chatHistory = mapper.createArrayNode();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
