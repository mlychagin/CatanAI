package com.SpringField.engine.board;

import static com.SpringField.engine.util.Util.STATUS_EMPTY;
import static com.SpringField.engine.util.Util.UNASSIGNED_PLAYER;

public class Vertex {
    private byte playerId;
    private byte building;

    public Vertex() {
        playerId = UNASSIGNED_PLAYER;
        building = STATUS_EMPTY;
    }

    public void setPlayerId(byte playerNumber) {
        this.playerId = playerNumber;
    }

    public void setBuilding(byte building) {
        this.building = building;
    }

    public byte getPlayerId() {
        return playerId;
    }

    public byte getBuilding() {
        return building;
    }

    public boolean isAssigned() {
        return playerId != UNASSIGNED_PLAYER;
    }
}
