package com.SpringField.engine.board;

import com.SpringField.engine.BoardState;
import com.SpringField.engine.util.BoardStateConfig;
import org.junit.Test;

import java.io.IOException;

import static com.SpringField.engine.util.Util.*;

public class BoardStateTest {
    private static int numPlayers = 4;

    private static BoardState getBoard() throws IOException {
        return new BoardState(new BoardStateConfig(null, numPlayers, 0), numPlayers, 0);
    }

    private static BoardState getBoardAfterSettlementPhase() throws IOException {
        BoardState b = getBoard();

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

        assert b.getPlayerTurn() == 0;
        return b;
    }

    @Test
    public void getCurrentPlayerTest() throws IOException {
        BoardState b = getBoard();
        for(int i = 0; i < 100; i++){
            assert i % 4 == b.getPlayerTurn();
            b.advanceTurn();
        }
    }

    /*
     * Todo:
     *  Build City
     */

    @Test
    public void buildRoadTest() throws IOException {

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

    @Test
    public void buildSettlementTest() throws IOException {
        for(byte settlementVertex = 0; settlementVertex < DEFAULT_NUM_VERTICES; settlementVertex++){
            BoardState b = getBoard();
            b.buildSettlement(settlementVertex);
            b.advanceTurn();
            assert b.getPlayerTurn() == 1;
            for(byte potentialSettlement = 0; potentialSettlement < DEFAULT_NUM_VERTICES; potentialSettlement++){
                if(potentialSettlement == settlementVertex){
                    assert !b.canBuildSettlement(settlementVertex);
                    continue;
                }
                boolean adjacent = false;
                for(byte adjacentVertex : vertexToVertex[settlementVertex]){
                    if(potentialSettlement == adjacentVertex){
                        assert !b.canBuildSettlement(potentialSettlement);
                        adjacent = true;
                        break;
                    }
                }
                assert adjacent || b.canBuildSettlement(potentialSettlement);
            }
        }
    }

    @Test
    public void buildCityTest() throws IOException {
        for(byte v = 0; v < DEFAULT_NUM_VERTICES; v++){
            BoardState b = getBoard();
            b.buildSettlement(v);
            Player p = b.getPlayers()[b.getPlayerTurn()];
            assert !b.canBuildCity(v);
            p.addResource(HAY, (byte) 2);
            p.addResource(ROCK, (byte) 3);
            assert b.canBuildCity(v);
            b.buildCity(v);
            assert !b.canBuildCity(v);
        }
    }

    @Test
    public void playRobberTest() throws IOException {
        BoardState b = getBoard();
        b.buildSettlement((byte) 10);
        byte robbedPlayerId = b.getPlayerTurn();
        Player robbedPlayer = b.getPlayers()[robbedPlayerId];
        robbedPlayer.addResource(WOOD, (byte) 1);
        b.advanceTurn();
        assert b.canPlayRobber((byte) 4, robbedPlayerId);
        assert !b.canPlayRobber((byte) 4, b.getPlayerTurn());
        assert !b.canPlayRobber((byte) 4, (byte) (b.getPlayerTurn() + 1));
        b.playRobber((byte) 4, robbedPlayerId);
        Player robbingPlayer = b.getPlayers()[b.getPlayerTurn()];
        assert robbedPlayer.getResources()[WOOD] == 0;
        assert robbingPlayer.getResources()[WOOD] == 1;
        assert !b.canPlayRobber((byte) 4, robbedPlayerId);
    }




}
