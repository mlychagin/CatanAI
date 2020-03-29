package com.SpringField.engine.util;

import java.util.ArrayList;
import java.util.Random;

public class Util {
    /*
     * Materials
     */
    public static final byte INVALID_RESOURCE = -1;
    public static final byte WOOD = 0;
    public static final byte BRICK = 1;
    public static final byte SHEEP = 2;
    public static final byte HAY = 3;
    public static final byte ROCK = 4;
    public static final byte DESERT = 6;
    public static final byte ANY = 6;

    public static final byte DEFAULT_WOOD_COUNT = 19;
    public static final byte DEFAULT_BRICK_COUNT = 19;
    public static final byte DEFAULT_SHEEP_COUNT = 19;
    public static final byte DEFAULT_HAY_COUNT = 19;
    public static final byte DEFAULT_ROCK_COUNT = 19;
    public static final byte DEFAULT_DESERT_COUNT = 0;

    /*
     * Structures
     */
    public static final byte ROAD = 0;
    public static final byte SETTLEMENT = 1;
    public static final byte CITY = 2;

    public static final byte DEFAULT_ROAD_COUNT = 15;
    public static final byte DEFAULT_SETTLEMENT_COUNT = 5;
    public static final byte DEFAULT_CITY_COUNT = 4;

    /*
     * Dev Cards
     */
    public static final byte KNIGHT = 0;
    public static final byte VICTORY = 1;
    public static final byte ROAD_BUILDING = 2;
    public static final byte MONOPOLY = 3;
    public static final byte YEAR_OF_PLENTY = 4;

    public static final byte DEFAULT_NUM_KNIGHT = 15;
    public static final byte DEFAULT_NUM_VICTORY = 5;
    public static final byte DEFAULT_NUM_ROAD_BUILDING = 2;
    public static final byte DEFAULT_NUM_MONOPOLY = 2;
    public static final byte DEFAULT_NUM_YEAR_OF_PLENTY = 2;

    /*
     * Building Status
     */
    public static final byte STATUS_EMPTY = 0;
    public static final byte STATUS_SETTLEMENT = 1;
    public static final byte STATUS_CITY = 2;

    /*
     * Player
     */
    public static final byte UNASSIGNED_PLAYER = -1;
    public static final byte UNASSIGNED_EDGE = -1;
    public static final byte UNASSIGNED_VERTEX = -1;
    public static final byte UNASSIGNED_PORT = -1;

    private static Random randomGen = new Random();

    /*
     * Board
     */
    public static byte[] tilesResource = new byte[] { DESERT, WOOD, WOOD, WOOD, WOOD, BRICK, BRICK, BRICK, SHEEP, SHEEP,
            SHEEP, SHEEP, HAY, HAY, HAY, HAY, ROCK, ROCK, ROCK };

    public static byte[] tilesNumber = new byte[] { 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12 };

    public static byte[][] edgeToVertex = new byte[][] { new byte[] { 0, 1 }, new byte[] { 1, 2 }, new byte[] { 2, 3 },
            new byte[] { 3, 4 }, new byte[] { 4, 5 }, new byte[] { 5, 6 }, new byte[] { 0, 8 }, new byte[] { 2, 10 },
            new byte[] { 4, 12 }, new byte[] { 6, 14 }, new byte[] { 7, 8 }, new byte[] { 8, 9 }, new byte[] { 9, 10 },
            new byte[] { 10, 11 }, new byte[] { 11, 12 }, new byte[] { 13, 14 }, new byte[] { 14, 15 },
            new byte[] { 7, 17 }, new byte[] { 9, 19 }, new byte[] { 11, 21 }, new byte[] { 13, 23 },
            new byte[] { 15, 25 }, new byte[] { 16, 17 }, new byte[] { 17, 18 }, new byte[] { 18, 19 },
            new byte[] { 19, 20 }, new byte[] { 20, 21 }, new byte[] { 21, 22 }, new byte[] { 22, 23 },
            new byte[] { 23, 24 }, new byte[] { 24, 25 }, new byte[] { 25, 26 }, new byte[] { 16, 27 },
            new byte[] { 18, 29 }, new byte[] { 20, 31 }, new byte[] { 22, 33 }, new byte[] { 24, 35 },
            new byte[] { 26, 37 }, new byte[] { 27, 28 }, new byte[] { 28, 29 }, new byte[] { 29, 30 },
            new byte[] { 30, 31 }, new byte[] { 31, 32 }, new byte[] { 32, 33 }, new byte[] { 33, 34 },
            new byte[] { 34, 35 }, new byte[] { 35, 36 }, new byte[] { 36, 37 }, new byte[] { 28, 38 },
            new byte[] { 30, 40 }, new byte[] { 32, 42 }, new byte[] { 34, 44 }, new byte[] { 36, 46 },
            new byte[] { 38, 39 }, new byte[] { 39, 40 }, new byte[] { 40, 41 }, new byte[] { 41, 42 },
            new byte[] { 42, 43 }, new byte[] { 43, 44 }, new byte[] { 44, 45 }, new byte[] { 45, 46 },
            new byte[] { 39, 47 }, new byte[] { 41, 49 }, new byte[] { 43, 51 }, new byte[] { 45, 53 },
            new byte[] { 47, 48 }, new byte[] { 48, 49 }, new byte[] { 49, 50 }, new byte[] { 50, 51 },
            new byte[] { 51, 52 }, new byte[] { 52, 53 },

    };

