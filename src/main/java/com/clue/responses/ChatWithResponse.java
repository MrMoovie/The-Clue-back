package com.clue.responses;

public class ChatWithResponse extends BasicResponse{
    private String response;

    public ChatWithResponse(boolean success, Integer errorCode, String response) {
        super(success, errorCode);
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
