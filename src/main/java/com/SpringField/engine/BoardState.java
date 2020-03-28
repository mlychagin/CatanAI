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
    private ArrayList<Player> players = new ArrayList<>();
    private byte[] devCardPool = new byte[] { DEFAULT_NUM_KNIGHT, DEFAULT_NUM_VICTORY, DEFAULT_NUM_ROAD_BUILDING,
            DEFAULT_NUM_MONOPOLY, DEFAULT_NUM_YEAR_OF_PLENTY };
    private byte playerWithLargestArmy = UNASSIGNED_PLAYER;
    private byte playerWithLongestRoad = UNASSIGNED_PLAYER;
    private byte currentLongestRoad;

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

    public ArrayList<Player> getPlayers() {
        return players;
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
            players.add(new Player());
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
        Player p = players.get(playerId);
        Vertex v = vertices.get(vertexId);
        if (v.getPlayerId() != UNASSIGNED_PLAYER) {
            throw new RuntimeException("Invalid Transaction");
        }
        p.buySettlement(pay);
        v.setPlayerId(playerId);
        v.setBuilding(STATUS_SETTLEMENT);
    }

    public void placeRoad(byte playerId, byte edgeId, boolean pay) {
        Player p = players.get(playerId);
        if (edges.get(edgeId) != UNASSIGNED_PLAYER) {
            throw new RuntimeException("Invalid Transaction");
        }
        edges.set(edgeId, playerId);
        p.buyRoad(pay);
        updateLargestRoad(playerId, edgeId);
    }

    public void placeCity(byte playerId, byte vertexId, boolean pay) {
        Player p = players.get(playerId);
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
        Player p = players.get(playerId);
        if (numDevCardsAvailable() == 0) {
            throw new RuntimeException("No Available Dev Cards");
        }
        byte type = getRandomSlot(devCardPool);
        p.buyDevCard(type);
        return type;
    }

    public byte playKnightCard(byte playerId, byte tileId, byte playerIdSteal) {
        Player p = players.get(playerId);
        p.playDevCard(KNIGHT);
        robberTile = tileId;
        byte type = players.get(playerIdSteal).stealResource();
        p.addResource(type, (byte) 1);
        return type;
    }

    public void playRoadBuilding(byte playerId, byte e1, byte e2) {
        Player p = players.get(playerId);
        if (edges.get(e1) != UNASSIGNED_PLAYER && edges.get(e2) != UNASSIGNED_PLAYER) {
            throw new RuntimeException("Invalid Transaction");
        }
        p.playDevCard(ROAD_BUILDING);
        if (e1 != UNASSIGNED_EDGE) {
            placeRoad(playerId, e1, false);
        }
        if (e2 != UNASSIGNED_EDGE) {
            placeRoad(playerId, e1, false);
        }
    }

    /*
     * Currently returns total amount stolen. For proper info we should return the amount stolen from each player as
     * well.
     */
    public byte playMonopoly(byte playerId, byte resourceType) {
        Player p = players.get(playerId);
        p.playDevCard(MONOPOLY);
        byte totalStolen = 0;
        for (int i = 0; i < players.size(); i++) {
            if (playerId == i) {
                break;
            }
            totalStolen += players.get(i).stealAllResource(resourceType);
        }
        return totalStolen;
    }

    /*
     * Concept of a transaction addResource() could fail because of bank restrictions. Bank has yet to be implemented.
     */
    public void playYearOfPlenty(byte playerId, byte r1, byte r2) {
        Player p = players.get(playerId);
        p.playDevCard(YEAR_OF_PLENTY);
        p.addResource(r1, (byte) 1);
        p.addResource(r2, (byte) 1);
    }

    public void advanceTurn() {
        playerTurn++;
        if (playerTurn == players.size()) {
            playerTurn = 0;
        }
    }

    private byte rollDice() {
        byte roll = (byte) (randomGen.nextInt(6) + randomGen.nextInt(6) + 2);
        if (roll == 7) {
            return roll;
        }
        for (int tileNum = 0; tileNum < tiles.size(); tileNum++) {
            if (tileNum == robberTile) {
                continue;
            }
            Tile t = tiles.get(tileNum);
            if (roll == t.getRollNumber()) {
                for (int vertexNum : tileToVertex[tileNum]) {
                    Vertex v = vertices.get(vertexNum);
                    if (v.isAssigned()) {
                        Player p = players.get(v.getPlayerId());
                        p.addResource(t.getResourceType(), v.getBuilding());
                    }
                }
            }
        }
        return roll;
    }

    private byte computeArmy(byte playerId) {
        return players.get(playerId).getKnightsPlayed();
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
                byte roadLength = transverseNode(seenEdges, playerId, nextNodeId, ++currentRoadLength);
                if (roadLength > maxRoadLength) {
                    maxRoadLength = roadLength;
                }
            }
        }
        return maxRoadLength;
    }

    public byte computeVictoryPoints(byte playerId) {
        byte points = players.get(playerId).getNumVictoryPoints();
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

    public ArrayList<BoardState> getAllPossibleMoves() {
        return null;
    }

}
