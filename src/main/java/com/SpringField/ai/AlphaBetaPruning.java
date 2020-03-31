package com.SpringField.ai;

import com.SpringField.engine.BoardState;
import com.SpringField.engine.BoardStateAI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class AlphaBetaPruning {
    private BoardStateAI state;
    private byte playerId;

    public AlphaBetaPruning(BoardStateAI state) {
        this.state = state;
        this.playerId = state.getPlayerTurn();
    }

    /*
     * Magic?
     */
    public BoardState getBestPossiblyMove() {
        return null;
    }

    /*
     * Simple Cost function for now, we can improve later on
     */
    private byte costFunction(BoardState b, byte playerId) {
        return b.computeVictoryPoints(playerId);
    }

    /*
     * Get all possible moves from this position You can use this method or the API directly I'm writing this method so
     * you know where to look
     */
    private HashSet<BoardStateAI> getAllPossibleMoves(BoardStateAI b) throws IOException {
        return b.getAllPossibleMoves();
    }
}
