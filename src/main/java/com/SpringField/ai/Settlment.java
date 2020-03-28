package com.SpringField.ai;

import com.SpringField.engine.BoardState;
import java.util.ArrayList;

public class Settlment {
    private BoardState state;
    private byte playerId;

    public Settlment(BoardState state) {
        this.state = state;
        this.playerId = state.getPlayerTurn();
    }

    /*
     * Magic!
     */
    public BoardState getBestPossibleSettle() {
        return null;
    }

    private ArrayList<BoardState> getAllPossibleSettles(BoardState b) {
        return b.getAllPossibleMoves();
    }
}
