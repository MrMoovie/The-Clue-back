package com.clue.responses;

import com.clue.entities.GameEntity;
import com.clue.entities.ItemEntity;
import com.clue.entities.SuspectEntity;

import java.util.List;

public class GameResponse extends BasicResponse{
    private String brief;
    private List<SuspectEntity> suspects;
    private List<ItemEntity> items;

    public GameResponse(boolean success, Integer errorCode, GameEntity game){
        super(success, errorCode);
        this.brief = game.getBrief();
        this.items = game.getItems();
        this.suspects = game.getSuspects();
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public List<SuspectEntity> getSuspects() {
        return suspects;
    }

    public void setSuspects(List<SuspectEntity> suspects) {
        this.suspects = suspects;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}
