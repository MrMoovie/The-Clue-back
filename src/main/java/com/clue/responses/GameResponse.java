package com.clue.responses;

import com.clue.entities.GameEntity;
import com.clue.entities.ItemEntity;
import com.clue.entities.SuspectEntity;

import java.util.ArrayList;
import java.util.List;

public class GameResponse extends BasicResponse{
    private String brief;

    private List<ItemEntity> items;

    private List<SuspectResponse> suspects = new ArrayList<>();

    public GameResponse(boolean success, Integer errorCode, GameEntity game){
        super(success, errorCode);
        for(SuspectEntity sus : game.getSuspects()){
            this.suspects.add(new SuspectResponse(sus.getId(), sus.getName(), sus.getChatHistory()));
        }


        this.brief = game.getBrief();
        this.items = game.getItems();
    }

    public List<SuspectResponse> getSuspects() {
        return suspects;
    }

    public void setSuspects(List<SuspectResponse> suspects) {
        this.suspects = suspects;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}
