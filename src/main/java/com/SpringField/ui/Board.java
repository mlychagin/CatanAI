package com.SpringField.ui;

import com.SpringField.ui.util.Hexagon;
import com.SpringField.ui.util.Point;
import javafx.scene.canvas.GraphicsContext;

public abstract class Board {

    public abstract void drawBoard(GraphicsContext gc);

    public abstract Hexagon[] getTiles();

    public abstract Point[] getPoints();
}
