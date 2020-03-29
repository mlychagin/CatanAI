package com.SpringField.engine.ai;

import com.SpringField.engine.board.Player;
import com.SpringField.engine.BoardState;
import com.SpringField.engine.board.Vertex;
import org.junit.Test;

import java.util.Random;

import static com.SpringField.engine.util.Util.*;
import static com.SpringField.engine.util.Util.KNIGHT;

public class settleTest {
    @Test
    public void canInitBoardstate(){
        BoardState test = new BoardState(4);

        // vertices
        Vertex[] vertexes = test.getVertices();
        for (Vertex v: vertexes){
            System.out.println(v);
        }

        // edges
        byte[] edges = test.getEdges();
        for (byte e: edges){
            System.out.println(e);
        }


        // players
        Player[] players = test.getPlayers();
        for (Player p: players){
            System.out.println(p);
        }

        // TODO:
        // resourceCardPool
        // devCardPool
        // playerWithLargestArmy
        // playerWithLongestRoad
        // currentLongestRoad
        // playerTurn
    }

}
