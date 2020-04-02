package com.SpringField.engine;

import com.SpringField.engine.board.Player;
import com.SpringField.engine.board.Vertex;
import com.SpringField.engine.util.BoardStateConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import static com.SpringField.engine.util.Util.*;

public class BoardStateAI extends BoardState {
    private byte[][] playerRoadCache;
    private byte[][] playerSettlementCache;

    private BoardStateAI() {
    }

    @Override
    protected void initialize(int numPlayers) {
        super.initialize(numPlayers);
        playerRoadCache = new byte[numPlayers][];
        playerSettlementCache = new byte[numPlayers][];
        for (int i = 0; i < numPlayers; i++) {
            playerRoadCache[i] = new byte[DEFAULT_ROAD_COUNT];
            for (int j = 0; j < DEFAULT_ROAD_COUNT; j++) {
                playerRoadCache[i][j] = UNASSIGNED_EDGE;
            }
            playerSettlementCache[i] = new byte[DEFAULT_SETTLEMENT_COUNT];
            for (int j = 0; j < DEFAULT_SETTLEMENT_COUNT; j++) {
                playerSettlementCache[i][j] = UNASSIGNED_VERTEX;
            }
        }
    }

    private byte[] getCurrentRoadCache() {
        return playerRoadCache[playerTurn];
    }

    private byte[] getCurrentSettlementCache() {
        return playerSettlementCache[playerTurn];
    }

    @Override
    public void buildRoadHelper(byte edgeId, boolean pay) {
        super.buildRoadHelper(edgeId, pay);
        for (int i = 0; i < playerRoadCache[playerTurn].length; i++) {
            if (playerRoadCache[playerTurn][i] == UNASSIGNED_EDGE) {
                playerRoadCache[playerTurn][i] = edgeId;
                break;
            }
        }
    }

    @Override
    public void buildSettlement(byte vertexId) throws IOException {
        super.buildSettlement(vertexId);
        for (int i = 0; i < playerSettlementCache[playerTurn].length; i++) {
            if (playerSettlementCache[playerTurn][i] == UNASSIGNED_VERTEX) {
                playerSettlementCache[playerTurn][i] = vertexId;
                break;
            }
        }
    }

    @Override
    public void buildCity(byte vertexId) throws IOException {
        super.buildCity(vertexId);
        int swapIndex = -1;
        byte[] settlementCache = playerSettlementCache[playerTurn];
        for (int i = 0; i < settlementCache.length; i++) {
            if (settlementCache[i] == UNASSIGNED_VERTEX) {
                settlementCache[swapIndex] = settlementCache[i - 1];
                settlementCache[i - 1] = UNASSIGNED_VERTEX;
            }
            if (settlementCache[i] == vertexId) {
                settlementCache[i] = UNASSIGNED_VERTEX;
                swapIndex = i;
            }
        }
    }

    public HashSet<BoardStateAI> getAllPossibleMoves() throws IOException {
        HashSet<BoardStateAI> resultSet = new HashSet<>();
        resultSet.add(this);
        for (byte type = GENERATE_DEV_KNIGHT; type <= GENERATE_BUY_DEV_CARD; type++) {
            for (BoardStateAI b : resultSet) {
                allPossibleStatesGenerator(b, resultSet, type);
            }
        }
        for (BoardStateAI b : resultSet) {
            b.advanceTurn();
        }
        return resultSet;
    }

    private void allPossibleStatesGenerator(BoardStateAI state, HashSet<BoardStateAI> resultSet, byte type)
            throws IOException {
        HashSet<BoardStateAI> batch = new HashSet<>();
        HashSet<BoardStateAI> newBatch = new HashSet<>();
        ArrayList<BoardStateAI> deleteSet = new ArrayList<>();
        allPossibleStatesGeneratorRouter(state, batch, type);
        while (true) {
            if (batch.isEmpty()) {
                return;
            }
            for (BoardStateAI b : batch) {
                if (resultSet.contains(b)) {
                    deleteSet.add(b);
                } else {
                    resultSet.add(b);
                }
            }
            for (BoardStateAI b : deleteSet) {
                batch.remove(b);
                // TODO Recycle b
            }
            deleteSet.clear();
            for (BoardStateAI b : batch) {
                allPossibleStatesGeneratorRouter(b, newBatch, type);
            }
            batch.addAll(newBatch);
            newBatch.clear();
        }
    }

    private void allPossibleStatesGeneratorRouter(BoardStateAI state, HashSet<BoardStateAI> states, byte type)
            throws IOException {
        switch (type) {
        case GENERATE_DEV_KNIGHT:
            break;
        case GENERATE_PLAYER_TRADES:
            break;
        case GENERATE_DEV_MONOPOLY:
            allPossibleMonopoly(state, states);
            break;
        case GENERATE_DEV_YEAR_OF_PLENTY:
            allPossibleYearOfPlenty(state, states);
            break;
        case GENERATE_BANK_TRADES:
            allPossibleBankTrades(state, states);
            break;
        case GENERATE_DEV_ROAD_BUILDING:
            allPossibleRoadBuilding(state, states);
            break;
        case GENERATE_BUILD_ROAD:
            allPossibleBuildRoad(state, states, true);
            break;
        case GENERATE_BUILD_SETTLEMENT:
            allPossibleBuildSettlement(state, states);
            break;
        case GENERATE_BUILD_CITY:
            allPossibleBuildCity(state, states);
            break;
        case GENERATE_BUY_DEV_CARD:
            allPossibleBuyDevCard(state, states);
            break;
        default:
            throw new RuntimeException("Invalid State Space Generation Type");
        }
    }

