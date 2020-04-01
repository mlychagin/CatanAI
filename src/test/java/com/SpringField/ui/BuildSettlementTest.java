package com.SpringField.ui;

// Catan Game Imports
import com.SpringField.engine.util.BoardStateConfig;
import com.SpringField.engine.BoardState;

// Graphics/UI Imports
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import static com.SpringField.engine.util.Util.initializeStaticInstance;

// Tests for
public class BuildSettlementTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Catan Game");

        Group root = new Group();
        Scene theScene = new Scene(root);
        stage.setScene(theScene);

        Canvas canvas = new Canvas(896, 768);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        BoardState b = new BoardState(new BoardStateConfig(null, 4, 0), 4, 0);

        //Round 1
        b.buildSettlement((byte) 29);
        b.buildRoad((byte) 41);
        b.advanceTurn();

        b.buildSettlement((byte) 24);
        b.buildRoad((byte) 30);
        b.advanceTurn();

        b.buildSettlement((byte) 43);
        b.buildRoad((byte) 58);
        b.advanceTurn();

        b.buildSettlement((byte) 33);
        b.buildRoad((byte) 36);
        b.advanceTurn();

        //Round 2
        b.buildSettlement((byte) 19);
        b.buildRoad((byte) 26);
        b.advanceTurn();

        b.buildSettlement((byte) 17);
        b.buildRoad((byte) 18);
        b.advanceTurn();

        b.buildSettlement((byte) 41);
        b.buildRoad((byte) 63);
        b.advanceTurn();

        b.buildSettlement((byte) 11);
        b.buildRoad((byte) 14);
        b.advanceTurn();

        Board gameBoard = new OriginalBoard(b);
        System.out.println("Settlement Phase Begin");
        // scenario.settlementPhase();
        System.out.println("Settlement Phase End");
        AnimationTimer gameLoop = new AnimationTimer() {

            public void handle(long currentNanoTime) {
                gameBoard.drawBoard(gc);
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


