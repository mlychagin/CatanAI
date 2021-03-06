package com.SpringField.engine;

import com.SpringField.engine.board.Player;
import com.SpringField.engine.board.Vertex;
import com.SpringField.engine.util.BoardStateConfig;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static com.SpringField.engine.util.Util.*;

public class BoardState {
    protected BoardStateConfig config;
    protected Player[] players;
    protected Vertex[] vertices;
    protected byte[] edges;
    protected byte[] resourceCardPool;
    protected byte[] devCardPool;
    protected byte[] devCardsAcquiredThisTurn;
    protected byte[] longestRoads;
    protected byte playerWithLargestArmy;
    protected byte playerWithLongestRoad;
    protected byte currentLongestRoad;
    protected byte playerTurn;
    protected byte robberTile;
    protected byte turnNumber;
    protected Random r;

    /*
     * Longest Road Helpers
     */
    private HashSet<Byte> seenEdges = new HashSet<>();

    protected BoardState() {
    }

    public BoardState(int numPlayers) throws IOException {
        r = new Random();
        this.config = new BoardStateConfig(null, numPlayers, r.nextInt());
        initialize(numPlayers);
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

    private BoardState(BoardStateConfig config, Player[] players, Vertex[] vertices, byte[] edges,
            byte[] resourceCardPool, byte[] devCardPool, byte[] devCardsAcquiredThisTurn, byte playerWithLargestArmy,
            byte playerWithLongestRoad, byte currentLongestRoad, byte playerTurn, byte robberTile, byte turnNumber) {
        this.config = config;
        this.players = players;
        this.vertices = vertices;
        this.edges = edges;
        this.resourceCardPool = resourceCardPool;
        this.devCardPool = devCardPool;
        this.devCardsAcquiredThisTurn = devCardsAcquiredThisTurn;
        this.playerWithLargestArmy = playerWithLargestArmy;
        this.playerWithLongestRoad = playerWithLongestRoad;
        this.currentLongestRoad = currentLongestRoad;
        this.playerTurn = playerTurn;
        this.robberTile = robberTile;
        this.turnNumber = turnNumber;
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

    public BoardStateConfig getConfig() {
        return config;
    }

    public byte[] getResourceCardPool() {
        return resourceCardPool;
    }

    public byte getPlayerTurn() {
        return playerTurn;
    }

    public byte getRobberTile() {
        return robberTile;
    }

    public byte getPlayerWithLargestArmy() {
        return playerWithLargestArmy;
    }

    public byte getPlayerWithLongestRoad() {
        return playerWithLongestRoad;
    }

    public byte getCurrentLongestRoad() {
        return currentLongestRoad;
    }

    public byte getTurnNumber() {
        return turnNumber;
    }

    public Player getCurrentPlayer() {
        return players[playerTurn];
    }

    protected void initialize(int numPlayers) {
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
        }
        vertices = new Vertex[DEFAULT_NUM_VERTICES];
        for (int i = 0; i < DEFAULT_NUM_VERTICES; i++) {
            vertices[i] = new Vertex(vertexToPort[i]);
        }
        edges = new byte[DEFAULT_NUM_EDGES];
        for (int i = 0; i < DEFAULT_NUM_EDGES; i++) {
            edges[i] = UNASSIGNED_PLAYER;
        }
        for (int i = 0; i < config.getTilesResource().length; i++) {
            if (config.getTilesResource()[i] == DESERT) {
                robberTile = (byte) i;
                break;
            }
        }
        resourceCardPool = new byte[] { DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT,
                DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT, DEFAULT_RESOURCE_COUNT };
        devCardPool =
                new byte[] { DEFAULT_NUM_KNIGHT, DEFAULT_NUM_VICTORY, DEFAULT_NUM_ROAD_BUILDING, DEFAULT_NUM_MONOPOLY,
                        DEFAULT_NUM_YEAR_OF_PLENTY };
        devCardsAcquiredThisTurn = new byte[numPlayers];
        longestRoads = new byte[numPlayers];
        playerWithLargestArmy = UNASSIGNED_PLAYER;
        playerWithLongestRoad = UNASSIGNED_PLAYER;
        currentLongestRoad = 0;
        playerTurn = 0;
        turnNumber = 0;
    }

    public boolean inSettlementPhase() {
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

    private boolean canBuildRoadHelper(byte edgeId, byte ghostEdge) {
        if (edges[edgeId] != UNASSIGNED_EDGE) {
            return false;
        }
        for (byte v : edgeToVertex[edgeId]) {
            Vertex vertex = vertices[v];
            if (vertex.isSettled()) {
                if (vertex.getPlayerId() == playerTurn) {
                    return true;
                }
                continue;
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
        if (inSettlementPhase() || !getCurrentPlayer().canBuyCity()) {
            return false;
        }
        Vertex v = vertices[vertexId];
        if (v.getBuilding() != STATUS_SETTLEMENT) {
            return false;
        }
        return v.getPlayerId() == playerTurn;
    }

    public boolean canPlayRobber(byte tileId, byte playerIdSteal) {
        if (inSettlementPhase() || playerTurn == playerIdSteal) {
            return false;
        }
        boolean found = false;
        for (byte v : tileToVertex[tileId]) {
            if (vertices[v].getPlayerId() == playerIdSteal) {
                found = true;
            }
        }
        if (!found) {
            return false;
        }
        return robberTile != tileId;
    }

    public boolean canBuyDevCard() {
        if (inSettlementPhase() || numDevCardsAvailable() == 0) {
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
        if (inSettlementPhase()) {
            return false;
        }
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
        if (inSettlementPhase()) {
            return false;
        }
        return getCurrentPlayer().canTradeBank(playerResource) && resourceCardPool[bankResource] > 0;
    }

    public boolean canTradePlayer(byte playerId, byte[] giving) {
        if (inSettlementPhase()) {
            return false;
        }
        return players[playerId].canPlayerTrade(giving);
    }

    public void buildRoad(byte edgeId) throws IOException {
        buildRoadHelper(edgeId, !inSettlementPhase());
        if (config.isLoggerActive()) {
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
        byte roadLength = getLongestRoad(playerTurn, edgeId);
        if (roadLength > currentLongestRoad) {
            currentLongestRoad = roadLength;
            playerWithLongestRoad = playerTurn;
        }
        if (roadLength > longestRoads[playerTurn]) {
            longestRoads[playerTurn] = roadLength;
        }
        if (pay) {
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
        updateLargestRoadSettlement(vertexId);
        if (!inSettlementPhase()) {
            resourceCardPool[WOOD]++;
            resourceCardPool[BRICK]++;
            resourceCardPool[SHEEP]++;
            resourceCardPool[HAY]++;
        }
        if (config.isLoggerActive()) {
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
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(CITY_COMMAND);
            dos.writeByte(vertexId);
        }
    }

    public byte playRobber(byte tileId, byte playerIdSteal) throws IOException {
        if (!canPlayRobber(tileId, playerIdSteal)) {
            throw new RuntimeException("Invalid Transaction");
        }
        Player p = getCurrentPlayer();
        robberTile = tileId;
        byte type = players[playerIdSteal].stealResource(r);
        if (type != INVALID_RESOURCE) {
            p.addResource(type, (byte) 1);
        }
        if (config.isLoggerActive()) {
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
        devCardPool[type]--;
        if (type != VICTORY) {
            devCardsAcquiredThisTurn[type]++;
        }
        resourceCardPool[SHEEP]++;
        resourceCardPool[HAY]++;
        resourceCardPool[ROCK]++;
        if (config.isLoggerActive()) {
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
        if (config.isLoggerActive()) {
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
        if (config.isLoggerActive()) {
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
        if (config.isLoggerActive()) {
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
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(YEAR_OF_PLENTY_COMMAND);
            dos.writeByte(r1);
            dos.writeByte(r2);
        }
    }

    public void tradeBank(byte playerResource, byte bankResource) throws IOException {
        getCurrentPlayer().tradeBank(playerResource, bankResource);
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(TRADE_BANK_COMMAND);
            dos.writeByte(playerResource);
            dos.writeByte(bankResource);
        }
    }

    public void tradePlayer(byte playerId, byte[] giving, byte[] receiving) throws IOException {
        if (!canTradePlayer(playerTurn, giving) || !canTradePlayer(playerId, receiving)) {
            throw new RuntimeException("Invalid Transaction");
        }
        getCurrentPlayer().tradePlayer(giving, receiving);
        players[playerId].tradePlayer(receiving, giving);
        if (config.isLoggerActive()) {
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
        if (inSettlementPhase()) {
            if (turnNumber < players.length - 1) {
                playerTurn++;
            } else if (turnNumber > players.length - 1 && turnNumber < players.length * 2 - 1) {
                playerTurn--;
            }
        } else {
            playerTurn++;
            if (playerTurn == players.length) {
                playerTurn = 0;
            }
        }
        turnNumber++;
        if (config.isLoggerActive()) {
            DataOutputStream dos = config.getLogger();
            dos.writeByte(ADVANCE_TURN_COMMAND);
        }
        return rollDice();
    }

    private byte rollDice() {
        if (inSettlementPhase()) {
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
        if(playerWithLargestArmy == -1){
            if(players[playerTurn].getKnightsPlayed() == 3){
                playerWithLargestArmy = playerTurn;
            }
            return;
        }
        if (computeArmy(playerTurn) > computeArmy(playerWithLargestArmy)) {
            playerWithLargestArmy = playerTurn;
        }
    }

    /*
     * Longest Road Algorithm
     */
    private void updateLargestRoadSettlement(byte vertexId) {
        byte[] adjacentEdges = vertexToEdge[vertexId];
        if (adjacentEdges.length == 3) {
            if (adjacentEnemyRoads(adjacentEdges, (byte) 0, (byte) 1) || adjacentEnemyRoads(adjacentEdges, (byte) 0,
                    (byte) 2) || adjacentEnemyRoads(adjacentEdges, (byte) 1, (byte) 2)) {
                byte enemyPlayer = -1;
                for (byte edge : vertexToEdge[vertexId]) {
                    if (edges[edge] != playerTurn) {
                        enemyPlayer = edges[edge];
                        break;
                    }
                }
                byte longestRoad = 0;
                for (int i = 0; i < edges.length; i++) {
                    if (edges[i] == enemyPlayer) {
                        byte roadLength = getLongestRoad(enemyPlayer, (byte) i);
                        if (roadLength > longestRoad) {
                            longestRoad = roadLength;
                        }
                    }
                }
                longestRoads[enemyPlayer] = longestRoad;
                if (playerWithLongestRoad == enemyPlayer && longestRoad != currentLongestRoad) {
                    currentLongestRoad = longestRoad;
                    for (int i = 0; i < longestRoads.length; i++) {
                        if (longestRoads[i] > currentLongestRoad) {
                            currentLongestRoad = longestRoads[i];
                            playerWithLongestRoad = (byte) i;
                        }
                    }
                }
            }
        }
    }

    private boolean adjacentEnemyRoads(byte[] adjacentEdges, byte index1, byte index2) {
        byte e1Status = edges[adjacentEdges[index1]];
        return (e1Status != playerTurn && e1Status != UNASSIGNED_EDGE) && (e1Status == edges[adjacentEdges[index2]]);
    }

    private byte getLongestRoad(byte playerId, byte edgeId) {
        seenEdges.add(edgeId);
        byte longestRoad = 1;
        for (byte vertexId : edgeToVertex[edgeId]) {
            longestRoad += transverseVertex(playerId, vertexId, (byte) 0);
        }
        seenEdges.clear();
        return longestRoad;
    }

    private byte transverseVertex(byte playerId, byte vertexId, byte depth) {
        byte maxDepth = depth;
        Vertex v = vertices[vertexId];
        if (v.isSettled() && v.getPlayerId() != playerId) {
            return depth;
        }
        for (byte adjacentEdgeId : vertexToEdge[vertexId]) {
            if (edges[adjacentEdgeId] != playerId || seenEdges.contains(adjacentEdgeId)) {
                continue;
            }
            seenEdges.add(adjacentEdgeId);
            byte intermediateDepth = transverseEdge(playerId, adjacentEdgeId, (byte) (depth + 1));
            if (intermediateDepth > maxDepth) {
                maxDepth = intermediateDepth;
            }
        }
        return maxDepth;
    }

    private byte transverseEdge(byte playerId, byte edgeId, byte depth) {
        byte maxDepth = depth;
        for (byte vertexId : edgeToVertex[edgeId]) {
            byte intermediateDepth = transverseVertex(playerId, vertexId, depth);
            if (intermediateDepth > maxDepth) {
                maxDepth = intermediateDepth;
            }
        }
        return maxDepth;
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

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(bOutput);
        config.serialize(output);
        output.writeByte(players.length);
        for (Player p : players) {
            p.serialize(output);
        }
        output.writeByte(vertices.length);
        for (Vertex v : vertices) {
            v.serialize(output);
        }
        writeByteArray(output, edges);
        writeByteArray(output, resourceCardPool);
        writeByteArray(output, devCardPool);
        writeByteArray(output, devCardsAcquiredThisTurn);
        output.writeByte(playerWithLargestArmy);
        output.writeByte(playerWithLongestRoad);
        output.writeByte(currentLongestRoad);
        output.writeByte(playerTurn);
        output.writeByte(robberTile);
        output.writeByte(turnNumber);
        output.flush();
        output.close();
        byte[] result = bOutput.toByteArray();
        bOutput.close();
        return result;
    }

    public static BoardState deSerialize(ObjectInputStream input) throws IOException {
        BoardStateConfig config = BoardStateConfig.deSerialize(input);
        byte lengthPlayers = input.readByte();
        Player[] players = new Player[lengthPlayers];
        for (byte i = 0; i < lengthPlayers; i++) {
            players[i] = Player.deSerialize(input);
        }
        byte lengthVertices = input.readByte();
        Vertex[] vertices = new Vertex[lengthVertices];
        for (byte i = 0; i < lengthVertices; i++) {
            vertices[i] = Vertex.deSerialize(input);
        }
        byte[] edges = readByteArray(input);
        byte[] resourceCardPool = readByteArray(input);
        byte[] devCardPool = readByteArray(input);
        byte[] devCardsAcquiredThisTurn = readByteArray(input);
        byte playerWithLargestArmy = input.readByte();
        byte playerWithLongestRoad = input.readByte();
        byte currentLongestRoad = input.readByte();
        byte playerTurn = input.readByte();
        byte robberTile = input.readByte();
        byte turnNumber = input.readByte();
        return new BoardState(config, players, vertices, edges, resourceCardPool, devCardPool, devCardsAcquiredThisTurn,
                playerWithLargestArmy, playerWithLongestRoad, currentLongestRoad, playerTurn, robberTile, turnNumber);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BoardState that = (BoardState) o;

        if (playerWithLargestArmy != that.playerWithLargestArmy)
            return false;
        if (playerWithLongestRoad != that.playerWithLongestRoad)
            return false;
        if (currentLongestRoad != that.currentLongestRoad)
            return false;
        if (playerTurn != that.playerTurn)
            return false;
        if (robberTile != that.robberTile)
            return false;
        if (turnNumber != that.turnNumber)
            return false;
        if (!Arrays.deepEquals(players, that.players))
            return false;
        if (!Arrays.deepEquals(vertices, that.vertices))
            return false;
        if (!Arrays.equals(edges, that.edges))
            return false;
        if (!Arrays.equals(resourceCardPool, that.resourceCardPool))
            return false;
        if (!Arrays.equals(devCardPool, that.devCardPool))
            return false;
        return Arrays.equals(devCardsAcquiredThisTurn, that.devCardsAcquiredThisTurn);
    }

    @Override public int hashCode() {
        int result = Arrays.hashCode(players);
        result = 31 * result + Arrays.hashCode(vertices);
        result = 31 * result + Arrays.hashCode(edges);
        result = 31 * result + Arrays.hashCode(resourceCardPool);
        result = 31 * result + Arrays.hashCode(devCardPool);
        result = 31 * result + Arrays.hashCode(devCardsAcquiredThisTurn);
        result = 31 * result + (int) playerWithLargestArmy;
        result = 31 * result + (int) playerWithLongestRoad;
        result = 31 * result + (int) currentLongestRoad;
        result = 31 * result + (int) playerTurn;
        result = 31 * result + (int) robberTile;
        result = 31 * result + (int) turnNumber;
        return result;
    }
}