    private void allPossibleMonopoly(BoardStateAI state, HashSet<BoardStateAI> states) throws IOException {
        if (!state.canPlayMonopoly()) {
            return;
        }
        Player p = state.players[state.getPlayerTurn()];
        for (byte r = WOOD; r <= ROCK; r++) {
            if (p.getResources()[r] + state.getResourceCardPool()[r] < DEFAULT_RESOURCE_COUNT) {
                BoardStateAI b = state.clone();
                b.playMonopoly(r);
                addStateToSet(b, states);
            }
        }
    }

    private void allPossibleYearOfPlenty(BoardStateAI state, HashSet<BoardStateAI> states) throws IOException {
        if (!state.canPlayDevCard(YEAR_OF_PLENTY)) {
            return;
        }
        for (byte r1 = WOOD; r1 <= ROCK; r1++) {
            for (byte r2 = WOOD; r2 <= ROCK; r2++) {
                if (canPlayYearOfPlenty(r1, r2)) {
                    BoardStateAI b = state.clone();
                    b.playYearOfPlenty(r1, r2);
                    addStateToSet(b, states);
                }
            }
        }
    }

    private void allPossibleBankTrades(BoardStateAI state, HashSet<BoardStateAI> states) throws IOException {
        for (byte playerResource = WOOD; playerResource <= ROCK; playerResource++) {
            for (byte bankResource = WOOD; bankResource <= ROCK; bankResource++) {
                if (playerResource == bankResource) {
                    continue;
                }
                if (state.canTradeBank(playerResource, bankResource)) {
                    BoardStateAI b = state.clone();
                    b.tradeBank(playerResource, bankResource);
                    addStateToSet(b, states);
                }
            }
        }
    }

    private void allPossibleRoadBuilding(BoardStateAI state, HashSet<BoardStateAI> states) {
        if (!state.canPlayDevCard(ROAD_BUILDING)) {
            return;
        }
        HashSet<BoardStateAI> firstRoadSet = new HashSet<>();
        allPossibleBuildRoad(state, firstRoadSet, false);
        for (BoardStateAI firstRoad : firstRoadSet) {
            allPossibleBuildRoad(firstRoad, states, false);
        }
        // TODO Recycle firstRoadSet
    }

    private void allPossibleBuildRoad(BoardStateAI state, HashSet<BoardStateAI> states, boolean pay) {
        if (pay && !state.getCurrentPlayer().canBuyRoad()) {
            return;
        }
        for (byte e : state.getCurrentRoadCache()) {
            for (byte v : edgeToVertex[e]) {
                if (state.vertices[v].getPlayerId() != state.playerTurn) {
                    continue;
                }
                for (byte possibleRoad : vertexToEdge[v]) {
                    if (state.edges[possibleRoad] == UNASSIGNED_EDGE) {
                        BoardStateAI b = state.clone();
                        b.buildRoadHelper(possibleRoad, pay);
                        addStateToSet(b, states);
                    }
                }
            }
        }
    }

    /*
     * Todo Technically this can be optimized because it obviously double checks right now
     */
    private void allPossibleBuildSettlement(BoardStateAI state, HashSet<BoardStateAI> states) throws IOException {
        if (!state.getCurrentPlayer().canBuySettlement()) {
            return;
        }
        for (byte e : state.getCurrentRoadCache()) {
            for (byte v : edgeToVertex[e]) {
                if (!state.vertices[v].isSettled()) {
                    BoardStateAI b = state.clone();
                    b.buildSettlement(v);
                    addStateToSet(b, states);
                }
            }
        }
    }

    private void allPossibleBuildCity(BoardStateAI state, HashSet<BoardStateAI> states) throws IOException {
        if (!state.getCurrentPlayer().canBuyCity()) {
            return;
        }
        for (byte v : state.getCurrentSettlementCache()) {
            BoardStateAI b = state.clone();
            b.buildCity(v);
            addStateToSet(b, states);
        }
    }

    private void allPossibleBuyDevCard(BoardStateAI state, HashSet<BoardStateAI> states) throws IOException {
        if (state.canBuyDevCard()) {
            return;
        }
        BoardStateAI b = state.clone();
        b.buyDevCard();
        addStateToSet(b, states);
    }

    private void addStateToSet(BoardStateAI state, HashSet<BoardStateAI> states) {
        if (!states.contains(state)) {
            states.add(state);
        } else {
            // TODO Recycle
        }
    }

    public BoardStateAI clone() {
        BoardStateAI b = new BoardStateAI();
        b.config = config;
        b.vertices = new Vertex[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            b.vertices[i] = vertices[i].clone();
        }
        b.edges = edges.clone();
        b.players = new Player[b.players.length];
        for (int i = 0; i < players.length; i++) {
            b.players[i] = players[i].clone();
        }
        b.resourceCardPool = resourceCardPool.clone();
        b.devCardPool = devCardPool.clone();
        b.devCardsAcquiredThisTurn = devCardsAcquiredThisTurn.clone();
        b.playerWithLargestArmy = playerWithLargestArmy;
        b.playerWithLongestRoad = playerWithLongestRoad;
        b.currentLongestRoad = currentLongestRoad;
        b.playerTurn = playerTurn;
        b.robberTile = robberTile;
        b.turnNumber = turnNumber;
        b.playerRoadCache = playerRoadCache.clone();
        b.playerSettlementCache = playerSettlementCache.clone();
        return b;
    }
}
