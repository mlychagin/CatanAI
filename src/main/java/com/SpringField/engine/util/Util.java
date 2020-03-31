package com.SpringField.engine.util;

import java.util.ArrayList;
import java.util.Collections;
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

    public static final byte DEFAULT_RESOURCE_COUNT = 19;

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
    public static final byte ROAD_BUILDING = 1;
    public static final byte MONOPOLY = 2;
    public static final byte YEAR_OF_PLENTY = 3;
    public static final byte VICTORY = 4;

    public static final byte DEFAULT_NUM_KNIGHT = 15;
    public static final byte DEFAULT_NUM_VICTORY = 5;
    public static final byte DEFAULT_NUM_ROAD_BUILDING = 2;
    public static final byte DEFAULT_NUM_MONOPOLY = 2;
    public static final byte DEFAULT_NUM_YEAR_OF_PLENTY = 2;
    public static final byte DEFAULT_DEV_TYPES = 5;

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

    /*
     * Board
     */
    public static byte[] tilesResource = new byte[] { DESERT, WOOD, WOOD, WOOD, WOOD, BRICK, BRICK, BRICK, SHEEP, SHEEP,
            SHEEP, SHEEP, HAY, HAY, HAY, HAY, ROCK, ROCK, ROCK };

    public static byte[] tilesNumber = new byte[] { 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12 };

    public static final byte[][] edgeToVertex = new byte[][] { new byte[] { 0, 1 }, new byte[] { 1, 2 },
            new byte[] { 2, 3 }, new byte[] { 3, 4 }, new byte[] { 4, 5 }, new byte[] { 5, 6 }, new byte[] { 0, 8 },
            new byte[] { 2, 10 }, new byte[] { 4, 12 }, new byte[] { 6, 14 }, new byte[] { 7, 8 }, new byte[] { 8, 9 },
            new byte[] { 9, 10 }, new byte[] { 10, 11 }, new byte[] { 11, 12 }, new byte[] { 12, 13 },
            new byte[] { 13, 14 }, new byte[] { 14, 15 }, new byte[] { 7, 17 }, new byte[] { 9, 19 },
            new byte[] { 11, 21 }, new byte[] { 13, 23 }, new byte[] { 15, 25 }, new byte[] { 16, 17 },
            new byte[] { 17, 18 }, new byte[] { 18, 19 }, new byte[] { 19, 20 }, new byte[] { 20, 21 },
            new byte[] { 21, 22 }, new byte[] { 22, 23 }, new byte[] { 23, 24 }, new byte[] { 24, 25 },
            new byte[] { 25, 26 }, new byte[] { 16, 27 }, new byte[] { 18, 29 }, new byte[] { 20, 31 },
            new byte[] { 22, 33 }, new byte[] { 24, 35 }, new byte[] { 26, 37 }, new byte[] { 27, 28 },
            new byte[] { 28, 29 }, new byte[] { 29, 30 }, new byte[] { 30, 31 }, new byte[] { 31, 32 },
            new byte[] { 32, 33 }, new byte[] { 33, 34 }, new byte[] { 34, 35 }, new byte[] { 35, 36 },
            new byte[] { 36, 37 }, new byte[] { 28, 38 }, new byte[] { 30, 40 }, new byte[] { 32, 42 },
            new byte[] { 34, 44 }, new byte[] { 36, 46 }, new byte[] { 38, 39 }, new byte[] { 39, 40 },
            new byte[] { 40, 41 }, new byte[] { 41, 42 }, new byte[] { 42, 43 }, new byte[] { 43, 44 },
            new byte[] { 44, 45 }, new byte[] { 45, 46 }, new byte[] { 39, 47 }, new byte[] { 41, 49 },
            new byte[] { 43, 51 }, new byte[] { 45, 53 }, new byte[] { 47, 48 }, new byte[] { 48, 49 },
            new byte[] { 49, 50 }, new byte[] { 50, 51 }, new byte[] { 51, 52 }, new byte[] { 52, 53 },

    };

    public static final byte[][] tileToVertex = new byte[][] { new byte[] { 0, 1, 2, 8, 9, 10 },
            new byte[] { 2, 3, 4, 10, 11, 12 }, new byte[] { 4, 5, 6, 12, 13, 14 }, new byte[] { 7, 8, 9, 17, 18, 19 },
            new byte[] { 9, 10, 11, 19, 20, 21 }, new byte[] { 11, 12, 13, 21, 22, 23 },
            new byte[] { 13, 14, 15, 23, 24, 25 }, new byte[] { 16, 17, 18, 27, 28, 29 },
            new byte[] { 18, 19, 20, 29, 30, 31 }, new byte[] { 20, 21, 22, 31, 32, 33 },
            new byte[] { 22, 23, 24, 33, 34, 35 }, new byte[] { 24, 25, 26, 35, 36, 37 },
            new byte[] { 28, 29, 30, 38, 39, 40 }, new byte[] { 30, 31, 32, 40, 41, 42 },
            new byte[] { 32, 33, 34, 42, 43, 44 }, new byte[] { 34, 35, 36, 44, 45, 46 },
            new byte[] { 39, 40, 41, 47, 48, 49 }, new byte[] { 41, 42, 43, 49, 50, 51 },
            new byte[] { 43, 44, 45, 51, 52, 53 }, };

    public static byte[] vertexToPort = new byte[] { ANY, ANY, UNASSIGNED_PORT, BRICK, BRICK, UNASSIGNED_PORT,
            UNASSIGNED_PORT, ANY, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT,
            UNASSIGNED_PORT, WOOD, WOOD, UNASSIGNED_PORT, ANY, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT,
            UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT, ANY, UNASSIGNED_PORT,
            SHEEP, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT,
            UNASSIGNED_PORT, UNASSIGNED_PORT, ANY, SHEEP, UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT,
            UNASSIGNED_PORT, UNASSIGNED_PORT, UNASSIGNED_PORT, HAY, HAY, ANY, ANY, UNASSIGNED_PORT, ROCK, ROCK,
            UNASSIGNED_PORT, UNASSIGNED_PORT };

    public final static byte DEFAULT_NUM_TILES = (byte) tilesResource.length;
    public final static byte DEFAULT_NUM_VERTICES = 54;
    public final static byte DEFAULT_NUM_EDGES = (byte) edgeToVertex.length;

    public final static byte[][] vertexToEdge = new byte[DEFAULT_NUM_VERTICES][];
    public final static byte[][] vertexToVertex = new byte[DEFAULT_NUM_VERTICES][];
    public final static byte[][] edgeToEdge = new byte[DEFAULT_NUM_EDGES][];

    /*
     * Game
     */
    public final static byte VICTORY_POINTS_REQ_WIN = 12;
    public final static byte WIN_CONDITION = -1;
    public final static byte NO_DICE_ROLL = -2;

    /*
     * BoardStateAI Space State Generation
     */
    public final static byte GENERATE_DEV_KNIGHT = 0;
    public final static byte GENERATE_PLAYER_TRADES = 1;
    public final static byte GENERATE_DEV_MONOPOLY = 2;
    public final static byte GENERATE_DEV_YEAR_OF_PLENTY = 3;
    public final static byte GENERATE_BANK_TRADES = 4;
    public final static byte GENERATE_DEV_ROAD_BUILDING = 5;
    public final static byte GENERATE_BUILD_ROAD = 6;
    public final static byte GENERATE_BUILD_SETTLEMENT = 7;
    public final static byte GENERATE_BUILD_CITY = 8;
    public final static byte GENERATE_BUY_DEV_CARD = 9;

    /*
     * Replay Commands
     */
    public final static byte INVALID_COMMAND = 0;
    public final static byte SEED_COMMAND = 1;
    public final static byte ROAD_COMMAND = 2;
    public final static byte SETTLEMENT_COMMAND = 3;
    public final static byte CITY_COMMAND = 4;
    public final static byte DEV_CARD_COMMAND = 5;
    public final static byte ROBBER_COMMAND = 6;
    public final static byte KNIGHT_COMMAND = 7;
    public final static byte ROAD_BUILDING_COMMAND = 8;
    public final static byte MONOPOLY_COMMAND = 9;
    public final static byte YEAR_OF_PLENTY_COMMAND = 10;
    public final static byte TRADE_BANK_COMMAND = 11;
    public final static byte TRADE_PLAYER_COMMAND = 12;
    public final static byte ADVANCE_TURN_COMMAND = 13;

    public static boolean initializedContext = false;

    public static void initializeStaticInstance() {
        setupVertexToEdge();
        setupEdgeToEdge();
        setupVertexToVertex();
        initializedContext = true;
    }

    public static void shuffleArray(byte[] a, Random r) {
        int n = a.length;
        r.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + r.nextInt(n - i);
            byte helper = a[i];
            a[i] = a[change];
            a[change] = helper;
        }
    }

    private static void setupVertexToEdge() {
        ArrayList<Byte>[] vertexToEdgeList = new ArrayList[DEFAULT_NUM_VERTICES];
        for (int i = 0; i < vertexToEdgeList.length; i++) {
            vertexToEdgeList[i] = new ArrayList<>();
        }
        for (int i = 0; i < edgeToVertex.length; i++) {
            byte[] verticies = edgeToVertex[i];
            for (int j = 0; j < verticies.length; j++) {
                vertexToEdgeList[verticies[j]].add((byte) i);
            }
        }
        for (int i = 0; i < vertexToEdge.length; i++) {
            ArrayList<Byte> list = vertexToEdgeList[i];
            vertexToEdge[i] = new byte[list.size()];
            for (int j = 0; j < list.size(); j++) {
                vertexToEdge[i][j] = list.get(j);
            }
        }
    }

    private static void setupEdgeToEdge() {
        for (int e = 0; e < DEFAULT_NUM_EDGES; e++) {
            ArrayList<Byte> adjacentEdges = new ArrayList<>();
            for (byte v : edgeToVertex[e]) {
                for (byte adjacentEdge : vertexToEdge[v]) {
                    if (adjacentEdge != e) {
                        adjacentEdges.add(adjacentEdge);
                    }
                }
            }
            Collections.sort(adjacentEdges);
            edgeToEdge[e] = new byte[adjacentEdges.size()];
            for (int i = 0; i < adjacentEdges.size(); i++) {
                edgeToEdge[e][i] = adjacentEdges.get(i);
            }
        }
    }

    private static void setupVertexToVertex() {
        for (byte v = 0; v < DEFAULT_NUM_VERTICES; v++) {
            ArrayList<Byte> adjacentVerticies = new ArrayList<>();
            for (byte e : vertexToEdge[v]) {
                for (byte adjacentVertex : edgeToVertex[e]) {
                    if (adjacentVertex != v) {
                        adjacentVerticies.add(adjacentVertex);
                    }
                }
            }
            Collections.sort(adjacentVerticies);
            vertexToVertex[v] = new byte[adjacentVerticies.size()];
            for (int i = 0; i < adjacentVerticies.size(); i++) {
                vertexToVertex[v][i] = adjacentVerticies.get(i);
            }
        }
    }

    public static byte getRandomSlot(byte[] a, Random r) {
        byte total = 0;
        for (byte amount : a) {
            total += amount;
        }
        byte randomNumber = (byte) r.nextInt(total);
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
