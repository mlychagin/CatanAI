package com.SpringField.engine;

import com.SpringField.engine.board.Player;
import com.SpringField.engine.board.Vertex;
import com.SpringField.engine.util.BoardStateConfig;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static com.SpringField.engine.util.Util.*;

public class BoardState {
    protected BoardStateConfig config;
    protected Vertex[] vertices;
    protected byte[] edges;
    protected Player[] players;
    protected byte[] resourceCardPool;
    protected byte[] devCardPool;
    protected byte[] devCardsAcquiredThisTurn;
    protected byte playerWithLargestArmy;
    protected byte playerWithLongestRoad;
    protected byte currentLongestRoad;
    protected byte playerTurn;
    protected byte robberTile;
    protected byte turnNumber;
    protected Random r;

    protected BoardState() {
    }

    public BoardState(BoardStateConfig config, int numPlayers) {
        this.config = config;
        initialize(numPlayers);
        r = new Random();
    }

    public BoardState(BoardStateConfig config, int numPlayers, long seed) {
        this.config = config;
        initialize(numPlayers);
        r = new Random(seed);
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

    protected Player getCurrentPlayer() {
        return players[playerTurn];
    }

    protected void initialize(int numPlayers) {
        vertices = new Vertex[DEFAULT_NUM_VERTICES];
        for (int i = 0; i < DEFAULT_NUM_VERTICES; i++) {
            vertices[i] = new Vertex(vertexToPort[i]);
        }
        edges = new byte[DEFAULT_NUM_EDGES];
        for (int i = 0; i < DEFAULT_NUM_EDGES; i++) {
            edges[i] = UNASSIGNED_PLAYER;
        }
        players = new Player[numPlayers];
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
        resourceCardPool = new byte[] { DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT,
                DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT };
        devCardPool = new byte[] { DEFAULT_NUM_KNIGHT, DEFAULT_NUM_VICTORY, DEFAULT_NUM_ROAD_BUILDING,
                DEFAULT_NUM_MONOPOLY, DEFAULT_NUM_YEAR_OF_PLENTY };
        devCardsAcquiredThisTurn = new byte[] { 0, 0, 0, 0 };
        playerWithLargestArmy = UNASSIGNED_PLAYER;
        playerWithLongestRoad = UNASSIGNED_PLAYER;
        currentLongestRoad = 0;
        playerTurn = 0;
        turnNumber = 0;
    }

    private boolean inSettlementPhase(){
        return turnNumber < players.length * 2;
    }

    private byte numDevCardsAvailable() {
        byte total = 0;
        for (byte amount : devCardPool) {
            total += amount;
        }
        return total;
    }

    public boolean canBuildRoad(byte edgeId, boolean buy) {
        if (!inSettlementPhase() && buy && !getCurrentPlayer().canBuyRoad()) {
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
        if (!inSettlementPhase() && !getCurrentPlayer().canBuySettlement()) {
            return false;
        }
        if (vertices[vertexId].isSettled()) {
            return false;
        }
        boolean foundRoad = inSettlementPhase();
        for (byte e : vertexToEdge[vertexId]) {
            if (edges[e] == playerTurn) {
                foundRoad = true;
            }
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

    public boolean canTradePlayer(byte playerId, byte[] giving){
        return players[playerId].canPlayerTrade(giving);
    }

    public void buildRoad(byte edgeId) throws IOException {
        buildRoadHelper(edgeId, !inSettlementPhase());
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(ROAD_COMMAND);
            dos.writeByte(edgeId);
        }
    }

    protected void buildRoadHelper(byte edgeId, boolean pay) {
        if (!canBuildRoad(edgeId, pay)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        edges[edgeId] = playerTurn;
        p.buyRoad(pay);
        updateLargestRoad(edgeId);
        if (pay){
            resourceCardPool[WOOD]++;
            resourceCardPool[BRICK]++;
        }
    }

    public void buildSettlement(byte vertexId) throws IOException {
        if (!canBuildSettlement(vertexId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        Vertex v = vertices[vertexId];
        p.buySettlement(!inSettlementPhase());
        v.setPlayerId(playerTurn);
        v.setBuilding(STATUS_SETTLEMENT);
        if (v.getPort() != UNASSIGNED_PORT) {
            p.addPort(v.getPort());
        }
        if (!inSettlementPhase()) {
            resourceCardPool[WOOD]++;
            resourceCardPool[BRICK]++;
            resourceCardPool[SHEEP]++;
            resourceCardPool[HAY]++;
        }
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(SETTLEMENT_COMMAND);
            dos.writeByte(vertexId);
        }
    }

    public void buildCity(byte vertexId) throws IOException {
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
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(CITY_COMMAND);
            dos.writeByte(vertexId);
        }
    }

    public byte playRobber(byte tileId, byte playerIdSteal) throws IOException {
        if (!canPlayRobber(tileId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        robberTile = tileId;
        byte type = players[playerIdSteal].stealResource(r);
        if (type != INVALID_RESOURCE) {
            p.addResource(type, (byte) 1);
        }
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(ROBBER_COMMAND);
            dos.writeByte(tileId);
            dos.writeByte(playerIdSteal);
        }
        return type;
    }

    public byte buyDevCard() throws IOException {
        if (!canBuyDevCard()) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        byte type = getRandomSlot(devCardPool, r);
        p.buyDevCard(type);
        if (type != VICTORY) {
            devCardsAcquiredThisTurn[type]++;
        }
        resourceCardPool[SHEEP]++;
        resourceCardPool[HAY]++;
        resourceCardPool[ROCK]++;
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(DEV_CARD_COMMAND);
        }
        return type;
    }

    public byte playKnightCard(byte tileId, byte playerIdSteal) throws IOException {
        if (!canPlayKnightCard(tileId)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(KNIGHT);
        updateLargestArmy();
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(KNIGHT_COMMAND);
        }
        return playRobber(tileId, playerIdSteal);
    }

    public void playRoadBuilding(byte e1, byte e2) throws IOException {
        if (!canPlayRoadBuilding(e1, e2)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(ROAD_BUILDING);
        buildRoadHelper(e1, false);
        buildRoadHelper(e1, false);
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(ROAD_BUILDING_COMMAND);
            dos.writeByte(e1);
            dos.writeByte(e2);
        }
    }

    /*
     * Currently returns total amount stolen. For proper info we should return the amount stolen from each player as
     * well.
     */
    public byte playMonopoly(byte resourceType) throws IOException {
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
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(MONOPOLY_COMMAND);
            dos.writeByte(resourceType);
        }
        return totalStolen;
    }

    public void playYearOfPlenty(byte r1, byte r2) throws IOException {
        if (!canPlayYearOfPlenty(r1, r2)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        p.playDevCard(YEAR_OF_PLENTY);
        p.addResource(r1, (byte) 1);
        p.addResource(r2, (byte) 1);
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(YEAR_OF_PLENTY_COMMAND);
            dos.writeByte(r1);
            dos.writeByte(r2);
        }
    }

    public void tradeBank(byte playerResource, byte bankResource) throws IOException {
        getCurrentPlayer().tradeBank(playerResource, bankResource);
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(TRADE_BANK_COMMAND);
            dos.writeByte(playerResource);
            dos.writeByte(bankResource);
        }
    }

    public void tradePlayer(byte playerId, byte[] giving, byte[] receiving) throws IOException {
        if(!canTradePlayer(playerTurn, giving) || !canTradePlayer(playerId, receiving)){
            throw new RuntimeException("Invalid Transaction");
        }
        getCurrentPlayer().tradePlayer(giving, receiving);
        players[playerId].tradePlayer(receiving, giving);
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(TRADE_PLAYER_COMMAND);
            dos.writeByte(playerId);
            config.writeByteArray(giving);
            config.writeByteArray(receiving);
        }
    }

    public byte advanceTurn() throws IOException {
        if (computeVictoryPoints(playerTurn) >= VICTORY_POINTS_REQ_WIN) {
            return WIN_CONDITION;
        }
        Arrays.fill(devCardsAcquiredThisTurn, (byte) 0);
        playerTurn++;
        if (playerTurn == players.length) {
            playerTurn = 0;
        }
        turnNumber++;
        if(config.isLoggerActive()){
            DataOutputStream dos = config.getLogger();
            dos.writeByte(ADVANCE_TURN_COMMAND);
        }
        return rollDice();
    }

    private byte rollDice() {
        if(inSettlementPhase()){
            return NO_DICE_ROLL;
        }
        byte roll = (byte) (r.nextInt(6) + r.nextInt(6) + 2);
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
            maxRoadLength += transverseVertex(seenEdges, n, maxRoadLength);
        }
        if (maxRoadLength > currentLongestRoad) {
            currentLongestRoad = maxRoadLength;
            playerWithLongestRoad = playerTurn;
        }
    }

    private byte transverseVertex(HashSet<Byte> seenEdges, byte vertexId, byte currentRoadLength) {
        if (vertices[vertexId].getPlayerId() != playerTurn) {
            return currentRoadLength;
        }
        byte maxRoadLength = currentRoadLength;
        byte[] outgoingEdges = vertexToEdge[vertexId];
        for (byte edgeId : outgoingEdges) {
            if (seenEdges.contains(edgeId)) {
                continue;
            }
            if (edges[edgeId] == playerTurn) {
                byte nextNodeId = -1;
                for (byte n : edgeToVertex[edgeId]) {
                    if (n != vertexId) {
                        nextNodeId = n;
                    }
                }
                if (nextNodeId == -1) {
                    throw new RuntimeException("Next Node not found");
                }
                byte roadLength = transverseVertex(seenEdges, nextNodeId, ++currentRoadLength);
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
