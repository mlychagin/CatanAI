package com.SpringField.engine;

import com.SpringField.engine.board.Player;
import com.SpringField.engine.board.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static com.SpringField.engine.util.Util.*;

public class BoardState {
    protected Vertex[] vertices;
    protected byte[] edges;
    protected Player[] players;
    protected byte[] resourceCardPool;
    protected byte[] devCardPool;
    protected byte playerWithLargestArmy;
    protected byte playerWithLongestRoad;
    protected byte currentLongestRoad;
    protected byte playerTurn;
    protected byte robberTile;

    private Random randomGen = new Random();

    protected BoardState() {
    }

    public BoardState(int numPlayers) {
        vertices = new Vertex[DEFAULT_NUM_VERTICES];
        edges = new byte[DEFAULT_NUM_EDGES];
        players = new Player[numPlayers];
        initBoard(numPlayers);
        resourceCardPool = new byte[] { DEFAULT_DESERT_COUNT, DEFAULT_WOOD_COUNT, DEFAULT_BRICK_COUNT,
                DEFAULT_SHEEP_COUNT, DEFAULT_HAY_COUNT, DEFAULT_ROCK_COUNT };
        devCardPool = new byte[] { DEFAULT_DESERT_COUNT, DEFAULT_WOOD_COUNT, DEFAULT_BRICK_COUNT, DEFAULT_SHEEP_COUNT,
                DEFAULT_HAY_COUNT, DEFAULT_ROCK_COUNT };
        playerWithLargestArmy = UNASSIGNED_PLAYER;
        playerWithLongestRoad = UNASSIGNED_PLAYER;
        currentLongestRoad = 0;
        playerTurn = 0;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public byte[] getEdges() {
        return edges;
    }

    public Player[] getPlayers() {
        return players;
    }

    public byte getPlayerTurn() {
        return playerTurn;
    }

    private void checkPlayerTurn(byte playerId) {
        if (playerTurn != playerId) {
            throw new RuntimeException("Invalid Transaction");
        }
    }

    private byte numDevCardsAvailable() {
        byte total = 0;
        for (byte amount : devCardPool) {
            total += amount;
        }
        return total;
    }

    public boolean canSettle(byte vertexId) {
        // Getting all the edges coming out of that vertex
        for (byte e : vertexToEdge[vertexId]) {
            // Checking the vertexes on the other sides of the edges
            for (byte v : edgeToVertex[e]) {
                Vertex adjacentVertex = vertices[v];
                if (adjacentVertex.isAssigned()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void initBoard(int numPlayers) {
        for (int i = 0; i < DEFAULT_NUM_VERTICES; i++) {
            vertices[i] = new Vertex(vertexToPort[i]);
        }
        for (int i = 0; i < DEFAULT_NUM_EDGES; i++) {
            edges[i] = UNASSIGNED_PLAYER;
        }
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
        }
        for (int i = 0; i < tilesResource.length; i++) {
            byte r = tilesResource[i];
            if (r == DESERT) {
                robberTile = (byte) i;
                break;
            }
        }
    }

    public void placeSettlement(byte playerId, byte vertexId, boolean pay) {
        checkPlayerTurn(playerId);
        if (!canSettle(vertexId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = players[playerId];
        Vertex v = vertices[vertexId];
        if (v.getPlayerId() != UNASSIGNED_PLAYER) {
            throw new RuntimeException("Invalid Transaction");
        }
        p.buySettlement(pay);
        v.setPlayerId(playerId);
        v.setBuilding(STATUS_SETTLEMENT);
        if (v.getPort() != UNASSIGNED_PORT) {
            p.addPort(v.getPort());
        }
        resourceCardPool[WOOD]++;
        resourceCardPool[BRICK]++;
        resourceCardPool[SHEEP]++;
        resourceCardPool[HAY]++;
    }

    public void placeRoad(byte playerId, byte edgeId, boolean pay) {
        checkPlayerTurn(playerId);
        Player p = players[playerId];
        if (edges[edgeId] != UNASSIGNED_PLAYER) {
            throw new RuntimeException("Invalid Transaction");
        }
        edges[edgeId] = playerId;
        p.buyRoad(pay);
        updateLargestRoad(playerId, edgeId);
        resourceCardPool[WOOD]++;
        resourceCardPool[BRICK]++;
    }

    public void placeCity(byte playerId, byte vertexId) {
        checkPlayerTurn(playerId);
        Vertex v = vertices[vertexId];
        if (v.getBuilding() != STATUS_SETTLEMENT) {
            throw new RuntimeException("Invalid Transaction");
        }
        if (v.getPlayerId() != playerId) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = players[playerId];
        p.buyCity();
        v.setPlayerId(playerId);
        v.setBuilding(STATUS_CITY);
        resourceCardPool[HAY] += 2;
        resourceCardPool[ROCK] += 3;
    }

    public byte playRobber(byte playerId, byte tileId, byte playerIdSteal) {
        checkPlayerTurn(playerId);
        Player p = players[playerId];
        robberTile = tileId;
        byte type = players[playerIdSteal].stealResource();
        p.addResource(type, (byte) 1);
        return type;
    }

    public byte buyDevCard(byte playerId) {
        checkPlayerTurn(playerId);
        Player p = players[playerId];
        if (numDevCardsAvailable() == 0) {
            throw new RuntimeException("Invalid Transaction");
        }
        byte type = getRandomSlot(devCardPool);
        p.buyDevCard(type);
        resourceCardPool[SHEEP]++;
        resourceCardPool[HAY]++;
        resourceCardPool[ROCK]++;
        return type;
    }

    public byte playKnightCard(byte playerId, byte tileId, byte playerIdSteal) {
        checkPlayerTurn(playerId);
        Player p = players[playerId];
        p.playDevCard(KNIGHT);
        updateLargestArmy(playerId);
        return playRobber(playerId, tileId, playerIdSteal);
    }

    public void playRoadBuilding(byte playerId, byte e1, byte e2) {
        checkPlayerTurn(playerId);
        Player p = players[playerId];
        if (edges[e1] != UNASSIGNED_PLAYER || edges[e2] != UNASSIGNED_PLAYER) {
            throw new RuntimeException("Invalid Transaction");
        }
        p.playDevCard(ROAD_BUILDING);
        placeRoad(playerId, e1, false);
        placeRoad(playerId, e1, false);
    }

    /*
     * Currently returns total amount stolen. For proper info we should return the amount stolen from each player as
     * well.
     */
    public byte playMonopoly(byte playerId, byte resourceType) {
        checkPlayerTurn(playerId);
        Player p = players[playerId];
        p.playDevCard(MONOPOLY);
        byte totalStolen = 0;
        for (int i = 0; i < players.length; i++) {
            if (playerId == i) {
                break;
            }
            totalStolen += players[i].stealAllResource(resourceType);
        }
        return totalStolen;
    }

    /*
     * Concept of a transaction addResource() could fail because of bank restrictions. Bank has yet to be implemented.
     */
    public void playYearOfPlenty(byte playerId, byte r1, byte r2) {
        checkPlayerTurn(playerId);
        Player p = players[playerId];
        p.playDevCard(YEAR_OF_PLENTY);
        p.addResource(r1, (byte) 1);
        p.addResource(r2, (byte) 1);
    }

    public void tradeBank(byte playerId, byte playerResource, byte bankResource) {
        players[playerId].tradeBank(playerResource, bankResource);
    }

    public void advanceTurn() {
        playerTurn++;
        if (playerTurn == players.length) {
            playerTurn = 0;
        }
    }

    private byte rollDice() {
        byte roll = (byte) (randomGen.nextInt(6) + randomGen.nextInt(6) + 2);
        if (roll == 7) {
            return roll;
        }
        for (int tileNum = 0; tileNum < tilesResource.length; tileNum++) {
            if (tileNum == robberTile) {
                continue;
            }
            byte resourceType = tilesResource[tileNum];
            if (resourceType == DESERT) {
                continue;
            }
            if (roll == tilesNumber[tileNum]) {
                for (int vertexNum : tileToVertex[tileNum]) {
                    Vertex v = vertices[vertexNum];
                    if (v.isAssigned()) {
                        Player p = players[v.getPlayerId()];
                        byte resourceAmount;
                        switch (v.getBuilding()) {
                        case SETTLEMENT:
                            resourceAmount = 1;
                            break;
                        case CITY:
                            resourceAmount = 2;
                            break;
                        default:
                            throw new RuntimeException("Invalid Transaction");
                        }
                        if (resourceCardPool[resourceType] < resourceAmount) {
                            resourceAmount = resourceCardPool[resourceType];
                        }
                        p.addResource(resourceType, resourceAmount);
                    }
                }
            }
        }
        return roll;
    }

    private byte computeArmy(byte playerId) {
        return players[playerId].getKnightsPlayed();
    }

    private void updateLargestArmy(byte playerId) {
        if (playerId == playerWithLargestArmy) {
            return;
        }
        if (computeArmy(playerId) > computeArmy(playerWithLargestArmy)) {
            playerWithLargestArmy = playerId;
        }
    }

    /*
     * Longest Road Algorithm
     */
    private void updateLargestRoad(byte playerId, byte edgeId) {
        HashSet<Byte> seenEdges = new HashSet<>();
        seenEdges.add(edgeId);
        byte maxRoadLength = 1;
        for (byte n : edgeToVertex[edgeId]) {
            maxRoadLength += transverseNode(seenEdges, playerId, n, maxRoadLength);
        }
        if (maxRoadLength > currentLongestRoad) {
            currentLongestRoad = maxRoadLength;
            playerWithLongestRoad = playerId;
        }
    }

    private byte transverseNode(HashSet<Byte> seenEdges, byte playerId, byte nodeId, byte currentRoadLength) {
        byte maxRoadLength = currentRoadLength;
        byte[] outgoingEdges = vertexToEdge[nodeId];
        for (byte edgeId : outgoingEdges) {
            if (seenEdges.contains(edgeId)) {
                continue;
            }
            if (edges[edgeId] == playerId) {
                byte nextNodeId = -1;
                for (byte n : edgeToVertex[edgeId]) {
                    if (n != nodeId) {
                        nextNodeId = n;
                    }
                }
                if (nextNodeId == -1) {
                    throw new RuntimeException("Next Node not found");
                }
                byte roadLength = transverseNode(seenEdges, playerId, nextNodeId, ++currentRoadLength);
                if (roadLength > maxRoadLength) {
                    maxRoadLength = roadLength;
                }
            }
        }
        return maxRoadLength;
    }

    public byte computeVictoryPoints(byte playerId) {
        byte points = players[playerId].getNumVictoryPoints();
        if (playerWithLargestArmy == playerId) {
            points += 2;
        }
        if (playerWithLongestRoad == playerId) {
            points += 2;
        }
        return points;
    }

    /*
     * Todo: Make sure a player doesn't play a recently acquired dev card 1. Create datastructure that holds dev cards
     * played this turn 2. Create method in Player that queries the number of dev cards they have of a type
     */

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BoardState that = (BoardState) o;

        if (playerTurn != that.playerTurn)
            return false;
        if (robberTile != that.robberTile)
            return false;
        if (!Arrays.equals(vertices, that.vertices))
            return false;
        if (!Arrays.equals(edges, that.edges))
            return false;
        if (!Arrays.equals(players, that.players))
            return false;
        return Arrays.equals(devCardPool, that.devCardPool);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(vertices);
        result = 31 * result + Arrays.hashCode(edges);
        result = 31 * result + Arrays.hashCode(players);
        result = 31 * result + Arrays.hashCode(devCardPool);
        result = 31 * result + (int) playerTurn;
        result = 31 * result + (int) robberTile;
        return result;
    }
}
