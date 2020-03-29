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
     * Resource Value - Resource num compared to resource abundance
     * Number Value - Purely odds of Landing
     * Current Resource Coverage weighting (probably a later implement)
     */
    public byte getBestPossibleSettle(BoardState b) {
        // Setup our best settle and our possibilities vector
        byte best = 0;
        ArrayList<Byte> possibleSettles   = getAllPossibleSettles(b); // ArrayList for vertexes that are settle-able
        ArrayList<Byte> resourceAbundance = getResourceAbundances();  // Total dot count of each resource

        // Settle randomly for now
        Random r =  new Random();
        best = possibleSettles.get(r.nextInt(possibleSettles.size()));
        return best;
    }

    /*
     * Get an array of each resources total "dot" count
     */
    private ArrayList<Byte> getResourceAbundances(){
        // Create byte arraylist to hold abundances
        ArrayList<Byte> abundance;
        abundance = (ArrayList<Byte>) Arrays.asList((byte) 0, (byte) 0,(byte) 0, (byte) 0, (byte) 0); // fuck this line

        // Loop to calculate each resources abundance
        for (byte i = 0 ; i < 5; i++){
            byte counter = 0;
            for (byte b : tilesResource){
                if (b == i){
                    abundance.set(i, (byte)(abundance.get(i)+ getProbability(tilesNumber[counter])));
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
        ArrayList<Vertex> vertexes = b.getVertices();
        ArrayList<Byte> ret_vector = new ArrayList<Byte>();

        // Find out how long it is
        int length = vertexes.size();

        // For loop to check over data
        for (int i = 0; i < length; i++) {
            if (b.canSettle((byte)i)) {
                ret_vector.add((byte) i); // Append if its unassigned
            }
        }
        return ret_vector;
    }

    /*
     * Literally assigning the "dot" values
     */
    private byte getProbability(byte b){
        // Bunch of if statements to get # of "dots"
        if (b == 2 || b==12){
            return (byte) 1;
        }
        else if (b == 3 || b==11){
            return (byte) 2;
        }
        else if (b == 4 || b==10){
            return (byte) 3;
        }
        else if (b == 5 || b==9){
            return (byte) 4;
        }
        else if (b == 6 || b==8){
            return (byte) 5;
        }
        else{
            return (byte) 0;
        }
    }
}
