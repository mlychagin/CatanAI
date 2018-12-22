package engine;

import java.util.ArrayList;
import java.util.Arrays;

public class BoardState {
  /*
   * 0 = Player 1
   * 1 = Player 2
   * ...
   */
  ArrayList<Player> playerList = new ArrayList<>();

  // -1 if no player, updated after every dev card drawn
  int playerWithLargestArmy = -1;
  int playerTurn = 0;
  int robberTile;
  /*
   * 0 = Knight
   * 1 = Victory
   * 2 = Road Building
   * 3 = Monopoly
   * 4 = Year of Plenty
   */
  private int[] devCardPool = new int[] {14, 5, 2, 2, 2};

  BoardState() {}

  public void initBoard() {
    setVertexResources();
  }

  public static int getRobberResourceSlot() {
    for (int i = 0; i < Util.tilesResource.length; i++) {
      if (Util.tilesResource[i] == 0) {
        return i;
      }
    }
    System.out.println("Invalid Robber Resource Slot");
    System.exit(1);
    return 0;
  }

  public static int getRobberNumberSlot() {
    for (int i = 0; i < Util.tilesNumber.length; i++) {
      if (Util.tilesNumber[i] == 7) {
        return i;
      }
    }
    System.out.println("Invalid Robber Number Slot");
    System.exit(1);
    return 0;
  }

  public void setVertexResources() {
    int slotResource = getRobberResourceSlot();
    int slotNumber = getRobberNumberSlot();
    int save = Util.tilesResource[slotNumber];
    Util.tilesResource[slotNumber] = 0;
    Util.tilesResource[slotResource] = save;
    robberTile = slotNumber;

    for (int[] numberSet : Util.resourceDependencies) {
      int currentTile = numberSet[0];
      GameEngine.vertices[currentTile].resources.add(
          new MutablePair(Util.tilesResource[currentTile], Util.tilesNumber[currentTile]));
    }
  }

  public static void randomizeArray(int[] array) {
    for (int i = 0; i < array.length; i++) {
      int randomPosition = GameEngine.randomGen.nextInt(array.length);
      int temp = array[i];
      array[i] = array[randomPosition];
      array[randomPosition] = temp;
    }
  }

  public void initPlayers(int playerSize) {
    for (int i = 0; i < playerSize; i++) {
      playerList.add(new Player());
    }
  }

  public void startSettlement(Player player) {
    // TODO:
    /*ArrayList<MutablePair> topSettles = topSettles();
    topSettles.sort(Comparator.comparing(MutablePair::getFirst));
    int slot = topSettles.get(topSettles.size() - 1).getSecond();
    int roadSlot = vertices[slot].getRandomAdjNode();
    playerList.get(playerID).buildSettlement(this, slot, false);
    if (roadSlot != -1) {
      playerList.get(playerID).buildRoad(this, slot, roadSlot);
    }*/
  }

  public ArrayList<MutablePair> topSettles() {
    // TODO:
    /*Player currentPlayer = playerList.get(playerTurn);
    ArrayList<MutablePair> returnArray = new ArrayList<>();
    for (int i = 0; i < vertices.length; i++) {
      VertexNode node = vertices[i];
      if (!node.canBuildCity(this)) {
        continue;
      }
      MutablePair pair = new MutablePair();
      double total = 0;

      int[] production = currentPlayer.production.clone();
      for (MutablePair resource : node.resources) {
        if (resourceScarcity[resource.getFirst()] != 0) {
          double rarity = GameEngine.getRarity(resource.getSecond());
          total +=
              (rarity / resourceScarcity[resource.getFirst()])
                  * (rarity / (rarity + production[resource.getFirst()]));
          production[resource.getFirst()] += (int) rarity;
        }
      }
      pair.set((int) (total * 100), i);
      returnArray.add(pair);
    }
    return returnArray;*/
    return null;
  }

  public void applyDice(int dieRoll) {
    for (Player player : playerList) {
      player.addResources(robberTile, dieRoll);
    }
  }

  @Override
  public String toString() {
    return "{ "
        + "\"playerList\" : ["
        + playerListToString()
        + "], \"devCardPool\" : "
        + Arrays.toString(devCardPool)
        + ", \"playerTurn\" : "
        + playerTurn
        + ", \"robberTile\" : "
        + robberTile
        + "}";
  }

  private String playerListToString() {
    StringBuilder returnString = new StringBuilder();
    boolean first = true;
    for (Player player : playerList) {
      if (!first) {
        returnString.append(",");
      }
      returnString.append(player.toString());
      first = false;
    }
    return returnString.toString();
  }

  public void clone(BoardState blankState) {
    for (Player player : playerList) {
      blankState.playerList.add(player.clone(new Player()));
    }
    blankState.playerTurn = playerTurn;
    blankState.robberTile = robberTile;
    System.arraycopy(devCardPool, 0, blankState.devCardPool, 0, Util.DEV_LENGTH);
  }

  public void nextPlayer() {
    playerTurn++;
    if (playerTurn == playerList.size()) {
      playerTurn = 0;
    }
  }

  public Player getPlayer(int playerNumber) {
    if (playerNumber > playerList.size() || playerNumber < 0) {
      return null;
    }
    return playerList.get(playerNumber);
  }

  // TODO----------------------------------------------------------------------
  public void setRobber(int tileNumber) {}

  public void playerRobber() {}

  public void applyRobber(int tile, int player, int resource) {
  }

  public void shortestPath(VertexNode nodeStart, VertexNode nodeEnd) {}

  /**
   * Compares dev cards of current holder and player who just played card
   *
   * @param player who just played dev card
   */
  private void updateLargestArmy(int player) {
    int contenderNumKnightCards = playerList.get(player).devCardsSeen[0];
    if (playerWithLargestArmy == -1) {
      if (contenderNumKnightCards > 1) {
        playerWithLargestArmy = player;
        return;
      }
      return;
    }
    int currNumKnightCards = playerList.get(playerWithLargestArmy).devCardsSeen[0];
    if (contenderNumKnightCards > currNumKnightCards) {
      playerWithLargestArmy = player;
    }
  }

  public void playDevCard(int player, int cardToPlay) {
    playerList.get(player).playDevCard(cardToPlay);
    // TODO: dev card action
    updateLargestArmy(player);
  }

  /*
   * Initialize the ports
   * Randomize, similar to how randomizing tiles and numbers works
   */
  public void initPorts() {}
}
