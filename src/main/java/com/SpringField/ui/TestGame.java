//package com.SpringField.ui;
//
//// Catan Game Imports
//import com.SpringField.engine.util.Util;
//import com.SpringField.engine.BoardState;
//import com.SpringField.engine.board.Vertex;
//
//// Graphics/UI Imports
//import javafx.animation.AnimationTimer;
//import javafx.application.Application;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.image.Image;
//import javafx.scene.paint.Color;
//import javafx.scene.text.*;
//import javafx.stage.Stage;
//
//public class TestGame extends Application {
//    private Image brickTile;
//    private Image desertTile;
//    private Image oreTile;
//    private Image wheatTile;
//    private Image pastureTile;
//    private Image forestTile;
//
//    private static int TILE_RADIUS = 80;
//    private Hexagon[] tiles = new Hexagon[19];
//
//    BoardState scenario;
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void init() {
//        brickTile = new Image("brick.png");
//        desertTile = new Image("desert.png");
//        oreTile = new Image("ore.png");
//        wheatTile = new Image("wheat.png");
//        pastureTile = new Image("pasture.png");
//        forestTile = new Image("forest.png");
//
//        initHexagons();
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        stage.setTitle("Catan Game");
//
//        Group root = new Group();
//        Scene theScene = new Scene(root);
//        stage.setScene(theScene);
//
//        Canvas canvas = new Canvas(896, 768);
//        root.getChildren().add(canvas);
//
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//
//        scenario = new BoardState(4);
//
//        System.out.println("Settlement Phase Begin");
//        // scenario.settlementPhase();
//        System.out.println("Settlement Phase End");
//        AnimationTimer gameLoop = new AnimationTimer() {
//
//            public void handle(long currentNanoTime) {
//                drawTileLabel(gc);
//                drawDots(gc);
//                drawHexagons(gc);
//                drawSettlements(gc);
//                updateRoads();
//                /*
//                 * if (!scenario.playRound()) { System.out.println(scenario.toString()); stop(); // stops animation
//                 * timer if winner found }
//                 */
//            }
//        };
//        gameLoop.start();
//
//        stage.show(); // goes into Animation Timer
//    }
//
//    // Method to draw lable on each tile
//    private void drawTileLabel(GraphicsContext gc) {
//        gc.setFill(Color.BLACK);
//        gc.setTextAlign(TextAlignment.LEFT);
//        int tileNumber = 0;
//        Image toDraw = null;
//        for (int resource : Util.tilesResource) {
//            String toPrint = "";
//            switch (resource) {
//            case Util.DESERT:
//                toPrint = "desert";
//                toDraw = desertTile;
//                break;
//            case Util.WOOD:
//                toPrint = "wood";
//                toDraw = forestTile;
//                break;
//            case Util.BRICK:
//                toPrint = "brick";
//                toDraw = brickTile;
//                break;
//            case Util.SHEEP:
//                toPrint = "sheep";
//                toDraw = pastureTile;
//                break;
//            case Util.HAY:
//                toPrint = "hay";
//                toDraw = wheatTile;
//                break;
//            case Util.ROCK:
//                toPrint = "rock";
//                toDraw = oreTile;
//                break;
//            }
//            int xPos = tiles[tileNumber].getCenterX();
//            int yPos = tiles[tileNumber].getCenterY();
//            gc.drawImage(toDraw, xPos - TILE_RADIUS, yPos - TILE_RADIUS, TILE_RADIUS * 2, TILE_RADIUS * 2);
//            int tileDiceNumber = Util.tilesNumber[tileNumber];
//            toPrint = Integer.toString(tileDiceNumber);
//            if (tileDiceNumber == 7) {
//                toPrint = "";
//            }
//            if (tileDiceNumber == 6 || tileDiceNumber == 8) {
//                gc.setFill(Color.RED);
//            } else {
//                gc.setFill(Color.BLACK);
//            }
//            gc.setFont(new Font(25));
//            if (tileDiceNumber >= 10) {
//                gc.fillText(toPrint, xPos - 15, yPos);
//            } else {
//                gc.fillText(toPrint, xPos - 10, yPos);
//            }
//            tileNumber++;
//        }
//    }
//
//    private void drawDots(GraphicsContext gc) {
//        int tileNumber = 0;
//        // {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}
//        final int[] probabilities = { 1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1 };
//        for (Hexagon tile : tiles) {
//            int xPos = tile.getCenterX();
//            int yPos = tile.getCenterY();
//            int tileDiceNumber = Util.tilesNumber[tileNumber];
//
//            gc.setFill(Color.BLACK);
//            gc.setTextAlign(TextAlignment.LEFT);
//            gc.setFont(new Font(25));
//            xPos = xPos - probabilities[tileDiceNumber - 2] * 4;
//            for (int i = 0; i < probabilities[tileDiceNumber - 2]; i++) {
//                if (tileDiceNumber == 8 || tileDiceNumber == 6) {
//                    gc.setFill(Color.RED);
//                }
//                if (tileDiceNumber != 7) {
//                    gc.fillOval(xPos + i * 8, yPos + 5, 2.5, 2.5);
//                }
//            }
//            // gc.fillText(toPrint, xPos - 10, yPos);
//            tileNumber++;
//        }
//    }
//
//    private void drawHexagons(GraphicsContext gc) {
//        for (Hexagon tile : tiles) {
//            tile.display(gc);
//        }
//    }
//
//    // Method loops over all vertices and draws a settlement with the player's color if it exists
//    private void drawSettlements(GraphicsContext gc) {
//          int size = 20;
//          Vertex[] vertices = scenario.getVertices();
//          for (int i = 0; i < vertices.length; i++) {
//              if (vertices[i].getBuilding() != Util.STATUS_EMPTY) {
//                  Point location = findPoint(i);
//                  int x = location.getX();
//                  int y = location.getY();
//                  switch (vertices[i].getPlayerId()) {
//                      case 0: gc.setFill(Color.RED); break;
//                      case 1: gc.setFill(Color.BLUE); break;
//                      case 2: gc.setFill(Color.ORANGE); break;
//                      case 3: gc.setFill(Color.GREEN); break;
//                  }
//                  gc.fillRect(x - (size / 2), y - size / 2, size, size);
//                  if(vertices[i].getBuilding() == Util.STATUS_CITY){
//                      gc.fillPolygon(new double[]{(x - (size/2)), x, (x + (size/2))},
//                                     new double[]{y - size / 2, y - size, y - size / 2},3);
//                  }
//                  gc.setFill(Color.BLACK);
//              }
//          }
//    }
//
//    private void initHexagons() {
//        final int INITIAL_TILE_X = 280;
//        final int INITIAL_TILE_Y = 120;
//        // Top 3 tiles
//        tiles[0] = new Hexagon(INITIAL_TILE_X, INITIAL_TILE_Y);
//        assignSlots(0, 0, 1, 2, 10, 9, 8);
//        tiles[1] = new Hexagon(tiles[0].getCenterX() + (TILE_RADIUS * 2), tiles[0].getCenterY());
//        assignSlots(1, 2, 3, 4, 12, 11, 10);
//        tiles[2] = new Hexagon(tiles[1].getCenterX() + (TILE_RADIUS * 2), tiles[1].getCenterY());
//        assignSlots(2, 4, 5, 6, 14, 13, 12);
//
//        // Second Row 4 tiles
//        tiles[3] = new Hexagon(tiles[0].getCenterX() - TILE_RADIUS,
//                tiles[0].getCenterY() + TILE_RADIUS + (TILE_RADIUS / 2));
//        assignSlots(3, 7, 8, 9, 19, 18, 17);
//        tiles[4] = new Hexagon(tiles[3].getCenterX() + TILE_RADIUS * 2, tiles[3].getCenterY());
//        assignSlots(4, 9, 10, 11, 21, 20, 19);
//        tiles[5] = new Hexagon(tiles[4].getCenterX() + TILE_RADIUS * 2, tiles[4].getCenterY());
//        assignSlots(5, 11, 12, 13, 23, 22, 21);
//        tiles[6] = new Hexagon(tiles[5].getCenterX() + TILE_RADIUS * 2, tiles[5].getCenterY());
//        assignSlots(6, 13, 14, 15, 25, 24, 23);
//
//        // Third Row 5 tiles
//        tiles[7] = new Hexagon(tiles[3].getCenterX() - TILE_RADIUS,
//                tiles[3].getCenterY() + TILE_RADIUS + (TILE_RADIUS / 2));
//        assignSlots(7, 16, 17, 18, 29, 28, 27);
//        tiles[8] = new Hexagon(tiles[7].getCenterX() + TILE_RADIUS * 2, tiles[7].getCenterY());
//        assignSlots(8, 18, 19, 20, 31, 30, 29);
//        tiles[9] = new Hexagon(tiles[8].getCenterX() + TILE_RADIUS * 2, tiles[8].getCenterY());
//        assignSlots(9, 20, 21, 22, 33, 32, 31);
//        tiles[10] = new Hexagon(tiles[9].getCenterX() + TILE_RADIUS * 2, tiles[9].getCenterY());
//        assignSlots(10, 22, 23, 24, 35, 34, 33);
//        tiles[11] = new Hexagon(tiles[10].getCenterX() + TILE_RADIUS * 2, tiles[10].getCenterY());
//        assignSlots(11, 24, 25, 26, 37, 36, 35);
//
//        // Fourth Row 4 tiles
//        tiles[12] = new Hexagon(tiles[3].getCenterX(), tiles[7].getCenterY() + TILE_RADIUS + (TILE_RADIUS / 2));
//        assignSlots(12, 28, 29, 30, 40, 39, 38);
//        tiles[13] = new Hexagon(tiles[12].getCenterX() + TILE_RADIUS * 2, tiles[12].getCenterY());
//        assignSlots(13, 30, 31, 32, 42, 41, 40);
//        tiles[14] = new Hexagon(tiles[13].getCenterX() + TILE_RADIUS * 2, tiles[13].getCenterY());
//        assignSlots(14, 32, 33, 34, 44, 43, 42);
//        tiles[15] = new Hexagon(tiles[14].getCenterX() + TILE_RADIUS * 2, tiles[14].getCenterY());
//        assignSlots(15, 34, 35, 36, 46, 45, 44);
//
//        // Fifth Row 3 tiles
//        tiles[16] = new Hexagon(tiles[0].getCenterX(), tiles[12].getCenterY() + TILE_RADIUS + (TILE_RADIUS / 2));
//        assignSlots(16, 39, 40, 41, 49, 48, 47);
//        tiles[17] = new Hexagon(tiles[16].getCenterX() + TILE_RADIUS * 2, tiles[16].getCenterY());
//        assignSlots(17, 41, 42, 43, 51, 50, 49);
//        tiles[18] = new Hexagon(tiles[17].getCenterX() + TILE_RADIUS * 2, tiles[17].getCenterY());
//        assignSlots(18, 43, 44, 45, 53, 52, 51);
//    }
//
//    private void assignSlots(int tileNumber, int slot1, int slot2, int slot3, int slot4, int slot5, int slot6) {
//        Point[] temp;
//        // Tile #1
//        temp = tiles[tileNumber].getPoints();
//        temp[0].setSlot(slot1);
//        temp[1].setSlot(slot2);
//        temp[2].setSlot(slot3);
//        temp[3].setSlot(slot4);
//        temp[4].setSlot(slot5);
//        temp[5].setSlot(slot6);
//    }
//
//    // Method goes through each vertex's edges and changes color of edges where there is a road.
//    // Method does not draw the edges/road. drawHexagons() draws the edges/roads.
//    private void updateRoads() {
//        byte[] edges = scenario.getEdges();
//        for (int i = 0; i < edges.length; i++) {
//            if(edges[i] != Util.UNASSIGNED_PLAYER){
//                updateRoadColor(edges[i], Util.edgeToVertex[i][0], Util.edgeToVertex[i][1]);
//            }
//        }
//    }
//
//    private Point findPoint(int slot) {
//        for (Hexagon tile : tiles) {
//            for (int i = 0; i < 6; i++) { // 6 points for each ui.Hexagon
//                Point temp = tile.getPoints()[i];
//                if (temp.getSlot() == slot) {
//                    return temp;
//                }
//            }
//        }
//        return null;
//    }
//
//    private void updateRoadColor(int playerNumber, int slot1, int slot2) {
//        // May not be the most efficient, but not that many to look through (loops through ~114 times)
//        for (Hexagon tile : tiles) {
//            for (Line line : tile.getEdges()) {
//                int vertex1 = line.getP1().getSlot();
//                int vertex2 = line.getP2().getSlot();
//                if (((vertex1 == slot1) && (vertex2 == slot2)) || ((vertex1 == slot2) && (vertex2 == slot1))) {
//                    line.setColor(playerNumber); // The integer playerNumber represents their color
//                }
//            }
//        }
//    }
//
//    public Hexagon[] getTiles() {
//        return tiles;
//    }
//}
