package com.clue.entities;

public class SuspectEntity extends BaseEntity{
    private String name;
    private String backGround;
    private String pov;
    private String chatHistory;

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

    public void addChatHistory(String chatHistory, String role) {
        this.chatHistory = chatHistory;
    }
}
