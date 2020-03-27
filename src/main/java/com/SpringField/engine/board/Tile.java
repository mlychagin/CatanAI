package com.SpringField.engine.board;

public class Tile {
    private final byte resourceType;
    private final byte rollNumber;

    public Tile(byte resourceType, byte rollNumber) {
        this.resourceType = resourceType;
        this.rollNumber = rollNumber;
    }

    public byte getResourceType() {
        return resourceType;
    }

    public byte getRollNumber() {
        return rollNumber;
    }

}
