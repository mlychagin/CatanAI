package engine;

import java.util.Random;

public class GameEngine {
  public static VertexNode[] vertices = new VertexNode[54];
  static Random randomGen = new Random();
  public BoardState root;

  private void initVertexes() {
    for (int i = 0; i < vertices.length; i++) {
      VertexNode node = new VertexNode();
      vertices[i] = node;
    }
  }

  private void setVertexDependencies() {
    for (int[] numberSet : Util.vertexDependencies) {
      VertexNode currentVertex = vertices[numberSet[0]];
      for (int i = 1; i < numberSet.length; i++) {
        MutablePair pair = new MutablePair();
        pair.set(-1, numberSet[i]);
        currentVertex.listEdges.add(pair);
      }
    }
  }

  public void initGame(int playerSize) {
    BoardState.randomizeArray(Util.tilesResource);
    BoardState.randomizeArray(Util.tilesNumber);
    initVertexes();
    setVertexDependencies();
    root = new BoardState();
    root.initBoard();
    root.initPlayers(playerSize);
  }

  public void settlementPhase() {
    for (Player p : root.playerList) {
      root.startSettlement(p);
    }
    for (int i = root.playerList.size() - 1; i >= 0; i--) {
      root.startSettlement(root.playerList.get(i));
    }
  }

  public boolean playRound() {
    if (!getWinner(root)) {
      int dieRoll = rollDice();
      System.out.println("DieRoll(" + dieRoll + ")");
      root.applyDice(dieRoll);
      root.nextPlayer();
      return true;
    }
    return false;
  }

  public void playGame() {
    System.out.println("Settlement Phase Begin");
    settlementPhase();
    System.out.println("Settlement Phase End");
    while (!getWinner(root)) {
      playRound();
    }
    System.out.println(toString());
  }

  public static boolean getWinner(BoardState boardState) {
    return boardState.playerList.get(boardState.playerTurn).getVictoryPoints() == 0;
  }

  @Override
  public String toString() {
    return "{\"GameEngine\" : {"
        + "\"vertexList\" : {"
        + vertexListToString()
        + "}, \"root\" : "
        + root
        + "}}";
  }

  private String vertexListToString() {
    StringBuilder returnString = new StringBuilder();
    boolean first = true;
    for (int i = 0; i < vertices.length; i++) {
      if (!first) {
        returnString.append(",");
      }
      returnString.append("\"").append(i).append("\": ").append(vertices[i].toString());
      first = false;
    }
    return returnString.toString();
  }

  public static int rollDice() {
    return randomGen.nextInt(6) + randomGen.nextInt(6) + 2;
  }

  public static int getRarity(int resourceNumber) {
    if (resourceNumber <= 7) {
      return resourceNumber - 1;
    } else {
      return 13 - resourceNumber;
    }
  }

  // TODO----------------------------------------------------------------------

}
