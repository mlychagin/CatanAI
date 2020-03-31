package com.SpringField.engine.ai;

import com.SpringField.ai.Settlement;
import com.SpringField.engine.board.Player;
import com.SpringField.engine.BoardState;
import com.SpringField.engine.board.Vertex;
import org.junit.Test;

import java.io.*;
import java.util.Random;

import static com.SpringField.engine.util.Util.*;
import static com.SpringField.engine.util.Util.KNIGHT;

public class settleTest {
    public static void main(String[] args) throws IOException{
        // Quick bulk testing to get us off the ground
        // Quick I/O to link with a csv file
        String pathToCsv ="";
        FileWriter csvWriter;
        try {
            pathToCsv = "output.txt";
            csvWriter = new FileWriter(pathToCsv);
        } catch (IOException e){
            throw new IOException("Could not locate: " + pathToCsv);
        }

        // Start the timer
        long startTime = System.nanoTime();

        // 1000 iterations of a board state being settled
        int i = 0;
        while (i < 100000) {
            String settles = "";
            BoardState test = new BoardState(4);
            Settlement chooser = new Settlement(test);
            for (byte n = 0; n < 8; n++) {
                byte next_settle = chooser.getBestPossibleSettle();
                settles = settles + next_settle + " ";
                try {
                    test.buildSettlement(next_settle);
                } catch (RuntimeException e) {
                    throw new RuntimeException("Could not build settlement");
                }
            }

            // Reset the bnoard and forward the counters
            initializeTiles(new Random());
            i++;

            // loop to print out pertinent data
            String[] arr = settles.split(" ");
            for (int j = 0; j < 7;j++) {
                //if ((j + 1) == 7) {
                csvWriter.write(arr[j]);
                //}
            }
            csvWriter.flush();
        }

        csvWriter.close();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("\nExecuted and settled " + i + " boardstates in: " + (duration*(1.0)/1000000000) + " second(s)!");
    }


    @Test
    public void canInitBoardstate() {
        BoardState test = new BoardState(4);

        // vertices
        Vertex[] vertexes = test.getVertices();
        for (Vertex v : vertexes) {
            System.out.println(v);
        }

        // edges
        byte[] edges = test.getEdges();
        for (byte e : edges) {
            System.out.println(e);
        }

        // players
        Player[] players = test.getPlayers();
        for (Player p : players) {
            System.out.println(p);
        }

        Settlement chooser = new Settlement(test);

        for (byte i = 0; i<8;i++) {
            byte next_settle = chooser.getBestPossibleSettle();
            try {
                test.buildSettlement(next_settle);
            }
            catch (RuntimeException | IOException e){
                throw new RuntimeException("could not build settlement");
            }
        }

        // vertices
        Vertex[] after = test.getVertices();
        for (Vertex v : after) {
            System.out.println(v);
        }

        // TODO:
        // resourceCardPool
        // devCardPool
        // playerWithLargestArmy
        // playerWithLongestRoad
        // currentLongestRoad
        // playerTurn
    }

}
