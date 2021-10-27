package com.bermin.landlords.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    private int typeId;
    private int playerId;
    private String content;
    private List<Poker> pokers = new ArrayList<>();

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Poker> getPokers() {
        return pokers;
    }

    public void setPokers(List<Poker> pokers) {
        this.pokers = pokers;
    }

    public Message() {
    }

    public Message(int typeId, int playerId, String content, List<Poker> pokers) {
        this.typeId = typeId;
        this.playerId = playerId;
        this.content = content;
        this.pokers = pokers;
    }
}
