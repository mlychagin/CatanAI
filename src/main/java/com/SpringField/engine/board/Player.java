package com.SpringField.engine.board;

import java.util.Arrays;

import static com.SpringField.engine.util.Util.*;

public class Player {
    private byte[] resources = new byte[] { 0, 0, 0, 0, 0 };
    private byte[] structures = new byte[] { DEFAULT_ROAD_COUNT, DEFAULT_SETTLEMENT_COUNT, DEFAULT_CITY_COUNT };
    private byte[] devCards = new byte[] { 0, 0, 0, 0, 0 };
    private boolean[] ports = new boolean[] { false, false, false, false };
    private boolean generalPort = false;
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

    public boolean[] getPorts() {
        return ports;
    }

    public boolean hasGeneralPort() {
        return generalPort;
    }

    public byte getKnightsPlayed() {
        return knightsPlayed;
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

    public void addPort(byte type) {
        if (type == ANY) {
            generalPort = true;
            return;
        }
        checkResource(type);
        ports[type] = true;
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

    public boolean canPlayDevCard(byte type) {
        checkDevCard(type);
        return devCards[type] > 0 && type != VICTORY;
    }

    public boolean canTradeBank(byte playerResource) {
        return resources[playerResource] >= tradeBankHelper(playerResource);
    }

    public byte tradeBankHelper(byte playerResource) {
        byte tradeAmount;
        if (ports[playerResource]) {
            tradeAmount = 2;
        } else if (generalPort) {
            tradeAmount = 3;
        } else {
            tradeAmount = 4;
        }
        return tradeAmount;
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

    public void playDevCard(byte type) {
        if (!canPlayDevCard(type)) {
            throw new RuntimeException("Invalid Transaction");
        }
        devCards[type]--;
        if (type == KNIGHT) {
            knightsPlayed++;
        }
    }

    public void tradeBank(byte playerResource, byte bankResource) {
        if (!canTradeBank(playerResource)) {
            throw new RuntimeException("Invalid Transaction");
        }
        resources[playerResource] -= tradeBankHelper(playerResource);
        resources[bankResource]++;
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

    public Player clone() {
        Player p = new Player();
        p.resources = resources.clone();
        p.structures = structures.clone();
        p.devCards = devCards.clone();
        p.ports = ports.clone();
        p.generalPort = generalPort;
        p.knightsPlayed = knightsPlayed;
        return p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Player player = (Player) o;

        if (generalPort != player.generalPort)
            return false;
        if (knightsPlayed != player.knightsPlayed)
            return false;
        if (!Arrays.equals(resources, player.resources))
            return false;
        if (!Arrays.equals(structures, player.structures))
            return false;
        if (!Arrays.equals(devCards, player.devCards))
            return false;
        return Arrays.equals(ports, player.ports);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(resources);
        result = 31 * result + Arrays.hashCode(structures);
        result = 31 * result + Arrays.hashCode(devCards);
        result = 31 * result + Arrays.hashCode(ports);
        result = 31 * result + (generalPort ? 1 : 0);
        result = 31 * result + (int) knightsPlayed;
        return result;
    }
}
