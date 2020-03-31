package com.SpringField.engine.board;

import com.SpringField.engine.BoardState;
import com.SpringField.engine.util.BoardStateConfig;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static com.SpringField.engine.util.Util.*;

public class BoardStateTest {
    private static Random r = new Random(0);
    private static int numPlayers = 4;

    private static BoardState getBoard() throws IOException {
        if(!initializedContext){
            initializeStaticInstance();
        }
        return new BoardState(new BoardStateConfig(null, numPlayers, r.nextLong()), numPlayers, r.nextLong());
    }

    @Test
    public void getCurrentPlayerTest() throws IOException {
        BoardState b = getBoard();
        for(int i = 0; i < 100; i++){
            assert i % 4 == b.getPlayerTurn();
            b.advanceTurn();
        }
    }

    @Test
    public void buildRoadToRoadTest() throws IOException {
        for(byte e = 0; e < DEFAULT_NUM_EDGES; e++){
            BoardState b = getBoard();
            b.buildSettlement(edgeToVertex[e][0]);
            b.buildRoad(e);
            for(int i = 0; i < numPlayers * 2; i++){
                b.advanceTurn();
            }
            assert b.getPlayerTurn() == 0;
            for(byte potentialRoad = 0; potentialRoad < DEFAULT_NUM_EDGES; potentialRoad++){
                boolean adjacent = false;
                for(byte adjacentEdges : edgeToEdge[e]){
                    if(potentialRoad == adjacentEdges){
                        assert b.canBuildRoad(potentialRoad, false);
                        adjacent = true;
                        break;
                    }
                }
                assert adjacent || !b.canBuildRoad(potentialRoad, false);
            }
        }
    }

    @Test
    public void buildSettlementToRoadTest() throws IOException {
        for(byte v = 0; v < DEFAULT_NUM_VERTICES; v++){
            BoardState b = getBoard();
            b.buildSettlement(v);
            for(int i = 0; i < numPlayers * 2; i++){
                b.advanceTurn();
            }
            assert b.getPlayerTurn() == 0;
            for(byte potentialRoad = 0; potentialRoad < DEFAULT_NUM_EDGES; potentialRoad++){
                boolean adjacent = false;
                for(byte adjacentEdges : vertexToEdge[v]){
                    if(potentialRoad == adjacentEdges){
                        assert b.canBuildRoad(potentialRoad, false);
                        adjacent = true;
                        break;
                    }
                }
                assert adjacent || !b.canBuildRoad(potentialRoad, false);
            }
        }
    }





}
