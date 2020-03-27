package com.succ.engine;

import com.succ.engine.board.Player;
import com.succ.engine.board.Tile;
import com.succ.engine.board.Vertex;

import java.util.ArrayList;
import java.util.Random;

import static com.succ.engine.util.Util.*;

public class BoardState {
  private ArrayList<Vertex> vertices = new ArrayList<>();
  private ArrayList<Byte> edges = new ArrayList<>();
  private ArrayList<Player> playerList = new ArrayList<>();
  private byte[] devCardPool =
            new byte[] { DEFAULT_NUM_KNIGHT, DEFAULT_NUM_VICTORY, DEFAULT_NUM_ROAD_BUILDING, DEFAULT_NUM_MONOPOLY,
                    DEFAULT_NUM_YEAR_OF_PLENTY };
  private byte playerWithLargestArmy = UNASSIGNED_PLAYER;
  private byte playerWithLongestRoad = UNASSIGNED_PLAYER;
  private byte playerTurn = UNASSIGNED_PLAYER;
  private byte robberTile;

  private Random randomGen = new Random();

    BoardState() {
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Byte> getEdges() {
        return edges;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void initBoard(int numPlayers) {
        for (int i = 0; i < DEFAULT_NUM_VERTICES; i++) {
            vertices.add(new Vertex());
        }
        for (int i = 0; i < edgeDependencies.length; i++) {
            edges.add(UNASSIGNED_PLAYER);
        }
        for (int i = 0; i < numPlayers; i++) {
            playerList.add(new Player());
        }
        for (int i = 0; i < tiles.size(); i++) {
            Tile t = tiles.get(i);
            if (t.getResourceType() == DESERT) {
                robberTile = (byte) i;
                break;
            }
        }
    }

    private byte rollDice() {
        byte roll = (byte) (randomGen.nextInt(6) + randomGen.nextInt(6) + 2);
        for (int tileNum = 0; tileNum < tiles.size(); tileNum++) {
            Tile t = tiles.get(tileNum);
            if (roll == t.getRollNumber()) {
                for (int vertexNum : tileDependencies[tileNum]) {
                    Vertex v = vertices.get(vertexNum);
                    if (v.isAssigned()) {
                        Player p = playerList.get(v.getPlayerNumber());
                        p.addResource(t.getResourceType(), v.getBuilding());
                    }
                }
            }
        }
        return roll;
    }

    private byte computeArmy(byte playerId) {
        return playerList.get(playerId).getKnightsPlayed();
    }

    private byte computeVictoryPoints(byte playerId) {
        byte points = playerList.get(playerId).getNumVictoryPoints();
        if (playerWithLargestArmy == playerId) {
            points += 2;
        }
        if (playerWithLongestRoad == playerId) {
            points += 2;
        }
        return points;
    }

    /*
     * Todo
     */

    private byte computeRoad(byte playerId) {
        return 0;
    }

    private void updateLargestArmy() {

    }

    private void updateLargestRoad() {

    }

}
