package com.SpringField.engine;


import com.SpringField.engine.board.Player;
import com.SpringField.engine.board.Tile;
import com.SpringField.engine.board.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static com.SpringField.engine.util.Util.*;

public class BoardState {
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<Byte> edges = new ArrayList<>();
    private ArrayList<Player> playerList = new ArrayList<>();
    private byte[] devCardPool = new byte[] { DEFAULT_NUM_KNIGHT, DEFAULT_NUM_VICTORY, DEFAULT_NUM_ROAD_BUILDING,
            DEFAULT_NUM_MONOPOLY, DEFAULT_NUM_YEAR_OF_PLENTY };
    private byte playerWithLargestArmy = UNASSIGNED_PLAYER;
    private byte playerWithLongestRoad = UNASSIGNED_PLAYER;
    private byte longestRoadCount;

    private byte playerTurn = UNASSIGNED_PLAYER;
    private byte robberTile;

    private Random randomGen = new Random();

    BoardState(int numPlayers) {
        initBoard(numPlayers);
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Byte> getEdges() {
        return edges;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public byte getPlayerTurn() {
        return playerTurn;
    }

    private byte numDevCardsAvailable() {
        byte total = 0;
        for (byte amount : devCardPool) {
            total += amount;
        }
        return total;
    }

    public void initBoard(int numPlayers) {
        for (int i = 0; i < DEFAULT_NUM_VERTICES; i++) {
            vertices.add(new Vertex());
        }
        for (int i = 0; i < edgeToVertex.length; i++) {
            edges.add(UNASSIGNED_PLAYER);
        }
        for (int i = 0; i < numPlayers; i++) {
            playerList.add(new Player());
        }
        for (int i = 0; i < tiles.size(); i++) {
            Tile t = tiles.get(i);
            if (t.getResourceType() == DESERT) {
                robberTile = (byte) i;
                break;
            }
        }
    }

    public void placeSettlement(byte playerId, byte vertexId, boolean pay) {
        Player p = playerList.get(playerId);
        if (pay && !p.canBuySettlement()) {
            throw new RuntimeException("Insufficient Resources");
        }
        Vertex v = vertices.get(vertexId);
        v.setPlayerId(playerId);
        v.setBuilding(STATUS_SETTLEMENT);
        if (pay) {
            p.buySettlement();
        }
    }

    public void placeRoad(byte playerId, byte edgeId, boolean pay) {
        Player p = playerList.get(playerId);
        if (pay && !p.canBuyRoad()) {
            throw new RuntimeException("Insufficient Resources");
        }
        if (edges.get(edgeId) != UNASSIGNED_PLAYER) {
            throw new RuntimeException("Edge already has road");
        }
        edges.set(edgeId, playerId);
        if (pay) {
            p.buyRoad();
        }
    }

    public void placeCity(byte playerId, byte vertexId, boolean pay) {
        Player p = playerList.get(playerId);
        if (pay && !p.canBuyCity()) {
            throw new RuntimeException("Insufficient Resources");
        }
        Vertex v = vertices.get(vertexId);
        if (v.getBuilding() != STATUS_SETTLEMENT) {
            throw new RuntimeException("City not placed on settlement");
        }
        if (v.getPlayerId() != playerId) {
            throw new RuntimeException("Settlement not owned by player");
        }
        v.setPlayerId(playerId);
        v.setBuilding(STATUS_CITY);
        if (pay) {
            p.buyCity();
        }
    }

    public byte buyDevCard(byte playerId) {
        Player p = playerList.get(playerId);
        byte totalDevCards = numDevCardsAvailable();
        if (!p.canBuyDevCard() || totalDevCards == 0) {
            throw new RuntimeException("Invalid Dev Card Purchase");
        }
        byte cardSelection = (byte) randomGen.nextInt(totalDevCards);
        byte cardType = 0;
        boolean found = false;
        for (int i = 0; i < devCardPool.length; i++) {
            int amountAvailable = devCardPool[i];
            cardSelection -= amountAvailable;
            if (cardSelection <= 0) {
                cardType = (byte) i;
                found = true;
            }
        }
        if (!found) {
            throw new RuntimeException("Dev Card algorithm failed");
        }
        return cardType;
    }

    public void advanceTurn() {
        playerTurn++;
        if (playerTurn == playerList.size()) {
            playerTurn = 0;
        }
    }

    private byte rollDice() {
        byte roll = (byte) (randomGen.nextInt(6) + randomGen.nextInt(6) + 2);
        for (int tileNum = 0; tileNum < tiles.size(); tileNum++) {
            Tile t = tiles.get(tileNum);
            if (roll == t.getRollNumber()) {
                for (int vertexNum : tileToVertex[tileNum]) {
                    Vertex v = vertices.get(vertexNum);
                    if (v.isAssigned()) {
                        Player p = playerList.get(v.getPlayerId());
                        p.addResource(t.getResourceType(), v.getBuilding());
                    }
                }
            }
        }
        return roll;
    }

    private byte computeArmy(byte playerId) {
        return playerList.get(playerId).getKnightsPlayed();
    }

    public byte computeVictoryPoints(byte playerId) {
        byte points = playerList.get(playerId).getNumVictoryPoints();
        if (playerWithLargestArmy == playerId) {
            points += 2;
        }
        if (playerWithLongestRoad == playerId) {
            points += 2;
        }
        return points;
    }

    /*
     * Todo
     */

    private byte computeRoad(byte playerId) {
        return 0;
    }

    private void updateLargestArmy(byte playerId) {
        if (playerId == playerWithLargestArmy) {
            return;
        }
        if (playerList.get(playerId).getKnightsPlayed() > playerList.get(playerWithLargestArmy).getKnightsPlayed()) {
            playerWithLargestArmy = playerId;
        }
    }

    public ArrayList<BoardState> getAllPossibleMoves() {
        return null;
    }

    /*
     * Longest Road Algorithm
     */
    private void updateLargestRoad(byte playerId, byte edgeId) {

    }

    private int transverseNode(HashSet<Byte> seenEdges, byte playerId, byte nodeId, byte roadLength) {
        byte maxRoadLength = roadLength;
        byte[] outgoingEdges = nodeToEdge[nodeId];
        for (byte edgeId : outgoingEdges) {
            if (seenEdges.contains(edgeId)) {
                continue;
            }
            if (edges.get(edgeId) == playerId) {
                byte nextNodeId = -1;
                for (byte n : edgeToVertex[edgeId]) {
                    if (n != nodeId) {
                        nextNodeId = n;
                    }
                }
                if (nextNodeId == -1) {
                    throw new RuntimeException("Next Node not found");
                }
                // byte roadLength = transverseNode(seenEdges, playerId, nextNodeId, ++roadLength);
            }
        }
        return 0;
    }

}
