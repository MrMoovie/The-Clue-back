package com.clue.entities;

public class ItemEntity extends BaseEntity{
    private String name;
    private String context;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
