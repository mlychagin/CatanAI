package com.SpringField.engine.board;

import static com.SpringField.engine.util.Util.*;

public class Player {
    private byte[] resources = new byte[] { 0, 0, 0, 0, 0, 0 };
    private byte[] structures = new byte[] { DEFAULT_ROAD_COUNT, DEFAULT_SETTLEMENT_COUNT, DEFAULT_CITY_COUNT };
    private byte[] devCards = new byte[] { 0, 0, 0, 0, 0 };
    private byte knightsPlayed = 0;

    public Player() {
    }

    public byte[] getResources() {
        return resources;
    }

    public byte[] getStructures() {
        return structures;
    }

    public byte[] getDevCards() {
        return devCards;
    }

    public byte getKnightsPlayed() {
        return knightsPlayed;
    }

    public void addResource(byte type, byte amount) {
        resources[type] += amount;
    }

    public boolean canBuyRoad() {
        return resources[WOOD] >= 1 && resources[BRICK] >= 1 && structures[ROAD] >= 1;
    }

    public boolean canBuySettlement() {
        return resources[WOOD] >= 1 && resources[BRICK] >= 1 && resources[SHEEP] >= 1 && resources[HAY] >= 1
                && structures[SETTLEMENT] >= 1;
    }

    public boolean canBuyCity() {
        return resources[HAY] >= 2 && resources[ROCK] >= 3 && structures[CITY] >= 1;
    }

    public boolean canBuyDevCard() {
        return resources[SHEEP] >= 1 && resources[HAY] >= 1 && resources[ROCK] >= 1;
    }

    public void buyRoad() {
        if (resources[WOOD] == 0 || resources[BRICK] == 0 || structures[ROAD] == 0) {
            throw new RuntimeException("Invalid Transaction");
        }
        resources[WOOD] -= 1;
        resources[BRICK] -= 1;
        structures[ROAD] -= 1;
    }

    public void buySettlement() {
        if (resources[WOOD] == 0 || resources[BRICK] == 0 || resources[SHEEP] == 0 || resources[HAY] == 0
                || structures[SETTLEMENT] == 0) {
            throw new RuntimeException("Invalid Transaction");
        }
        resources[WOOD] -= 1;
        resources[BRICK] -= 1;
        resources[SHEEP] -= 1;
        resources[HAY] -= 1;
        structures[SETTLEMENT] -= 1;
    }

    public void buyCity() {
        if (resources[HAY] < 2 || resources[ROCK] < 3 || structures[CITY] == 0) {
            throw new RuntimeException("Invalid Transaction");
        }
        resources[HAY] -= 2;
        resources[ROCK] -= 3;
        structures[CITY] -= 1;
        structures[SETTLEMENT] += 1;
    }

    public void buyDevCard(byte type) {
        if (resources[SHEEP] == 0 || resources[HAY] == 0 || resources[ROCK] == 0) {
            throw new RuntimeException("Invalid Transaction");
        }
        resources[SHEEP] -= 1;
        resources[HAY] -= 1;
        resources[ROCK] -= 1;
        devCards[type]++;
    }

    public boolean canPlayDevCard(byte type) {
        return devCards[type] > 0;
    }

    public void playDevCard(byte type) {
        if (devCards[type] == 0 || type == VICTORY) {
            throw new RuntimeException("Invalid Transaction");
        }
        devCards[type]--;
        if (type == KNIGHT) {
            knightsPlayed++;
        }
    }

    /*
     * Note: This does not include victory points from LargestArmy or LongestRoad
     */
    public byte getNumVictoryPoints() {
        return (byte) ((DEFAULT_SETTLEMENT_COUNT -= structures[SETTLEMENT]) + (DEFAULT_CITY_COUNT -= structures[CITY])
                + devCards[VICTORY]);
    }

}
