package com.SpringField.ui.Util;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import com.SpringField.engine.util.Util;
import com.SpringField.engine.BoardState;

public class Hexagon extends Polygon {

    private int centerX, centerY;
    private static int RADIUS = 80;
    private Line[] edges = new Line[6];
    private Point[] points = new Point[6];

    final int[] probabilities = { 1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1 };
    private Image brickTile = new Image("brick.png");
    private Image desertTile = new Image("desert.png");
    private Image oreTile = new Image("ore.png");
    private Image wheatTile = new Image("wheat.png");
    private Image pastureTile = new Image("pasture.png");
    private Image forestTile = new Image("forest.png");

    private final int tileIndex;
    private byte tileNumber;
    private byte tileResource;

    public Hexagon(int centerX, int centerY, int tileIndex, byte tileNumber, byte tileResource, Point nw, Point n, Point ne, Point se, Point s, Point sw) {
        setOpacity(0.0);
        this.centerX = centerX;
        this.centerY = centerY;
        this.tileIndex = tileIndex;
        this.tileNumber = tileNumber;
        this.tileResource = tileResource;

        points[0] = nw;
        points[1] = n;
        points[2] = ne;
        points[3] = se;
        points[4] = s;
        points[5] = sw;

        initPoints();
    }

    private void initPoints() {
        points[0] = new Point(centerX - RADIUS, centerY - (RADIUS / 2)); // NW point
        points[1] = new Point(centerX, centerY - RADIUS); // N point
        points[2] = new Point(centerX + RADIUS, centerY - (RADIUS / 2)); // NE point
        points[3] = new Point(centerX + RADIUS, centerY + (RADIUS / 2)); // SE point
        points[4] = new Point(centerX, centerY + RADIUS); // S point
        points[5] = new Point(centerX - RADIUS, centerY + (RADIUS / 2)); // SW point

        getPoints().addAll(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(),
                points[2].getY(), points[3].getX(), points[3].getY(), points[4].getX(), points[4].getY(),
                points[5].getX(), points[5].getY());
    }

    public void initLines(int nw, int ne, int e, int se, int sw, int w) {
        edges[0] = new Line(points[0], points[1], nw);
        edges[1] = new Line(points[1], points[2], ne);
        edges[2] = new Line(points[2], points[3], e);
        edges[3] = new Line(points[3], points[4], se);
        edges[4] = new Line(points[4], points[5], sw);
        edges[5] = new Line(points[5], points[0], w);
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public Line getLine(int lineNumber) {
        return edges[lineNumber];
    }

    public Line[] getEdges() {
        return edges;
    }

    public Point[] getPointList() {
        return points;
    }

    public void display(GraphicsContext gc, BoardState currentState) {
        // Print edge
        drawTileLabel(gc, currentState.getRobberTile() == tileIndex);
        displayDots(gc);
        for (Line l : edges) {
            l.display(gc, currentState);
        }
    }

    private void displayDots(GraphicsContext gc) {
        // Print probability
        int xPos = centerX;
        int yPos = centerY;
        int tileDiceNumber = tileNumber;

        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(new Font(25));
        xPos = xPos - probabilities[tileDiceNumber - 2] * 4;
        for (int i = 0; i < probabilities[tileDiceNumber - 2]; i++) {
            if (tileDiceNumber == 8 || tileDiceNumber == 6) {
                gc.setFill(Color.RED);
            }
            if (tileDiceNumber != 7) {
                gc.fillOval(xPos + i * 8, yPos + 5, 2.5, 2.5);
            }
        }
    }

    private void drawTileLabel(GraphicsContext gc, boolean hasRobber) {
        // Method to draw lable on each tile
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.LEFT);
        Image toDraw = null;
        String toPrint = "";

        int tileDiceNumber = tileNumber;
        int resourceType = tileResource;

        switch (resourceType) {
        case Util.DESERT:
            toPrint = "desert";
            toDraw = desertTile;
            break;
        case Util.WOOD:
            toPrint = "wood";
            toDraw = forestTile;
            break;
        case Util.BRICK:
            toPrint = "brick";
            toDraw = brickTile;
            break;
        case Util.SHEEP:
            toPrint = "sheep";
            toDraw = pastureTile;
            break;
        case Util.HAY:
            toPrint = "hay";
            toDraw = wheatTile;
            break;
        case Util.ROCK:
            toPrint = "rock";
            toDraw = oreTile;
            break;
        }
        int xPos = centerX;
        int yPos = centerY;
        gc.drawImage(toDraw, xPos - RADIUS, yPos - RADIUS, RADIUS * 2, RADIUS * 2);
        toPrint = Integer.toString(tileDiceNumber);
        if (tileDiceNumber == 7) {
            toPrint = "";
        }
        if (tileDiceNumber == 6 || tileDiceNumber == 8) {
            gc.setFill(Color.RED);
        } else {
            gc.setFill(Color.BLACK);
        }
        gc.setFont(new Font(25));
        if (tileDiceNumber >= 10) {
            gc.fillText(toPrint, xPos - 15, yPos);
        } else {
            gc.fillText(toPrint, xPos - 10, yPos);
        }

        if (hasRobber) {
            gc.setLineWidth(4);
            gc.strokeLine(xPos - 15, yPos - 5, xPos + 15, yPos - 5);
            gc.fill();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
        }
    }
}
