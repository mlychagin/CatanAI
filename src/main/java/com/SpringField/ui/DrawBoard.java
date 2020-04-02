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
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.SpringField.ui.util.Util.outputFileName;

public class DrawBoard extends Application {
    private static BoardState b;

    public static void main(String[] args) throws IOException {
        byte[] serializedBoardState = Files.readAllBytes(Path.of(outputFileName));
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(serializedBoardState));
        b = BoardState.deSerialize(input);
        input.close();
        File f = new File(outputFileName);
        f.delete();
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
