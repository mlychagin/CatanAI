package com.SpringField.ui;

import com.SpringField.engine.BoardState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DrawBoard extends Application {
    private static BoardState b;

    public static void main(String[] args) throws IOException {
        String boardBytecode = args[0];
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(boardBytecode.getBytes()));
        b = BoardState.deSerialize(input);
        input.close();
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Catan Game");

        Group root = new Group();
        Scene theScene = new Scene(root);
        stage.setScene(theScene);

        Canvas canvas = new Canvas(896, 768);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Board gameBoard = new OriginalBoard(b);
        AnimationTimer gameLoop = new AnimationTimer() {

            public void handle(long currentNanoTime) {
                gameBoard.drawBoard(gc);
            }
        };
        gameLoop.start();

        stage.show(); // goes into Animation Timer
    }
}


