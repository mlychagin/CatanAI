package com.SpringField.engine.board;

import com.SpringField.engine.BoardState;
import com.SpringField.engine.util.BoardStateConfig;
import org.junit.Test;

import java.io.IOException;

import static com.SpringField.engine.util.Util.*;
import static com.SpringField.ui.util.Util.drawBoard;

public class BoardStateTest {
    private static int numPlayers = 4;

    private static BoardState getBoard() throws IOException {
        return new BoardState(new BoardStateConfig(null, numPlayers, 0), numPlayers, 0);
    }

    private static BoardState getBoardAfterSettlementPhase() throws IOException {
        BoardState b = getBoard();

        // Round 1
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

        // Round 2
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
        BoardState b = getBoardAfterSettlementPhase();
        for (int i = 0; i < 100; i++) {
            assert i % 4 == b.getPlayerTurn();
            b.advanceTurn();
        }
    }

    @Test
    public void advanceTurnTest() throws IOException {
        BoardState b = getBoard();
        byte[] t = new byte[] { 0, 1, 2, 3, 3, 2, 1, 0 };
        for (byte i : t) {
            assert b.getPlayerTurn() == i;
            b.advanceTurn();
        }
        for (byte i = 8; i < 50; i++) {
            assert (b.getPlayerTurn() == (i % 4));
            b.advanceTurn();
        }
    }

    @Test
    public void buildRoadTest() throws IOException {
        BoardState b = getBoardAfterSettlementPhase();
        Player p = b.getCurrentPlayer();
        b.getCurrentPlayer().addResource(WOOD, (byte) 1);
        b.getCurrentPlayer().addResource(BRICK, (byte) 1);
        assert b.canBuildRoad((byte) 8, true);
        assert b.canBuildRoad((byte) 15, true);
        assert b.canBuildRoad((byte) 42, true);
        assert b.canBuildRoad((byte) 50, true);
        b.buildRoad((byte) 8);
        b.getCurrentPlayer().addResource(WOOD, (byte) 1);
        b.getCurrentPlayer().addResource(BRICK, (byte) 1);
        assert b.canBuildRoad((byte) 3, true);
        assert b.canBuildRoad((byte) 4, true);
        b.getCurrentPlayer().addResource(WOOD, (byte) 16);
        b.getCurrentPlayer().addResource(BRICK, (byte) 16);
        b.buildRoad((byte) 4);
        b.buildRoad((byte) 5);
        b.buildRoad((byte) 9);
        b.buildRoad((byte) 16);
        b.buildRoad((byte) 21);
        assert !b.canBuildRoad((byte) 30, true);
        b.buildRoad((byte) 17);
        b.buildRoad((byte) 22);
        b.buildRoad((byte) 31);
        assert !b.canBuildRoad((byte) 37, true);
        b.buildRoad((byte) 32);
        b.buildRoad((byte) 38);
        b.buildRoad((byte) 48);
        b.buildRoad((byte) 53);
        assert !b.canBuildRoad((byte) 61, true);
    }

    @Test
    public void canBuildSettlementTest() throws IOException {
        BoardState b = getBoardAfterSettlementPhase();
        b.getCurrentPlayer().addResource(WOOD, (byte) 5);
        b.getCurrentPlayer().addResource(BRICK, (byte) 5);
        b.getCurrentPlayer().addResource(SHEEP, (byte) 1);
        b.getCurrentPlayer().addResource(HAY, (byte) 1);
        assert !b.canBuildSettlement((byte) 12);
        assert !b.canBuildSettlement((byte) 10);
        assert !b.canBuildSettlement((byte) 11);
        b.buildRoad((byte) 8);
        b.buildRoad((byte) 34);
        b.buildRoad((byte) 25);
        b.buildRoad((byte) 50);
        for (byte i = 0; i < 53; i++) {
            if (i != 4) {
                assert !b.canBuildSettlement(i);
            } else {
                assert b.canBuildSettlement(i);
            }
        }
        b.getCurrentPlayer().addResource(HAY, (byte) 15);
        b.getCurrentPlayer().addResource(ROCK, (byte) 15);
        b.getCurrentPlayer().addResource(BRICK, (byte) 15);
        b.getCurrentPlayer().addResource(SHEEP, (byte) 15);
        b.getCurrentPlayer().addResource(WOOD, (byte) 15);

        b.buildRoad((byte) 3);
        b.buildRoad((byte) 2);
        b.buildRoad((byte) 1);
        b.buildRoad((byte) 0);
        b.buildRoad((byte) 42);
        b.buildSettlement((byte)4);
        assert b.canBuildSettlement((byte)2);
        b.buildSettlement((byte)2);
        assert b.canBuildSettlement((byte)0);
        b.buildSettlement((byte)0);
        assert !b.canBuildSettlement((byte)31);
    }

