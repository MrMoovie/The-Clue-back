package com.clue.entities.template;

import com.clue.entities.PlayerEntity;

public class ClientEntity extends PlayerEntity {
    private String address;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
