package com.SpringField.engine.board;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.SpringField.engine.util.Util.STATUS_EMPTY;
import static com.SpringField.engine.util.Util.UNASSIGNED_PLAYER;

public class Vertex {
    private byte playerId;
    private byte building;
    private byte port;

    public Vertex(byte port) {
        this.playerId = UNASSIGNED_PLAYER;
        this.building = STATUS_EMPTY;
        this.port = port;
    }

    private Vertex(byte playerId, byte building, byte port) {
        this.playerId = playerId;
        this.building = building;
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

    public boolean isSettled() {
        return playerId != UNASSIGNED_PLAYER;
    }

    public Vertex clone() {
        return new Vertex(playerId, building, port);
    }

    public void serialize(ObjectOutputStream output) throws IOException {
        output.writeByte(playerId);
        output.writeByte(building);
        output.writeByte(port);
    }

    public static Vertex deSerialize(ObjectInputStream input) throws IOException {
        return new Vertex(input.readByte(), input.readByte(), input.readByte());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Vertex vertex = (Vertex) o;

        if (playerId != vertex.playerId)
            return false;
        if (building != vertex.building)
            return false;
        return port == vertex.port;
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + (int) building;
        result = 31 * result + (int) port;
        return result;
    }
}