    public static byte[][] tileToVertex = new byte[][] { new byte[] { 0, 1, 2, 8, 9, 10 },
            new byte[] { 2, 3, 4, 10, 11, 12 }, new byte[] { 4, 5, 6, 12, 13, 14 }, new byte[] { 7, 8, 9, 17, 18, 19 },
            new byte[] { 9, 10, 11, 19, 20, 21 }, new byte[] { 11, 12, 13, 21, 22, 23 },
            new byte[] { 13, 14, 15, 23, 24, 25 }, new byte[] { 16, 17, 18, 27, 28, 29 },
            new byte[] { 18, 19, 20, 29, 30, 31 }, new byte[] { 20, 21, 22, 31, 32, 33 },
            new byte[] { 22, 23, 24, 33, 34, 35 }, new byte[] { 24, 25, 26, 35, 36, 37 },
            new byte[] { 28, 29, 30, 38, 39, 40 }, new byte[] { 30, 31, 32, 40, 41, 42 },
            new byte[] { 32, 33, 34, 42, 43, 44 }, new byte[] { 34, 35, 36, 44, 45, 46 },
            new byte[] { 39, 40, 41, 47, 48, 49 }, new byte[] { 41, 42, 43, 49, 50, 51 },
            new byte[] { 43, 44, 45, 51, 52, 53 }, };

    public static byte DEFAULT_NUM_TILES = (byte) tilesResource.length;
    public static byte DEFAULT_NUM_VERTICES = 54;
    public static byte DEFAULT_NUM_EDGES = (byte) edgeToVertex.length;

    public static byte[][] vertexToEdge = new byte[DEFAULT_NUM_VERTICES][];

    static {
        initializeTiles();
        setupNodeToEdge();
    }

    public static void initializeTiles() {
        if (tilesResource.length != tilesNumber.length) {
            throw new RuntimeException("Resources and Numbers not aligned");
        }
        shuffleArray(tilesResource);
        shuffleArray(tilesNumber);
    }

    public static void shuffleArray(byte[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            byte helper = a[i];
            a[i] = a[change];
            a[change] = helper;
        }
    }

    public static void setupNodeToEdge() {
        ArrayList<Byte>[] nodeToEdgeList = new ArrayList[54];
        for (int i = 0; i < nodeToEdgeList.length; i++) {
            nodeToEdgeList[i] = new ArrayList<>();
        }
        for (int i = 0; i < edgeToVertex.length; i++) {
            byte[] verticies = edgeToVertex[i];
            for (int j = 0; j < verticies.length; j++) {
                nodeToEdgeList[verticies[j]].add((byte) i);
            }
        }
        for (int i = 0; i < vertexToEdge.length; i++) {
            ArrayList<Byte> list = nodeToEdgeList[i];
            vertexToEdge[i] = new byte[list.size()];
            for (int j = 0; j < list.size(); j++) {
                vertexToEdge[i][j] = list.get(j);
            }
        }
    }

    public static byte getRandomSlot(byte[] a) {
        byte total = 0;
        for (byte amount : a) {
            total += amount;
        }
        byte randomNumber = (byte) randomGen.nextInt(total);
        byte slot = 0;
        boolean found = false;
        for (int i = 0; i < a.length; i++) {
            int amountAvailable = a[i];
            randomNumber -= amountAvailable;
            if (randomNumber <= 0) {
                slot = (byte) i;
                found = true;
            }
        }
        if (!found) {
            throw new RuntimeException("Algorithm Failure");
        }
        return slot;
    }

}
