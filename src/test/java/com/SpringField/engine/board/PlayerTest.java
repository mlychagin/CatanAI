package com.SpringField.engine.board;

import org.junit.Test;
import org.junit.*;

import java.util.Random;

import static com.SpringField.engine.util.Util.*;

public class PlayerTest {

    @Test
    public void canBuySettlementTest() {
        Player p = new Player();
        p.addResource(WOOD, (byte) 1);
        p.addResource(BRICK, (byte) 1);
        p.addResource(SHEEP, (byte) 1);
        p.addResource(HAY, (byte) 1);
        assert p.canBuySettlement();
        p.buySettlement(true);
        try {
            p.buySettlement(true);
            assert false;
        } catch (Exception e) {
            assert e.getMessage().equals("Invalid Transaction");
        }
    }

    @Test
    public void canBuyRoadTest() {
        Player p = new Player();
        p.addResource(WOOD, (byte) 1);
        p.addResource(BRICK, (byte) 1);
        assert p.canBuyRoad();
        p.buyRoad(true);
        try {
            p.buyRoad(true);
            assert false;
        } catch (Exception e) {
            assert e.getMessage().equals("Invalid Transaction");
        }
    }

    @Test
    public void canBuyCityTest() {
        Player p = new Player();
        p.addResource(ROCK, (byte) 3);
        p.addResource(HAY, (byte) 2);
        assert p.canBuyCity();
        p.buyCity();
        try {
            p.buyCity();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().equals("Invalid Transaction");
        }
    }

    @Test
    public void canBuyDevCardTest() {
        Player p = new Player();
        p.addResource(SHEEP, (byte) 1);
        p.addResource(ROCK, (byte) 1);
        p.addResource(HAY, (byte) 1);
        assert p.canBuyDevCard();
        p.buyDevCard(KNIGHT);
        try {
            p.buyDevCard(KNIGHT);
            assert false;
        } catch (Exception e) {
            assert e.getMessage().equals("Invalid Transaction");
        }
    }

    @Test
    public void settlementStructuresTest() {
        Player p = new Player();
        p.addResource(WOOD, (byte) (1 + DEFAULT_SETTLEMENT_COUNT));
        p.addResource(BRICK, (byte) (1 + DEFAULT_SETTLEMENT_COUNT));
        p.addResource(SHEEP, (byte) (1 + DEFAULT_SETTLEMENT_COUNT));
        p.addResource(HAY, (byte) (1 + DEFAULT_SETTLEMENT_COUNT));
        int i = 0;
        while (true) {
            if (!p.canBuySettlement()) {
                try {
                    p.buySettlement(true);
                    assert false;
                } catch (Exception e) {
                    assert e.getMessage().equals("Invalid Transaction");
                }
                assert (i == DEFAULT_SETTLEMENT_COUNT);
                break;
            }
            p.buySettlement(true);
            i += 1;
        }
    }

    @Test
    public void cityStructuresTest() {
        Player p = new Player();
        int i = 0;
        p.addResource(ROCK, (byte) (1 + DEFAULT_CITY_COUNT * 3));
        p.addResource(HAY, (byte) (1 + DEFAULT_CITY_COUNT * 2));
        while (true) {
            if (!p.canBuyCity()) {
                try {
                    p.buyCity();
                    assert false;
                } catch (Exception e) {
                    assert e.getMessage().equals("Invalid Transaction");
                }
                assert (i == DEFAULT_CITY_COUNT);
                break;
            }
            p.buyCity();
            i += 1;
        }
    }

    @Test
    public void roadStructuresTest() {
        Player p = new Player();
        int i = 0;
        p.addResource(WOOD, (byte) (1 + DEFAULT_ROAD_COUNT));
        p.addResource(BRICK, (byte) (1 + DEFAULT_ROAD_COUNT));
        while (true) {
            if (!p.canBuyRoad()) {
                try {
                    p.buyRoad(true);
                    assert false;
                } catch (Exception e) {
                    assert e.getMessage().equals("Invalid Transaction");
                }
                assert (i == DEFAULT_ROAD_COUNT);
                break;
            }
            p.buyRoad(true);
            i += 1;
        }
    }

