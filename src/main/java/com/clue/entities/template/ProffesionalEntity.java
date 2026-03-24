package com.clue.entities.template;

import com.clue.entities.PlayerEntity;

public class ProffesionalEntity extends PlayerEntity {

    private int plan;
    private String areas;


    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }
}
