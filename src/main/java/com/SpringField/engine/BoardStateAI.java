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
     * TODO Implement Knight Card TODO Implement Player Trading Order as follows: 1. Knight Card 2. Player Trades 3.
     * Monopoly / Year of Plenty 4. Bank Trades 5. Road Building / All Building
     */
    public ArrayList<BoardStateAI> getAllPossibleMoves() {
        return null;
    }

    private ArrayList<BoardStateAI> allPossibleBankTrades(BoardStateAI state) {
        HashSet<BoardStateAI> resultSet = new HashSet<>();
        HashSet<BoardStateAI> batch = allPossibleBankTradesHelper(state);
        while (true) {

        }
    }

    private HashSet<BoardStateAI> allPossibleBankTradesHelper(BoardStateAI state) {
        HashSet<BoardStateAI> states = new HashSet<>();
        for (byte playerResource = WOOD; playerResource <= ROCK; playerResource++) {
            for (byte bankResource = WOOD; bankResource <= ROCK; bankResource++) {
                if (playerResource == bankResource) {
                    continue;
                }
                BoardStateAI b = state.clone();
                try {
                    b.tradeBank(b.playerTurn, playerResource, bankResource);
                    if (!states.contains(b)) {
                        states.add(b);
                    } else {
                        // TODO Recycle BoardState
                    }
                } catch (Exception ignored) {
                    // TODO Recycle BoardState
                }
            }
        }
        return states;
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
        b.playerWithLargestArmy = playerWithLargestArmy;
        b.playerWithLongestRoad = playerWithLongestRoad;
        b.currentLongestRoad = currentLongestRoad;
        b.playerTurn = playerTurn;
        b.robberTile = robberTile;
        return b;
    }
}
