package com.SpringField.engine.ai;

import com.SpringField.ai.Settlement;
import com.SpringField.engine.board.Player;
import com.SpringField.engine.BoardState;
import com.SpringField.engine.board.Vertex;
import com.SpringField.engine.util.BoardStateConfig;
import org.junit.Test;

import java.io.*;
import java.util.Random;

import static com.SpringField.engine.util.Util.*;
import com.SpringField.engine.util.Util;

public class SettleTest {
    private static BoardStateConfig defaultConfig;

    static {
        try {
            defaultConfig = new BoardStateConfig(null, 4, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // Quick bulk testing to get us off the ground
        // Quick I/O to link with a csv file
        String pathToCsv = "";
        FileWriter csvWriter;
        try {
            pathToCsv = "src\\test\\java\\com\\SpringField\\engine\\ai\\output.txt";
            csvWriter = new FileWriter(pathToCsv);
        } catch (IOException e) {
            throw new IOException("Could not locate: " + pathToCsv);
        }

        // Start the timer
        long startTime = System.nanoTime();
        Random r = new Random();

        // 1000 iterations of a board state being settled
        int i = 0;
        while (i < 1) {
            String settles = "";
            BoardState test = new BoardState(new BoardStateConfig(null, 4, r.nextLong()), 4);
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

            i++;

            // loop to print out pertinent data
            String[] arr = settles.split(" ");
            for (int j = 0;; j++) {
                if ((j + 1) == 8) {
                    csvWriter.write(arr[j] + "\n");
                    break;
                }
                csvWriter.write(arr[j] + ", ");

            }
            csvWriter.flush();
        }

        csvWriter.close();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        BoardState test = new BoardState(defaultConfig, 4);
        System.out.println(test.getVertices());
        System.out.println(
                "\nExecuted and settled " + i + " boardstates in: " + (duration * (1.0) / 1000000000) + " second(s)!");
    }

    @Test
    public void canInitBoardstate() {
        BoardState test = new BoardState(defaultConfig, 4);

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

        for (byte i = 0; i < 8; i++) {
            byte next_settle = chooser.getBestPossibleSettle();
            try {
                test.buildSettlement(next_settle);
            } catch (RuntimeException | IOException e) {
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
