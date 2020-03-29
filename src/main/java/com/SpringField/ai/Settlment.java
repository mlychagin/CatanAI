package com.SpringField.ai;

import com.SpringField.engine.BoardState;
import com.SpringField.engine.board.Vertex;
import com.SpringField.engine.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.SpringField.engine.util.Util.tilesNumber;
import static com.SpringField.engine.util.Util.tilesResource;

public class Settlment {
    private BoardState state;
    private byte playerId;

    public Settlment(BoardState state) {
        this.state = state;
        this.playerId = state.getPlayerTurn();
    }

    /*
     * Information I want to calculate (purely Heuristics based for now):
     * Vertex Value - Purely odds of Landing
     * Diversity - Current Resource coverage (from player's perspective)
     * Scarcity - How abundant resource are
     * Distance to Port - Not important yet
     */
    public byte getBestPossibleSettle(BoardState b) {
        // Setup our best settle and our possibilities vector
        byte best = 0;
        ArrayList<Byte> possibleSettles = getAllPossibleSettles(b); // ArrayList for vertexes that are settle-able
        ArrayList<Byte> resourceAbundance = getResourceAbundances(); // Total dot count of each resource

        // For loop to traverse all possible settles for getVertex
        for (Byte v: possibleSettles){
            if (best < getVertexValue(v)){
                best = v;
            }
        }


        // Settle randomly for now
        Random r = new Random();
        best = possibleSettles.get(r.nextInt(possibleSettles.size()));
        return best;
    }

    public float getVertexValue(byte vertex){
        byte ret_val = 0;
        return ret_val;
    }

    /*
     * Get an array of each resources total "dot" count
     */
    private ArrayList<Byte> getResourceAbundances() {
        // Create byte arraylist to hold abundances
        ArrayList<Byte> abundance;
        abundance = (ArrayList<Byte>) Arrays.asList((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0); // fuck this line

        // Loop to calculate each resources abundance
        for (byte i = 0; i < 5; i++) {
            byte counter = 0;
            for (byte b : tilesResource) {
                if (b == i) {
                    abundance.set(i, (byte) (abundance.get(i) + getProbability(tilesNumber[counter])));
                }
                counter++;
            }
        }

        return abundance;
    }

    /*
     * Return all possible settleable vertexes. nice extrapolation for data manipulation later down the road
     */
    private ArrayList<Byte> getAllPossibleSettles(BoardState b) {
        // Initialize vertex array holders
        Vertex[] vertexes = b.getVertices();
        ArrayList<Byte> ret_vector = new ArrayList<Byte>();

        // Find out how long it is
        int length = vertexes.length;

        // For loop to check over data
        for (int i = 0; i < length; i++) {
            if (b.canSettle((byte) i)) {
                ret_vector.add((byte) i); // Append if its unassigned
            }
        }
        return ret_vector;
    }

    /*
     * Literally assigning the "dot" values
     */
    private byte getProbability(byte b) {
        // Bunch of if statements to get # of "dots"
        if (b == 2 || b == 12) {
            return (byte) 1;
        } else if (b == 3 || b == 11) {
            return (byte) 2;
        } else if (b == 4 || b == 10) {
            return (byte) 3;
        } else if (b == 5 || b == 9) {
            return (byte) 4;
        } else if (b == 6 || b == 8) {
            return (byte) 5;
        } else {
            return (byte) 0;
        }
    }
}
