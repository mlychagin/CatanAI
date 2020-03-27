package com.succ.engine.util;

import com.succ.engine.board.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Util {
    /*
     * Materials
     */
    public static byte DESERT = 0;
    public static byte WOOD = 1;
    public static byte BRICK = 2;
    public static byte SHEEP = 3;
    public static byte HAY = 4;
    public static byte ROCK = 5;
    public static byte ANY = 6;

    /*
     * Structures
     */
    public static byte ROAD = 0;
    public static byte SETTLEMENT = 1 ;
    public static byte CITY = 2;

    /*
     * Dev Cards
     */
    public static byte KNIGHT = 0;
    public static byte VICTORY = 1;
    public static byte ROAD_BUILDING = 2;
    public static byte MONOPOLY = 3;
    public static byte YEAR_OF_PLENTY = 4;

    public static byte DEFAULT_NUM_KNIGHT = 15;
    public static byte DEFAULT_NUM_VICTORY = 5;
    public static byte DEFAULT_NUM_ROAD_BUILDING = 2;
    public static byte DEFAULT_NUM_MONOPOLY = 2;
    public static byte DEFAULT_NUM_YEAR_OF_PLENTY = 2;

    /*
     * Building Status
     */
    public static byte STATUS_EMPTY = 0;
    public static byte STATUS_SETTLEMENT = 1;
    public static byte STATUS_CITY = 2;

    /*
     * Player
     */
    public static byte UNASSIGNED_PLAYER = -1;
    public static byte DEFAULT_ROAD_COUNT = 15;
    public static byte DEFAULT_SETTLEMENT_COUNT = 5;
    public static byte DEFAULT_CITY_COUNT = 4;

    /*
     * Board
     */
    public static byte DEFAULT_NUM_VERTICES = 54;

    public static byte[] tilesResource =
            new byte[] { DESERT, WOOD, WOOD, WOOD, WOOD, BRICK, BRICK, BRICK, SHEEP, SHEEP, SHEEP, SHEEP, HAY, HAY, HAY,
                    HAY, ROCK, ROCK, ROCK };

    public static byte[] tilesNumber = new byte[] { 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12 };

    public static int[][] edgeDependencies =
            new int[][] { new int[] { 8, 9 }, new int[] { 0, 1 }, new int[] { 4, 5 }, new int[] { 16, 17 },
                    new int[] { 20, 21 }, new int[] { 24, 25 }, new int[] { 28, 29 }, new int[] { 32, 33 },
                    new int[] { 36, 37 }, new int[] { 40, 41 }, new int[] { 44, 45 }, new int[] { 48, 49 },
                    new int[] { 52, 53 }, new int[] { 0, 8 }, new int[] { 4, 12 }, new int[] { 28, 38 },
                    new int[] { 32, 42 }, new int[] { 36, 46 }, new int[] { 16, 27 }, new int[] { 20, 31 },
                    new int[] { 24, 35 }, new int[] { 13, 14 }, new int[] { 1, 2 }, new int[] { 5, 6 },
                    new int[] { 9, 10 }, new int[] { 17, 18 }, new int[] { 21, 22 }, new int[] { 25, 26 },
                    new int[] { 29, 30 }, new int[] { 33, 34 }, new int[] { 41, 42 }, new int[] { 45, 46 },
                    new int[] { 49, 50 }, new int[] { 41, 49 }, new int[] { 45, 53 }, new int[] { 9, 19 },
                    new int[] { 13, 23 }, new int[] { 18, 19 }, new int[] { 2, 3 }, new int[] { 10, 11 },
                    new int[] { 14, 15 }, new int[] { 22, 23 }, new int[] { 30, 31 }, new int[] { 34, 35 },
                    new int[] { 38, 39 }, new int[] { 42, 43 }, new int[] { 50, 51 }, new int[] { 2, 10 },
                    new int[] { 6, 14 }, new int[] { 30, 40 }, new int[] { 34, 44 }, new int[] { 18, 29 },
                    new int[] { 22, 33 }, new int[] { 26, 37 }, new int[] { 19, 20 }, new int[] { 7, 8 },
                    new int[] { 3, 4 }, new int[] { 11, 12 }, new int[] { 23, 24 }, new int[] { 27, 28 },
                    new int[] { 31, 32 }, new int[] { 35, 36 }, new int[] { 39, 40 }, new int[] { 43, 44 },
                    new int[] { 47, 48 }, new int[] { 51, 52 }, new int[] { 39, 47 }, new int[] { 43, 51 },
                    new int[] { 7, 17 }, new int[] { 11, 21 }, new int[] { 15, 25 } };

    public static int[][] tileDependencies =
            new int[][] { new int[] { 0, 1, 2, 8, 9, 10 }, new int[] { 2, 3, 4, 10, 11, 12 },
                    new int[] { 4, 5, 6, 12, 13, 14 }, new int[] { 7, 8, 9, 17, 18, 19 },
                    new int[] { 9, 10, 11, 19, 20, 21 }, new int[] { 11, 12, 13, 21, 22, 23 },
                    new int[] { 13, 14, 15, 23, 24, 25 }, new int[] { 16, 17, 18, 27, 28, 29 },
                    new int[] { 18, 19, 20, 29, 30, 31 }, new int[] { 20, 21, 22, 31, 32, 33 },
                    new int[] { 22, 23, 24, 33, 34, 35 }, new int[] { 24, 25, 26, 35, 36, 37 },
                    new int[] { 28, 29, 30, 38, 39, 40 }, new int[] { 30, 31, 32, 40, 41, 42 },
                    new int[] { 32, 33, 34, 42, 43, 44 }, new int[] { 34, 35, 36, 44, 45, 46 },
                    new int[] { 39, 40, 41, 47, 48, 49 }, new int[] { 41, 42, 43, 49, 50, 51 },
                    new int[] { 43, 44, 45, 51, 52, 53 }, };

    public static ArrayList<Tile> tiles;

    static {
        initializeTiles();
    }

    public static void initializeTiles() {
        if (tilesResource.length != tilesNumber.length) {
            throw new RuntimeException("Resources and Numbers not aligned");
        }
        tiles = new ArrayList<>();
        shuffleArray(tilesResource);
        shuffleArray(tilesNumber);
        for (int i = 0; i < tilesResource.length; i++) {
            tiles.add(new Tile(tilesResource[i], tilesNumber[i]));
        }
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

    public static void initializeVertexToEdgeLinking(){

    }
}