    @Test
    public void canBuildCityTest() throws IOException {
        BoardState b = getBoardAfterSettlementPhase();
        assert !b.canBuildCity((byte) 11);
        assert !b.canBuildCity((byte) 29);
        b.getCurrentPlayer().addResource(HAY, (byte) 2);
        b.getCurrentPlayer().addResource(ROCK, (byte) 3);
        for (byte i = 0; i < 53; i++) {
            if (i != 11 && i != 29) {
                assert !b.canBuildCity(i);
            } else {
                assert b.canBuildCity(i);
            }
        }

        b.getCurrentPlayer().addResource(HAY, (byte) 15);
        b.getCurrentPlayer().addResource(ROCK, (byte) 15);
        b.getCurrentPlayer().addResource(BRICK, (byte) 15);
        b.getCurrentPlayer().addResource(SHEEP, (byte) 15);
        b.getCurrentPlayer().addResource(WOOD, (byte) 15);

        b.buildRoad((byte) 8);
        b.buildRoad((byte) 3);
        b.buildRoad((byte) 2);
        b.buildRoad((byte) 1);
        b.buildRoad((byte) 0);
        b.buildSettlement((byte)4);
        b.buildSettlement((byte)2);
        b.buildSettlement((byte)0);

        assert b.canBuildCity((byte) 0);
        b.buildCity((byte) 0);
        assert !b.canBuildCity((byte) 0);

        assert b.canBuildCity((byte) 2);
        b.buildCity((byte) 2);
        assert !b.canBuildCity((byte) 2);

        assert b.canBuildCity((byte) 4);
        b.buildCity((byte) 4);
        assert !b.canBuildCity((byte) 4);

        assert b.canBuildCity((byte) 11);
        b.buildCity((byte) 11);
        assert !b.canBuildCity((byte) 11);
        assert !b.canBuildCity((byte) 29);
    }

    @Test
    public void playRobberTest() throws IOException {
        BoardState b = getBoardAfterSettlementPhase();
        //steal from self test
        assert !b.canPlayRobber((byte)5,(byte)0);
        //steal from non-adjacent player
        assert !b.canPlayRobber((byte)5,(byte)1);
        //can steal from either all adjacent players
        assert b.canPlayRobber((byte)10,(byte)1);
        assert b.canPlayRobber((byte)10,(byte)3);
        //stealing correct resource
        Player arr[] = b.getPlayers();
        arr[1].addResource((byte)1,(byte)0);
        System.out.println(b.playRobber((byte)10,(byte)1));
        //bug found does not steal correct resource

        //have to move robber to different spot
        b.playRobber((byte)13,(byte)1);
        assert !b.canPlayRobber((byte)13,(byte)1);

        /*b.buildSettlement((byte) 10);
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
        assert b.getRobberTile() == 4;*/
    }
    @Test
    public void buyDevCardTest() throws IOException {
        BoardState b = getBoardAfterSettlementPhase();
        assert !b.canBuyDevCard();
        b.getCurrentPlayer().addResource(HAY, (byte) 1);
        b.getCurrentPlayer().addResource(ROCK, (byte) 1);
        b.getCurrentPlayer().addResource(SHEEP, (byte) 1);
        assert b.canBuyDevCard();
    }

    @Test
    public void longestRoadTest() throws IOException {
        BoardState b = getBoardAfterSettlementPhase();
        assert b.getCurrentLongestRoad() == 1;
        Player p = b.getCurrentPlayer();
        addResourcesForRoad(p, (byte) 100);
        b.buildRoad((byte) 42);
        assert b.getCurrentLongestRoad() == 2;
        b.buildRoad((byte) 35);
        assert b.getCurrentLongestRoad() == 3;
        b.buildRoad((byte) 27);
        assert b.getCurrentLongestRoad() == 4;
        b.buildRoad((byte) 8);
        assert b.getCurrentLongestRoad() == 4;
        b.buildRoad((byte) 3);
        assert b.getCurrentLongestRoad() == 4;
        b.buildRoad((byte) 2);
        assert b.getCurrentLongestRoad() == 4;
        b.buildRoad((byte) 7);
        assert b.getCurrentLongestRoad() == 5;
        b.buildRoad((byte) 13);
        assert b.getCurrentLongestRoad() == 6;
        b.buildRoad((byte) 20);
        assert b.getCurrentLongestRoad() == 11;
        b.buildRoad((byte) 34);
        assert b.getCurrentLongestRoad() == 12;
        b.buildRoad((byte) 25);
        assert b.getCurrentLongestRoad() == 13;
        b.advanceTurn();
        b.advanceTurn();
        b.advanceTurn();
        p = b.getCurrentPlayer();
        addResourcesForRoad(p, (byte) 10);
        addResourcesForSettlement(p, (byte) 5);
        b.buildRoad((byte) 44);
        b.buildRoad((byte) 43);
        b.buildSettlement((byte) 31);
        assert b.getCurrentLongestRoad() == 9;
    }
    @Test
    public void playKnightCardTest() throws IOException {
        BoardState b = getBoardAfterSettlementPhase();
        //can't play knight without knight card
        assert !b.canPlayKnightCard((byte)10);
        b.getCurrentPlayer().addResource(HAY, (byte) 15);
        b.getCurrentPlayer().addResource(ROCK, (byte) 15);
        b.getCurrentPlayer().addResource(SHEEP, (byte) 15);
        byte knightCount = 0;
        while(true){
            if(knightCount == 3){
                break;
            }
            byte added = b.buyDevCard();
            if(added == KNIGHT){
              knightCount++;
            }
        }
        System.out.println(b.getPlayerWithLargestArmy());
        for(int i = 0; i<4; i++){
            b.advanceTurn();
        }
        System.out.println(b.getPlayerTurn());
        b.playKnightCard((byte)10,(byte)1);
        System.out.println(b.getPlayerWithLargestArmy());
        b.playKnightCard((byte)7,(byte)2);
        System.out.println(b.getPlayerWithLargestArmy());
        b.playKnightCard((byte)17,(byte)2);
        System.out.println(b.getPlayerWithLargestArmy());
        //POSSIBLE BUG game will not let you rob players all the time
    }
    private void addResourcesForRoad(Player p, byte numRoads) {
        p.addResource(WOOD, numRoads);
        p.addResource(BRICK, numRoads);
    }

    private void addResourcesForSettlement(Player p, byte numSettlements) {
        p.addResource(WOOD, numSettlements);
        p.addResource(BRICK, numSettlements);
        p.addResource(SHEEP, numSettlements);
        p.addResource(HAY, numSettlements);
    }

}


