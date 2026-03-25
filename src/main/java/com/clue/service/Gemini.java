package com.clue.service;

import com.clue.entities.GameEntity;
import com.clue.entities.ItemEntity;
import com.clue.entities.SuspectEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Gemini {
    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    private static final String STORY_MODEL_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";
    private static final String CHAT_MODEL_URL =  "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    public Gemini() {
        this.apiKey = "AIzaSyA4M4-uP4QR44vSWmwcO7jQhLK_HhyJPao";
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    private String sendRequest(String modelUrl, String payload) throws Exception {
        String url = modelUrl + apiKey;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public GameEntity generateNewGame(String topic) throws Exception {
        String promptInstruction = "Create a detailed murder mystery game based on the topic: " + topic + ". " +
                "Return ONLY a valid JSON object. The JSON must strictly follow this structure: " +
                "{\"fullStory\": \"The complete, objective truth of the murder\", " +
                "\"brief\": \"A short introductory setup given to the detective\", " +
                "\"suspects\": [{\"name\": \"Suspect Name\", \"backGround\": \"Background and personality\", \"pov\": \"What they know, their secrets, and motives\"}], " +
                "\"items\": [{\"name\": \"Item Name\", \"description\": \"Brief description of the clue\"}]}+" +
                "\"Generate exactly 4 suspects. Exactly one of these suspects must be the killer. Generate exactly 3 clue items.";

        String payload = """
        {
          "contents": [{
            "parts": [{"text": "%s"}]
          }],
          "generationConfig": {
            "responseMimeType": "application/json"
          }
        }
        """.replace("%s", promptInstruction.replace("\"", "\\\""));

        String rawResponse = sendRequest(STORY_MODEL_URL, payload);
        return parseGameFromJson(rawResponse);
    }

    private GameEntity parseGameFromJson(String rawResponse) throws Exception {
        String innerJsonText = extractTextFromJson(rawResponse);

        JsonNode gameData = mapper.readTree(innerJsonText);

        GameEntity game = new GameEntity();
        game.setStatus(0);

        // Full story
        if (gameData.hasNonNull("fullStory")) {
            game.setFullStory(gameData.get("fullStory").asText());
        }

        // Brief
        if (gameData.hasNonNull("brief")) {
            game.setBrief(gameData.get("brief").asText());
        }


        // suspects
        List<SuspectEntity> suspectList = new ArrayList<>();
        if (gameData.hasNonNull("suspects") && gameData.get("suspects").isArray()) {
            for (JsonNode sNode : gameData.get("suspects")) {
                SuspectEntity suspect = new SuspectEntity();
                suspect.setName(sNode.path("name").asText(null));
                suspect.setBackGround(sNode.path("backGround").asText(null));
                suspect.setPov(sNode.path("pov").asText(null));
                suspectList.add(suspect);
            }
        }
        game.setSuspects(suspectList);

        // Items
        List<ItemEntity> itemList = new ArrayList<>();
        if (gameData.hasNonNull("items") && gameData.get("items").isArray()) {
            for (JsonNode iNode : gameData.get("items")) {
                ItemEntity item = new ItemEntity();
                item.setName(iNode.path("name").asText(null));
                item.setContext(iNode.path("description").asText(null));
                itemList.add(item);
            }
        }
        game.setItems(itemList);

        return game;
    }

    public String chatWith(String characterName, String background, String pov, String formattedHistoryJson) throws Exception {
        String systemInstruction = String.format(
                "You are a character in a murder mystery game. Never break character. Your name is: %s. Your background: %s. The truth you know (your POV): %s. Keep your answers concise, act defensively, and protect your own interests. Do not confess or volunteer information unless explicitly confronted with irrefutable evidence. If you are the killer, deflect blame.",
                characterName, background, pov
        );

        String historyWithComma = (formattedHistoryJson != null && !formattedHistoryJson.trim().isEmpty())
                ? formattedHistoryJson + ","
                : "";

        String payload = """
        {
          "systemInstruction": {
            "parts": [{"text": "%s"}]
          },
          "contents": [
            %s
          ]
        }
        """.formatted(systemInstruction, historyWithComma);

        String rawResponse = sendRequest(CHAT_MODEL_URL, payload);
        return extractTextFromJson(rawResponse);
    }

    private String extractTextFromJson(String jsonResponse) throws Exception {
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode candidates = rootNode.path("candidates");

        if (candidates.isArray() && !candidates.isEmpty()) {
            return candidates.get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        }
        throw new RuntimeException("Failed to extract response text. API returned: " + jsonResponse);
    }
}