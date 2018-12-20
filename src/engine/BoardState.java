package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BoardState {
  /*
   * 0 = Player 1
   * 1 = Player 2
   * ...
   */
  ArrayList<Player> playerList = new ArrayList<>();
  int[] devCardPool = new int[] {14, 5, 2, 2, 2};
  int playerTurn = 0;
  int robberTile;

  int[] resourceScarcity = new int[] {0, 0, 0, 0, 0, 0};

  BoardState() {}

  public void initBoard() {
    setVertexResources();
    setResourceScarcity();
  }

  public void setVertexResources() {
    int[] tilesResource = GameEngine.tilesResource;
    int[] tilesNumber = GameEngine.tilesNumber;
    int slotResource = 0;
    int slotNumber = 0;
    for (int i = 0; i < tilesResource.length; i++) {
      if (tilesResource[i] == 0) {
        slotResource = i;
        break;
      }
    }
    for (int i = 0; i < tilesNumber.length; i++) {
      if (tilesNumber[i] == 7) {
        slotNumber = i;
        break;
      }
    }
    int save = tilesResource[slotNumber];
    tilesResource[slotNumber] = 0;
    tilesResource[slotResource] = save;
    robberTile = slotNumber;

    for (int[] numberSet : GameEngine.resourceDependencies) {
      int currentTile = numberSet[0];
      MutablePair tile = new MutablePair();
      tile.set(GameEngine.tilesResource[currentTile], GameEngine.tilesNumber[currentTile]);
    }
  }

  public void setResourceScarcity() {
    for (int i = 0; i < GameEngine.tilesNumber.length; i++) {
      resourceScarcity[GameEngine.tilesResource[i]] +=
          GameEngine.getRarity(GameEngine.tilesNumber[i]);
    }
    resourceScarcity[0] = 0;
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
    //TODO:
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
      player.addResources(this, dieRoll);
    }
  }

  @Override
  public String toString() {
    return "{ "
        + "\"playerList\" : {"
        + playerListToString()
        + "}, \"devCardPool\" : "
        + Arrays.toString(devCardPool)
        + ", \"playerTurn\" : "
        + playerTurn
        + ", \"robberTile\" : "
        + robberTile
        + ", \"resourceScarcity\" : "
        + Arrays.toString(resourceScarcity)
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

  // TODO----------------------------------------------------------------------
  public void setRobber(int tileNumber) {}

  public void playerRobber() {}

  public void applyRobber(int tile, int person, int resource) {}

  public void shortestPath(VertexNode nodeStart, VertexNode nodeEnd) {}

  /*
   * Initialize the ports
   * Randomize, similar to how randomizing tiles and numbers works
   */
  public void initPorts() {}
}
