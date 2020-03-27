package com.SpringField.ai;

import com.SpringField.engine.BoardState;

import java.util.ArrayList;

public class AlphaBetaPruning {
    private BoardState state;
    private byte playerId;

    public AlphaBetaPruning(BoardState state) {
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
    private ArrayList<BoardState> getAllPossibleMoves(BoardState b) {
        return b.getAllPossibleMoves();
    }
}
