package com.SpringField.engine;

import com.SpringField.engine.board.Player;
import com.SpringField.engine.board.Vertex;

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
    protected byte[] devCardsAcquiredThisTurn;
    protected boolean settlementPhase;
    protected byte playerWithLargestArmy;
    protected byte playerWithLongestRoad;
    protected byte currentLongestRoad;
    protected byte playerTurn;
    protected byte robberTile;

    // TODO Recycle Random
    private Random randomGen = new Random();

    protected BoardState() {
    }

    public BoardState(int numPlayers) {
        vertices = new Vertex[DEFAULT_NUM_VERTICES];
        edges = new byte[DEFAULT_NUM_EDGES];
        players = new Player[numPlayers];
        initBoard(numPlayers);
        resourceCardPool = new byte[] { DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT,
                DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT };
        devCardPool = new byte[] { DEFAULT_NUM_KNIGHT, DEFAULT_NUM_VICTORY, DEFAULT_NUM_ROAD_BUILDING,
                DEFAULT_NUM_MONOPOLY, DEFAULT_NUM_YEAR_OF_PLENTY };
        devCardsAcquiredThisTurn = new byte[] { 0, 0, 0, 0 };
        settlementPhase = true;
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

    public byte[] getResourceCardPool() {
        return resourceCardPool;
    }

    public byte getPlayerTurn() {
        return playerTurn;
    }
    
    protected Player getCurrentPlayer(){
        return getCurrentPlayer();
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

    private byte numDevCardsAvailable() {
        byte total = 0;
        for (byte amount : devCardPool) {
            total += amount;
        }
        return total;
    }

    public boolean canBuildRoad(byte edgeId, boolean buy) {
        if (!settlementPhase && !getCurrentPlayer().canBuyRoad()) {
            return false;
        }
        return canBuildRoadHelper(edgeId, UNASSIGNED_EDGE);
    }

    public boolean canBuildRoadHelper(byte edgeId, byte ghostEdge) {
        if (edges[edgeId] != UNASSIGNED_EDGE) {
            return false;
        }
        for (byte v : edgeToVertex[edgeId]) {
            if (vertices[v].getPlayerId() == playerTurn) {
                return true;
            }
            for (byte e : vertexToEdge[v]) {
                if (edges[e] == playerTurn) {
                    return true;
                }
                if (ghostEdge != UNASSIGNED_EDGE && e == ghostEdge) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canBuildSettlement(byte vertexId) {
        if (!settlementPhase && !getCurrentPlayer().canBuySettlement()) {
            return false;
        }
        if (vertices[vertexId].isSettled()) {
            return false;
        }
        boolean foundRoad = false;
        // Getting all the edges coming out of that vertex
        for (byte e : vertexToEdge[vertexId]) {
            if (edges[e] == playerTurn) {
                foundRoad = true;
            }
            // Checking the vertexes on the other sides of the edges
            for (byte v : edgeToVertex[e]) {
                Vertex adjacentVertex = vertices[v];
                if (adjacentVertex.isSettled()) {
                    return false;
                }
            }
        }
        // TODO Why does Intellij say this is always false?
        if (!foundRoad) {
            return false;
        }
        return true;
    }

    public boolean canBuildCity(byte vertexId) {
        if (!getCurrentPlayer().canBuyCity()) {
            return false;
        }
        Vertex v = vertices[vertexId];
        if (v.getBuilding() != STATUS_SETTLEMENT) {
            return false;
        }
        return v.getPlayerId() == playerTurn;
    }

    public boolean canPlayRobber(byte tileId) {
        return robberTile != tileId;
    }

    public boolean canBuyDevCard() {
        if (numDevCardsAvailable() == 0) {
            return false;
        }
        return getCurrentPlayer().canBuyDevCard();
    }

    public boolean canPlayKnightCard(byte tileId) {
        if (!canPlayDevCard(KNIGHT)) {
            return false;
        }
        return tileId != robberTile;
    }

    public boolean canPlayRoadBuilding(byte e1, byte e2) {
        if (!canPlayDevCard(ROAD_BUILDING)) {
            return false;
        }
        if (!canBuildRoadHelper(e1, UNASSIGNED_EDGE)) {
            return false;
        }
        return canBuildRoadHelper(e2, e1);
    }

    public boolean canPlayMonopoly() {
        return canPlayDevCard(MONOPOLY);
    }

    public boolean canPlayYearOfPlenty(byte r1, byte r2) {
        if (resourceCardPool[r1] == 0) {
            return false;
        }
        if (resourceCardPool[r2] == 0) {
            return false;
        }
        return canPlayDevCard(YEAR_OF_PLENTY);
    }

    public boolean canPlayDevCard(byte type) {
        Player p = getCurrentPlayer();
        if (p.getDevCards()[type] <= devCardsAcquiredThisTurn[type]) {
            return false;
        }
        if (!p.canPlayDevCard(type)) {
            return false;
        }
        return true;
    }

    public boolean canTradeBank(byte playerResource, byte bankResource) {
        return getCurrentPlayer().canTradeBank(playerResource) && resourceCardPool[bankResource] > 0;
    }

    public void buildRoad(byte edgeId, boolean pay) {
        if (!canBuildRoad(edgeId, pay)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        edges[edgeId] = playerTurn;
        p.buyRoad(pay);
        updateLargestRoad(edgeId);
        resourceCardPool[WOOD]++;
        resourceCardPool[BRICK]++;
    }

    public void buildSettlement(byte vertexId, boolean pay) {
        if (!canBuildSettlement(vertexId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        Vertex v = vertices[vertexId];
        p.buySettlement(pay);
        v.setPlayerId(playerTurn);
        v.setBuilding(STATUS_SETTLEMENT);
        if (v.getPort() != UNASSIGNED_PORT) {
            p.addPort(v.getPort());
        }
        resourceCardPool[WOOD]++;
        resourceCardPool[BRICK]++;
        resourceCardPool[SHEEP]++;
        resourceCardPool[HAY]++;
    }

    public void buildCity(byte vertexId) {
        if (!canBuildCity(vertexId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Vertex v = vertices[vertexId];
        Player p = getCurrentPlayer();
        p.buyCity();
        v.setPlayerId(playerTurn);
        v.setBuilding(STATUS_CITY);
        resourceCardPool[HAY] += 2;
        resourceCardPool[ROCK] += 3;
    }

    public byte playRobber(byte tileId, byte playerIdSteal) {
        if (!canPlayRobber(tileId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        robberTile = tileId;
        byte type = players[playerIdSteal].stealResource();
        if (type != INVALID_RESOURCE) {
            p.addResource(type, (byte) 1);
        }
        return type;
    }

    public byte buyDevCard() {
        if (!canBuyDevCard()) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        byte type = getRandomSlot(devCardPool);
        p.buyDevCard(type);
        if (type != VICTORY) {
            devCardsAcquiredThisTurn[type]++;
        }
        resourceCardPool[SHEEP]++;
        resourceCardPool[HAY]++;
        resourceCardPool[ROCK]++;
        return type;
    }

    public byte playKnightCard(byte tileId, byte playerIdSteal) {
        if (!canPlayKnightCard(tileId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(KNIGHT);
        updateLargestArmy();
        return playRobber(tileId, playerIdSteal);
    }

    public void playRoadBuilding(byte e1, byte e2) {
        if (!canPlayRoadBuilding(e1, e2)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(ROAD_BUILDING);
        buildRoad(e1, false);
        buildRoad(e1, false);
    }

    /*
     * Currently returns total amount stolen. For proper info we should return the amount stolen from each player as
     * well.
     */
    public byte playMonopoly(byte resourceType) {
        if (!canPlayMonopoly()) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(MONOPOLY);
        byte totalStolen = 0;
        for (int i = 0; i < players.length; i++) {
            if (playerTurn == i) {
                break;
            }
            totalStolen += players[i].stealAllResource(resourceType);
        }
        return totalStolen;
    }

    public void playYearOfPlenty(byte r1, byte r2) {
        if (!canPlayYearOfPlenty(r1, r2)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(YEAR_OF_PLENTY);
        p.addResource(r1, (byte) 1);
        p.addResource(r2, (byte) 1);
    }

    public void tradeBank(byte playerResource, byte bankResource) {
        getCurrentPlayer().tradeBank(playerResource, bankResource);
    }

    public byte advanceTurn() {
        if (computeVictoryPoints(playerTurn) >= VICTORY_POINTS_REQ_WIN) {
            return WIN_CONDITION;
        }
        Arrays.fill(devCardsAcquiredThisTurn, (byte) 0);
        playerTurn++;
        if (playerTurn == players.length) {
            playerTurn = 0;
        }
        return rollDice();
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
                    if (v.isSettled()) {
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

    private void updateLargestArmy() {
        if (playerTurn == playerWithLargestArmy) {
            return;
        }
        if (computeArmy(playerTurn) > computeArmy(playerWithLargestArmy)) {
            playerWithLargestArmy = playerTurn;
        }
    }

    /*
     * Longest Road Algorithm
     */
    private void updateLargestRoad(byte edgeId) {
        HashSet<Byte> seenEdges = new HashSet<>();
        seenEdges.add(edgeId);
        byte maxRoadLength = 1;
        for (byte n : edgeToVertex[edgeId]) {
            maxRoadLength += transverseNode(seenEdges, n, maxRoadLength);
        }
        if (maxRoadLength > currentLongestRoad) {
            currentLongestRoad = maxRoadLength;
            playerWithLongestRoad = playerTurn;
        }
    }

    private byte transverseNode(HashSet<Byte> seenEdges, byte nodeId, byte currentRoadLength) {
        byte maxRoadLength = currentRoadLength;
        byte[] outgoingEdges = vertexToEdge[nodeId];
        for (byte edgeId : outgoingEdges) {
            if (seenEdges.contains(edgeId)) {
                continue;
            }
            if (edges[edgeId] == playerTurn) {
                byte nextNodeId = -1;
                for (byte n : edgeToVertex[edgeId]) {
                    if (n != nodeId) {
                        nextNodeId = n;
                    }
                }
                if (nextNodeId == -1) {
                    throw new RuntimeException("Next Node not found");
                }
                byte roadLength = transverseNode(seenEdges, nextNodeId, ++currentRoadLength);
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
