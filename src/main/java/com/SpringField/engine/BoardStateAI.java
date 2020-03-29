package com.SpringField.engine;

import com.SpringField.engine.board.Player;
import com.SpringField.engine.board.Vertex;

import java.util.ArrayList;
import java.util.HashSet;

import static com.SpringField.engine.util.Util.*;

public class BoardStateAI extends BoardState {

    private BoardStateAI() {

    }

    BoardStateAI(int numPlayers) {
        super(numPlayers);
    }

    private boolean moveAvailable() {
        return false;
    }

    /*
     * 1. Knight Card 2. Player Trades 3. Monopoly / Year of Plenty 4. Bank Trades 5. Road Building (Dev Card) 6. Build
     * Road 7. Build Settlement 8. Build City 9. Buy Dev Card
     */
    public HashSet<BoardStateAI> getAllPossibleMoves() {
        HashSet<BoardStateAI> resultSet = new HashSet<>();
        resultSet.add(this);
        for (byte type = GENERATE_DEV_KNIGHT; type <= GENERATE_BUY_DEV_CARD; type++) {
            for (BoardStateAI b : resultSet) {
                allPossibleStatesGenerator(b, resultSet, type);
            }
        }
        return resultSet;
    }

    private void allPossibleStatesGenerator(BoardStateAI state, HashSet<BoardStateAI> resultSet, byte type) {
        HashSet<BoardStateAI> batch = new HashSet<>();
        HashSet<BoardStateAI> newBatch = new HashSet<>();
        allPossibleStatesGeneratorRouter(state, batch, type);
        while (true) {
            if (batch.isEmpty()) {
                return;
            }
            ArrayList<BoardStateAI> deleteSet = new ArrayList<>();
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
            for (BoardStateAI b : batch) {
                allPossibleStatesGeneratorRouter(b, newBatch, type);
            }
            batch.addAll(newBatch);
            newBatch.clear();
        }
    }

    private void allPossibleStatesGeneratorRouter(BoardStateAI state, HashSet<BoardStateAI> states, byte type) {
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
            break;
        case GENERATE_BUILD_ROAD:
            break;
        case GENERATE_BUILD_SETTLEMENT:
            break;
        case GENERATE_BUILD_CITY:
            break;
        case GENERATE_BUY_DEV_CARD:
            break;
        default:
            throw new RuntimeException("Invalid State Space Generation Type");
        }
    }

    private void allPossibleMonopoly(BoardStateAI state, HashSet<BoardStateAI> states) {
        if (!state.canPlayMonopoly()) {
            return;
        }
        Player p = state.players[state.getPlayerTurn()];
        for (byte r = WOOD; r <= ROCK; r++) {
            if (p.getResources()[r] + state.getResourceCardPool()[r] < DEFAULT_RESOURCE_COUNT) {
                BoardStateAI b = state.clone();
                b.playMonopoly(r);
                if (!states.contains(b)) {
                    states.add(b);
                } else {
                    // TODO Recycle
                }
            }
        }
    }

    private void allPossibleYearOfPlenty(BoardStateAI state, HashSet<BoardStateAI> states) {
        if (!state.canPlayDevCard(YEAR_OF_PLENTY)) {
            return;
        }
        for (byte r1 = WOOD; r1 <= ROCK; r1++) {
            for (byte r2 = WOOD; r2 <= ROCK; r2++) {
                if (canPlayYearOfPlenty(r1, r2)) {
                    BoardStateAI b = state.clone();
                    b.playYearOfPlenty(r1, r2);
                    if (!states.contains(b)) {
                        states.add(b);
                    } else {
                        // TODO Recycle
                    }
                }
            }
        }
    }

    private void allPossibleBankTrades(BoardStateAI state, HashSet<BoardStateAI> states) {
        for (byte playerResource = WOOD; playerResource <= ROCK; playerResource++) {
            for (byte bankResource = WOOD; bankResource <= ROCK; bankResource++) {
                if (playerResource == bankResource) {
                    continue;
                }
                if (state.canTradeBank(playerResource, bankResource)) {
                    BoardStateAI b = state.clone();
                    b.tradeBank(playerResource, bankResource);
                    if (!states.contains(b)) {
                        states.add(b);
                    } else {
                        // TODO Recycle
                    }
                }
            }
        }
    }

    private void allPossibleRoadBuilding(BoardStateAI state, HashSet<BoardStateAI> states) {
        if (!state.canPlayDevCard(ROAD_BUILDING)) {
            return;
        }
    }

    private void allPossibleBuildRoad(BoardStateAI state, HashSet<BoardStateAI> states) {
        if (!state.players[state.playerTurn].canBuyRoad()) {
            return;
        }
    }

    private void allPossibleBuildSettlement(BoardStateAI state, HashSet<BoardStateAI> states) {
        if (!state.players[state.playerTurn].canBuySettlement()) {
            return;
        }
    }

    private void allPossibleBuildCity(BoardStateAI state, HashSet<BoardStateAI> states) {
        if (!state.players[state.playerTurn].canBuyCity()) {
            return;
        }
    }

    private void allPossibleBuyDevCard(BoardStateAI state, HashSet<BoardStateAI> states) {
        if (state.canBuyDevCard()) {
            return;
        }
    }

    public BoardStateAI clone() {
        BoardStateAI b = new BoardStateAI();
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
        return b;
    }
}
