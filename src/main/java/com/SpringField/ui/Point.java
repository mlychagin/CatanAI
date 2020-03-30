package com.SpringField.ui;

import com.SpringField.engine.BoardState;

import com.SpringField.engine.board.Vertex;
import com.SpringField.engine.util.Util;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Point extends Circle {

    private double x, y;
    private int slot;

    public Point(int x, int y) {
        setOpacity(0.0);
        setCenterX(x);
        setCenterY(y);
        setRadius(20.0f);
        this.x = x;
        this.y = y;
    }

    public Point(int slot) {
        this.slot = slot;
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        setOpacity(0.0);
        setCenterX(x);
        setCenterY(y);
        setRadius(20.0f);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public void display(GraphicsContext gc, BoardState currentState) {
        int size = 20;
        Vertex[] vertices = currentState.getVertices();
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].getBuilding() != Util.STATUS_EMPTY) {
                switch (vertices[i].getPlayerId()) {
                case 0:
                    gc.setFill(Color.RED);
                    break;
                case 1:
                    gc.setFill(Color.BLUE);
                    break;
                case 2:
                    gc.setFill(Color.ORANGE);
                    break;
                case 3:
                    gc.setFill(Color.GREEN);
                    break;
                }
                gc.fillRect(x - (size / 2), y - size / 2, size, size);
                if (vertices[i].getBuilding() == Util.STATUS_CITY) {
                    gc.fillPolygon(new double[] { (x - (size / 2)), x, (x + (size / 2)) },
                            new double[] { y - size / 2, y - size, y - size / 2 }, 3);
                }
                gc.setFill(Color.BLACK);
            }
        }
    }
}
