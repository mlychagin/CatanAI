package com.SpringField.engine.board;

import org.junit.Test;

import java.util.Random;

import static com.SpringField.engine.util.Util.*;

public class PlayerTest {

    @Test public void freeRoadTest() {
        Player p = new Player();
        assert !p.canBuyRoad();
        p.addResource(WOOD, (byte) 1);
        p.addResource(BRICK, (byte) 1);
        assert p.canBuyRoad();
        p.buyRoad(false);
        assert p.canBuyRoad();
    }

    @Test public void canBuyRoadTest() {
        Player p = new Player();
        assert !p.canBuyRoad();
        p.addResource(WOOD, (byte) 1);
        p.addResource(BRICK, (byte) 1);
        assert p.canBuyRoad();
        p.buyRoad(true);
        assert !p.canBuyRoad();
    }

    @Test
    public void freeSettlementTest() {
        Player p = new Player();
        assert !p.canBuySettlement();
        p.addResource(WOOD, (byte) 1);
        p.addResource(BRICK, (byte) 1);
        p.addResource(SHEEP, (byte) 1);
        p.addResource(HAY, (byte) 1);
        assert p.canBuySettlement();
        p.buySettlement(false);
        assert p.canBuySettlement();
    }

    @Test
    public void canBuySettlementTest() {
        Player p = new Player();
        assert !p.canBuySettlement();
        p.addResource(WOOD, (byte) 1);
        p.addResource(BRICK, (byte) 1);
        p.addResource(SHEEP, (byte) 1);
        p.addResource(HAY, (byte) 1);
        assert p.canBuySettlement();
        p.buySettlement(true);
        assert !p.canBuySettlement();
    }

    @Test
    public void canBuyCityTest() {
        Player p = new Player();
        assert !p.canBuyCity();
        p.addResource(ROCK, (byte) 3);
        p.addResource(HAY, (byte) 2);
        assert p.canBuyCity();
        p.buyCity();
        assert !p.canBuyCity();
    }

    @Test
    public void canBuyDevCardTest() {
        Player p = new Player();
        assert !p.canBuyDevCard();
        p.addResource(SHEEP, (byte) 1);
        p.addResource(ROCK, (byte) 1);
        p.addResource(HAY, (byte) 1);
        assert p.canBuyDevCard();
        p.buyDevCard(KNIGHT);
        assert !p.canBuyDevCard();
    }

    @Test
    public void maxRoadBuyTest() {
        Player p = new Player();
        p.addResource(WOOD, (byte) 1);
        p.addResource(BRICK, (byte) 1);
        for(int i = 0; i < DEFAULT_ROAD_COUNT; i++){
            p.buyRoad(false);
        }
        assert p.canBuyRoad();
    }

    @Test
    public void maxSettlementBuyTest() {
        Player p = new Player();
        p.addResource(WOOD, (byte) (1 + DEFAULT_SETTLEMENT_COUNT));
        p.addResource(BRICK, (byte) (1 + DEFAULT_SETTLEMENT_COUNT));
        p.addResource(SHEEP, (byte) (1 + DEFAULT_SETTLEMENT_COUNT));
        p.addResource(HAY, (byte) (1 + DEFAULT_SETTLEMENT_COUNT));
        for(int i = 0; i < DEFAULT_SETTLEMENT_COUNT; i++){
            p.buySettlement(true);
        }
        assert !p.canBuySettlement();
    }

    @Test
    public void maxCityBuyTest() {
        Player p = new Player();
        p.addResource(ROCK, (byte) (1 + DEFAULT_CITY_COUNT * 3));
        p.addResource(HAY, (byte) (1 + DEFAULT_CITY_COUNT * 2));
        for(int i = 0; i < DEFAULT_CITY_COUNT; i++){
            p.buyCity();
        }
        assert !p.canBuyCity();
    }

    @Test
    public void playDevCardTest() {
        Player p = new Player();
        p.addResource(SHEEP, DEFAULT_DEV_TYPES);
        p.addResource(ROCK, DEFAULT_DEV_TYPES);
        p.addResource(HAY, DEFAULT_DEV_TYPES);
        for (byte i = KNIGHT; i <= VICTORY; i++) {
            assert !p.canPlayDevCard(i);
            p.buyDevCard(i);
            assert i == VICTORY || p.canPlayDevCard(i);
        }
    }

    @Test
    public void tradeBankTest(){
        for(byte i = WOOD; i <= ROCK; i++){
            byte j = (byte) (i+1);
            if(j > ROCK){
                j = WOOD;
            }
            Player p = new Player();

            p.addResource(i, (byte) 4);
            assert p.canTradeBank(i);
            p.tradeBank(i, j);
            assert !p.canTradeBank(i);

            p.addResource(i, (byte) 3);
            p.addPort(ANY);
            assert p.canTradeBank(i);
            p.tradeBank(i, j);
            assert !p.canTradeBank(i);

            p.addResource(i, (byte) 2);
            p.addPort(i);
            assert p.canTradeBank(i);
            p.tradeBank(i, j);
            assert !p.canTradeBank(i);
        }
    }

    @Test
    public void stealResourceTest() {
        Random r = new Random();
        Player p = new Player();
        for(byte i = WOOD; i <= ROCK; i++){
            p.addResource(i, (byte) 64);
        }
        while(p.getTotalResourceCount() > 0){
            byte[] resources = p.getResources().clone();
            byte stolenType = p.stealResource(r);
            assert p.getResources()[stolenType] == resources[stolenType] - 1;

        }
    }

    @Test
    public void stealAllResource(){
        Player p = new Player();
        byte rCount = 64;
        for(byte i = WOOD; i <= ROCK; i++){
            p.addResource(i, rCount);
            assert p.stealAllResource(i) == rCount;
            assert p.getResources()[i] == 0;
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

}
