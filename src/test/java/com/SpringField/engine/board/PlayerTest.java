package com.SpringField.engine.board;

import org.junit.Test;

import static com.SpringField.engine.util.Util.BRICK;
import static com.SpringField.engine.util.Util.WOOD;

public class PlayerTest {
    @Test
    public void buyRoadTest(){
        Player P = new Player();
        P.addResource(WOOD, (byte) 1);
        P.addResource(BRICK, (byte) 1);
        assert P.canBuyRoad();
        P.buyRoad();
    }
    
}
