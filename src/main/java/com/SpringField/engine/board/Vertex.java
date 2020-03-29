package com.SpringField.engine.board;

import static com.SpringField.engine.util.Util.STATUS_EMPTY;
import static com.SpringField.engine.util.Util.UNASSIGNED_PLAYER;

public class Vertex {
    private byte playerId;
    private byte building;
    private byte port;

    public Vertex(byte port) {
        playerId = UNASSIGNED_PLAYER;
        building = STATUS_EMPTY;
        this.port = port;
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

    public byte getPort() {
        return port;
    }

    public boolean isAssigned() {
        return playerId != UNASSIGNED_PLAYER;
    }
}
