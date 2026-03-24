package com.clue.entities;

import java.util.List;

public class GameEntity extends BaseEntity{
    private PlayerEntity playerEntity;
    private String fullStory;
    private String brief;
    private List<SuspectEntity> suspects;
    private List<ItemEntity> items;
    private int Status; //0 - started; 1 - finished

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public void setPlayerEntity(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getFullStory() {
        return fullStory;
    }

    public void setFullStory(String fullStory) {
        this.fullStory = fullStory;
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
