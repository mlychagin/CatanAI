package com.SpringField.ui;

import com.SpringField.engine.BoardState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line {
    /*
     * Color Codes 0 = Red 1 = Blue 2 = Orange 3 = Green else = black
     *
     */

    private int color = 4; // defaults to black
    private int edgeNumber;
    private Point p1, p2;

    public Line(Point p1, Point p2, int edgeNumber) {
        this.p1 = p1;
        this.p2 = p2;
        this.edgeNumber = edgeNumber;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public int getEdgeNumber() { return edgeNumber; }

    public int getColor() {
        return color;
    }

    public void setColor(int newColor) {
        color = newColor;
    }

    public void display(GraphicsContext gc, BoardState currentState) {
        switch (currentState.getEdges()[edgeNumber]) {
        case 0:
            gc.setStroke(Color.RED);
            gc.setLineWidth(4);
            break;
        case 1:
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(4);
            break;
        case 2:
            gc.setStroke(Color.ORANGE);
            gc.setLineWidth(4);
            break;
        case 3:
            gc.setStroke(Color.GREEN);
            gc.setLineWidth(4);
            break;
        default:
            gc.setStroke(Color.BLACK);
            break;
        }
        gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        gc.fill();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
    }
}
