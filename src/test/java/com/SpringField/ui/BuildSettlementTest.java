package com.SpringField.ui;

// Catan Game Imports
import com.SpringField.engine.util.Util;
import com.SpringField.engine.BoardState;
import com.SpringField.engine.board.Vertex;

// Graphics/UI Imports
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

// Tests for
public class BuildSettlementTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {

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

        //BoardState scenario = new BoardState(4);

        //Board gameBoard = new SmallBoard(scenario);
        System.out.println("Settlement Phase Begin");
        // scenario.settlementPhase();
        System.out.println("Settlement Phase End");
        AnimationTimer gameLoop = new AnimationTimer() {

            public void handle(long currentNanoTime) {
                //gameBoard.drawBoard(gc);
                /*
                 * if (!scenario.playRound()) { System.out.println(scenario.toString()); stop(); // stops animation
                 * timer if winner found }
                 */
            }
        };
        gameLoop.start();

        stage.show(); // goes into Animation Timer
    }
}