    @Test
    public void canPlayDevCardTest() {
        Player p = new Player();
        for (byte i = 0; i < 5; i++) {
            assert !p.canPlayDevCard(i);
        }
        p.addResource(SHEEP, (byte) 1);
        p.addResource(ROCK, (byte) 1);
        p.addResource(HAY, (byte) 1);
        try {
            p.playDevCard(VICTORY);
            assert false;
        } catch (Exception e) {
            assert e.getMessage().equals("Invalid Transaction");
        }
    }

    @Test
    public void playDevCardTest() {
        Player p = new Player();
        for (byte i = 0; i < 5; i++) {
            try {
                p.playDevCard(i);
                assert false;
            } catch (Exception e) {
                assert e.getMessage().equals("Invalid Transaction");
            }
        }
        for (byte i = 0; i < 5; i++) {
            p.addResource(SHEEP, (byte) 1);
            p.addResource(ROCK, (byte) 1);
            p.addResource(HAY, (byte) 1);
            p.buyDevCard(i);
            p.playDevCard(i);
            try {
                p.playDevCard(i);
                assert false;
            } catch (Exception e) {
                assert e.getMessage().equals("Invalid Transaction");
            }
        }

    }

    @Test
    public void getNumVictoryPointsTest() {
        Player p = new Player();
        p.addResource(WOOD, (byte) (1));
        p.addResource(SHEEP, (byte) (1));
        p.addResource(HAY, (byte) (1));
        p.addResource(BRICK, (byte) (1));
        p.buySettlement(true);
        assert (p.getNumVictoryPoints() == 1);
        p.addResource(ROCK, (byte) (3));
        p.addResource(HAY, (byte) (2));
        p.buyCity();
        assert (p.getNumVictoryPoints() == 2);
        p.addResource(ROCK, (byte) (1));
        p.addResource(SHEEP, (byte) (1));
        p.addResource(HAY, (byte) (1));
        p.buyDevCard(VICTORY);
        assert (p.getNumVictoryPoints() == 3);
    }

    @Test
    public void getTotalResourceCountTest() {
        Player p = new Player();
        assert (p.getTotalResourceCount() == 0);
        p.addResource(WOOD, (byte) (1));
        p.addResource(BRICK, (byte) (1));
        p.addResource(SHEEP, (byte) (1));
        p.addResource(HAY, (byte) 1);
        assert (p.getTotalResourceCount() == 4);
        p.buySettlement(true);
        assert (p.getTotalResourceCount() == 0);
        p.addResource(ROCK, (byte) (3));
        p.addResource(HAY, (byte) (2));
        assert (p.getTotalResourceCount() == 5);
        p.buyCity();
        assert (p.getTotalResourceCount() == 0);
        for (byte i = 1; i < 50; i++) {
            Random r = new Random();
            byte type = (byte) r.nextInt(5);
            p.addResource(type, (byte) 1);
            assert (p.getTotalResourceCount() == i);
        }
    }

    @Test
    public void getKnightsPlayedTest() {
        Player p = new Player();
        assert p.getKnightsPlayed() == 0;
        p.addResource(ROCK, (byte) (1));
        p.addResource(SHEEP, (byte) (1));
        p.addResource(HAY, (byte) (1));
        p.buyDevCard(KNIGHT);
        assert p.getKnightsPlayed() == 0;
        p.playDevCard(KNIGHT);
        assert p.getKnightsPlayed() == 1;
    }

    @Test
    public void stealResourceTest() {
        Player p = new Player();
        try {
            p.stealResource();
            assert false;
        } catch (Exception e) {
            e.getMessage().equals("Algorithm Failure");
        }
        for (byte i = 0; i < 5; i++) {
            p.addResource(i, (byte) 1);
        }
        byte stolenType1 = p.stealResource();
        assert p.getResources()[stolenType1] == 0;
        byte stolenType2 = p.stealResource();
        assert p.getResources()[stolenType2] == 0;
        assert (stolenType1 != stolenType2);
    }

    @Test
    public void nameTest() {
        Player p = new Player();
    }
}
