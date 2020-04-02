package com.SpringField.ui;

import com.SpringField.ui.util.Hexagon;
import com.SpringField.ui.util.Point;
import javafx.scene.canvas.GraphicsContext;
import com.SpringField.engine.BoardState;

public class OriginalBoard extends Board {
    private static int TILE_RADIUS = 80;
    private Hexagon[] tiles = new Hexagon[19];
    private Point[] points = new Point[54];

    BoardState currentState;

    public OriginalBoard(BoardState initialState) {
        final int INITIAL_TILE_X = 280;
        final int INITIAL_TILE_Y = 120;

        byte[] tilesNumber = initialState.getConfig().getTilesNumber();
        byte[] tilesResource = initialState.getConfig().getTilesResource();

        currentState = initialState;
        // initialize points [0-53]
        initPoints();

        // Top 3 tiles
        tiles[0] = new Hexagon(INITIAL_TILE_X, INITIAL_TILE_Y, 0, tilesNumber[0], tilesResource[0], points[0],
                points[1], points[2], points[10], points[9], points[8]);
        tiles[0].initLines(0, 1, 7, 12, 11, 6);

        tiles[1] = new Hexagon(tiles[0].getCenterX() + (TILE_RADIUS * 2), tiles[0].getCenterY(), 1, tilesNumber[1],
                tilesResource[1], points[2], points[3], points[4], points[12], points[11], points[10]);
        tiles[1].initLines(2, 3, 8, 14, 13, 7);

        tiles[2] = new Hexagon(tiles[1].getCenterX() + (TILE_RADIUS * 2), tiles[1].getCenterY(), 2, tilesNumber[2],
                tilesResource[2], points[4], points[5], points[6], points[14], points[13], points[12]);
        tiles[2].initLines(4, 5, 9, 16, 15, 8);

        // Second Row 4 tiles
        tiles[3] = new Hexagon(tiles[0].getCenterX() - TILE_RADIUS,
                tiles[0].getCenterY() + TILE_RADIUS + (TILE_RADIUS / 2), 3, tilesNumber[3], tilesResource[3], points[7],
                points[8], points[9], points[19], points[18], points[17]);
        tiles[3].initLines(10, 11, 19, 25, 24, 18);

        tiles[4] = new Hexagon(tiles[3].getCenterX() + TILE_RADIUS * 2, tiles[3].getCenterY(), 4, tilesNumber[4],
                tilesResource[4], points[9], points[10], points[11], points[21], points[20], points[19]);
        tiles[4].initLines(12, 13, 20, 27, 26, 19);

        tiles[5] = new Hexagon(tiles[4].getCenterX() + TILE_RADIUS * 2, tiles[4].getCenterY(), 5, tilesNumber[5],
                tilesResource[5], points[11], points[12], points[13], points[23], points[22], points[21]);
        tiles[5].initLines(14, 15, 21, 29, 28, 20);

        tiles[6] = new Hexagon(tiles[5].getCenterX() + TILE_RADIUS * 2, tiles[5].getCenterY(), 6, tilesNumber[6],
                tilesResource[6], points[13], points[14], points[15], points[25], points[24], points[23]);
        tiles[6].initLines(16, 17, 22, 31, 30, 21);

        // Third Row 5 tiles
        tiles[7] = new Hexagon(tiles[3].getCenterX() - TILE_RADIUS,
                tiles[3].getCenterY() + TILE_RADIUS + (TILE_RADIUS / 2), 7, tilesNumber[7], tilesResource[7],
                points[16], points[17], points[18], points[29], points[28], points[27]);
        tiles[7].initLines(23, 24, 34, 40, 39, 33);

        tiles[8] = new Hexagon(tiles[7].getCenterX() + TILE_RADIUS * 2, tiles[7].getCenterY(), 8, tilesNumber[8],
                tilesResource[8], points[18], points[19], points[20], points[31], points[30], points[29]);
        tiles[8].initLines(25, 26, 35, 42, 41, 34);

        tiles[9] = new Hexagon(tiles[8].getCenterX() + TILE_RADIUS * 2, tiles[8].getCenterY(), 9, tilesNumber[9],
                tilesResource[9], points[20], points[21], points[22], points[33], points[32], points[31]);
        tiles[9].initLines(27, 28, 36, 44, 43, 35);

        tiles[10] = new Hexagon(tiles[9].getCenterX() + TILE_RADIUS * 2, tiles[9].getCenterY(), 10, tilesNumber[10],
                tilesResource[10], points[22], points[23], points[24], points[35], points[34], points[33]);
        tiles[10].initLines(29, 30, 37, 46, 45, 36);

        tiles[11] = new Hexagon(tiles[10].getCenterX() + TILE_RADIUS * 2, tiles[10].getCenterY(), 11, tilesNumber[11],
                tilesResource[11], points[24], points[25], points[26], points[37], points[36], points[35]);
        tiles[11].initLines(31, 32, 38, 48, 47, 37);

        // Fourth Row 4 tiles
        tiles[12] = new Hexagon(tiles[3].getCenterX(), tiles[7].getCenterY() + TILE_RADIUS + (TILE_RADIUS / 2), 12,
                tilesNumber[12], tilesResource[12], points[28], points[29], points[30], points[40], points[39],
                points[38]);
        tiles[12].initLines(40, 41, 50, 55, 54, 49);

        tiles[13] = new Hexagon(tiles[12].getCenterX() + TILE_RADIUS * 2, tiles[12].getCenterY(), 13, tilesNumber[13],
                tilesResource[13], points[30], points[31], points[32], points[42], points[41], points[40]);
        tiles[13].initLines(42, 43, 51, 57, 56, 50);

        tiles[14] = new Hexagon(tiles[13].getCenterX() + TILE_RADIUS * 2, tiles[13].getCenterY(), 14, tilesNumber[14],
                tilesResource[14], points[32], points[33], points[34], points[44], points[43], points[42]);
        tiles[14].initLines(44, 45, 52, 59, 58, 51);

        tiles[15] = new Hexagon(tiles[14].getCenterX() + TILE_RADIUS * 2, tiles[14].getCenterY(), 15, tilesNumber[15],
                tilesResource[15], points[34], points[35], points[36], points[46], points[45], points[44]);
        tiles[15].initLines(46, 47, 53, 61, 60, 52);

        // Fifth Row 3 tiles
        tiles[16] = new Hexagon(tiles[0].getCenterX(), tiles[12].getCenterY() + TILE_RADIUS + (TILE_RADIUS / 2), 16,
                tilesNumber[16], tilesResource[16], points[39], points[40], points[41], points[49], points[48],
                points[47]);
        tiles[16].initLines(55, 56, 63, 67, 66, 62);

        tiles[17] = new Hexagon(tiles[16].getCenterX() + TILE_RADIUS * 2, tiles[16].getCenterY(), 17, tilesNumber[17],
                tilesResource[17], points[41], points[42], points[43], points[51], points[50], points[49]);
        tiles[17].initLines(57, 58, 64, 69, 68, 63);

        tiles[18] = new Hexagon(tiles[17].getCenterX() + TILE_RADIUS * 2, tiles[17].getCenterY(), 18, tilesNumber[18],
                tilesResource[18], points[43], points[44], points[45], points[53], points[52], points[51]);
        tiles[18].initLines(59, 60, 65, 71, 70, 64);
    }

    private void assignSlots(int tileNumber, int slot1, int slot2, int slot3, int slot4, int slot5, int slot6) {
        Point[] temp;
        // Tile #1
        temp = tiles[tileNumber].getPointList();
        temp[0].setSlot(slot1);
        temp[1].setSlot(slot2);
        temp[2].setSlot(slot3);
        temp[3].setSlot(slot4);
        temp[4].setSlot(slot5);
        temp[5].setSlot(slot6);
    }

    public void updateState(BoardState newState) {
        currentState = newState;
    }

    private void initPoints() {
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(i);
        }
    }

    private void initResources() {
    }

    private void initTileNumbers() {
    }

    @Override
    public void drawBoard(GraphicsContext gc) {
        for (Hexagon tile : tiles) {
            tile.display(gc, currentState);
        }
        for (Point point : points) {
            point.display(gc, currentState);
        }
    }

    public Hexagon[] getTiles() {
        return tiles;
    }

    public Point[] getPoints() {
        return points;
    }
}
