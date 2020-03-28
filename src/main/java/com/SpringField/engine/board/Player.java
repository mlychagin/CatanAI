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

    public void addResource(byte type, byte amount) {
        resources[type] += amount;
    }

    public void removeResource(byte type, byte amount) {
        if (resources[type] < amount) {
            throw new RuntimeException("Invalid Transaction");
        }
        resources[type] -= amount;
    }

    public byte getKnightsPlayed() {
        return knightsPlayed;
    }

    public byte getTotalResourceCount() {
        byte total = 0;
        for (byte b : resources) {
            total += b;
        }
        return total;
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

    public void buyRoad(boolean pay) {
        if (!pay) {
            return;
        }
        if (!canBuyRoad()) {
            throw new RuntimeException("Invalid Transaction");
        }
        resources[WOOD] -= 1;
        resources[BRICK] -= 1;
        structures[ROAD] -= 1;
    }

    public void buySettlement(boolean pay) {
        if (pay && !canBuySettlement()) {
            throw new RuntimeException("Invalid Transaction");
        }
        if (pay) {
            resources[WOOD] -= 1;
            resources[BRICK] -= 1;
            resources[SHEEP] -= 1;
            resources[HAY] -= 1;
        }
        structures[SETTLEMENT] -= 1;
    }

    public void buyCity() {
        if (!canBuyCity()) {
            throw new RuntimeException("Invalid Transaction");
        }
        resources[HAY] -= 2;
        resources[ROCK] -= 3;
        structures[CITY] -= 1;
        structures[SETTLEMENT] += 1;
    }

    public void buyDevCard(byte type) {
        checkDevCard(type);
        if (!canBuyDevCard()) {
            throw new RuntimeException("Invalid Transaction");
        }
        resources[SHEEP] -= 1;
        resources[HAY] -= 1;
        resources[ROCK] -= 1;
        devCards[type]++;
    }

    public boolean canPlayDevCard(byte type) {
        checkDevCard(type);
        return devCards[type] > 0;
    }

    public void playDevCard(byte type) {
        if (canPlayDevCard(type)) {
            throw new RuntimeException("Invalid Transaction");
        }
        devCards[type]--;
        if (type == KNIGHT) {
            knightsPlayed++;
        }
    }

    public byte stealResource() {
        if (getTotalResourceCount() == 0) {
            return INVALID_RESOURCE;
        }
        byte type = getRandomSlot(resources);
        if (resources[type] == 0) {
            throw new RuntimeException("Algorithm Failure");
        }
        resources[type]--;
        return type;
    }

    public byte stealAllResource(byte type) {
        checkResource(type);
        byte stolenAmount = resources[type];
        resources[type] = 0;
        return stolenAmount;
    }

    private void checkResource(byte type) {
        switch (type) {
        case WOOD:
        case BRICK:
        case SHEEP:
        case HAY:
        case ROCK:
            break;
        default:
            throw new RuntimeException("Invalid Transaction");
        }
    }

    private void checkDevCard(byte type) {
        switch (type) {
        case KNIGHT:
        case VICTORY:
        case ROAD_BUILDING:
        case MONOPOLY:
        case YEAR_OF_PLENTY:
            break;
        default:
            throw new RuntimeException("Invalid Transaction");
        }
    }

    /*
     * Note: This does not include victory points from LargestArmy or LongestRoad
     */
    public byte getNumVictoryPoints() {
        return (byte) ((DEFAULT_SETTLEMENT_COUNT - structures[SETTLEMENT]) + 2 * (DEFAULT_CITY_COUNT - structures[CITY])
                + devCards[VICTORY]);
    }

}
