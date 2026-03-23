package com.clue.entities;

public class SuspectEntity extends BaseEntity{
    private String name;
    private String backGround;
    private String pov;

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
}
