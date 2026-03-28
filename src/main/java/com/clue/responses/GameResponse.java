package com.clue.responses;

import com.clue.entities.GameEntity;
import com.clue.entities.ItemEntity;
import com.clue.entities.SuspectEntity;

import java.util.List;

public class GameResponse extends BasicResponse{
    private String brief;

    private List<ItemEntity> items;

    private List<SuspectResponse> suspectResponses;

    public GameResponse(boolean success, Integer errorCode, GameEntity game){
        super(success, errorCode);
        for(SuspectEntity sus : game.getSuspects()){
            suspectResponses.add(new SuspectResponse(sus.getName(), sus.getChatHistory()));
        }


        this.brief = game.getBrief();
        this.items = game.getItems();
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
