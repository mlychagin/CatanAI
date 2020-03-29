package com.SpringField.ai;

import com.SpringField.engine.BoardState;
import com.SpringField.engine.board.Vertex;
import com.SpringField.engine.util.Util;
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

    /*
     * Return all possible settleable vertexes. nice extrapolation for data manipulation later down the road
     */
    private ArrayList<Byte> getAllPossibleSettles(BoardState b) {
        // Initialize array holders
        ArrayList<Vertex> vertexes = b.getVertices();
        ArrayList<Byte> ret_vector = new ArrayList<Byte>();

        // Find out how long it is
        int length = vertexes.size();

        // For loop to check over data
        for (int i = 0; i < length; i++) {
            if (!(vertexes.get(i).isAssigned())) {
                ret_vector.add((byte) i); // Append if its unassigned
            }
        }
        return ret_vector;
    }
}
