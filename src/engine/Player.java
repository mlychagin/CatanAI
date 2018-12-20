package engine;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {
  /*
   * 0 = Desert
   * 1 = Wood
   * 2 = Brick
   * 3 = Sheep
   * 4 = Hay
   * 5 = Rock
   */
  int[] materials = new int[] {0, 0, 0, 0, 0, 0};

  /*
   * 0 = Roads
   * 1 = Settlements
   * 2 = Cities
   */
  int[] buildMaterials = new int[] {20, 5, 4};

  /*
   * 0 = Knight
   * 1 = Victory
   * 2 = Road Building
   * 3 = Monopoly
   * 4 = Year of Plenty
   */
  int[] devCardsHolding = new int[] {0, 0, 0, 0, 0};
  int[] devCardsSeen = new int[] {0, 0, 0, 0, 0};
  ArrayList<Integer> currentCities = new ArrayList<>();
  ArrayList<Integer> currentRoads = new ArrayList<>();

  int currentRoad;
  int currentArmy;

  Player() {
    currentRoad = 0;
    currentArmy = 0;
  }

  public void addResources(BoardState boardState, int dieRoll) {
    // TODO:
    /*boolean robbed = false;
    int[] robbedTiles = GameEngine.resourceDependencies[boardState.robberTile];
    for (Integer slot : currentCities) {
      for (int i = 1; i < robbedTiles.length; i++) {
        if (robbedTiles[i] == slot) {
          robbed = true;
        }
      }
      if (robbed) {
        robbed = false;
        continue;
      }
      VertexNode node = boardState.vertices[slot];
      int strength = node.city.getSecond();
      for (MutablePair pair : node.resources) {
        if (pair.getSecond() == dieRoll) {
          materials[pair.getFirst()] += strength;
        }
      }
    }*/
  }

  public int getVictoryPoints(){
      return 0;
  }

  // Todo: Add error checking
  public void buildSettlement(BoardState boardState, int slot, boolean pay) {
    // TODO:
    /*VertexNode node = boardState.vertices[slot];
    if (pay) {
      materials[1] = -1;
      materials[2] = -1;
      materials[3] = -1;
      materials[4] = -1;
    }
    buildMaterials[1] -= 1;
    addProduction(node);
    node.city.set(playerNumber, 1);
    currentCities.add(slot);
    System.out.println("Player(" + playerNumber + ") : BuildSettlement(" + slot + ")");*/
  }

  // Todo: Add error checking
  public void buildCity(BoardState boardState, int slot) {
    // TODO:
    /*VertexNode node = boardState.vertices[slot];
    materials[4] = -2;
    materials[5] = -3;
    addProduction(node);
    node.city.setSecond(2);*/
  }

  private void addProduction(VertexNode node) {
    // TODO
    /*for (MutablePair pair : node.resources) {
      production[pair.getFirst()] += GameEngine.getRarity(pair.getSecond());
    }
    production[0] = 0;*/
  }

  public void buyDevCard(int kind) {
    materials[3] = -1;
    materials[4] = -1;
    materials[5] = -1;
  }

  @Override
  public String toString() {
    return "{ "
        + "\"materials\" : "
        + Arrays.toString(materials)
        + ", \"buildMaterials\" : "
        + Arrays.toString(buildMaterials)
        + ", \"devCardsHolding\" : "
        + Arrays.toString(devCardsHolding)
        + ", \"devCardsSeen\" : "
        + Arrays.toString(devCardsSeen)
        + ", \"currentCities\" : "
        + Arrays.toString(currentCities.toArray())
        + ", \"currentRoad\" : "
        + currentRoad
        + ", \"currentArmy\" : "
        + currentArmy
        + "}";
  }

  public Player clone(Player player) {
    System.arraycopy(materials, 0, player.materials, 0, Util.MATERIALS_LENGTH);
    System.arraycopy(buildMaterials, 0, player.buildMaterials, 0, Util.BUILD_LENGTH);
    System.arraycopy(devCardsHolding, 0, player.devCardsHolding, 0, Util.DEV_LENGTH);
    System.arraycopy(devCardsSeen, 0, player.devCardsSeen, 0, Util.DEV_LENGTH);
    player.currentCities.addAll(currentCities);
    player.currentRoad = currentRoad;
    player.currentArmy = currentArmy;
    return player;
  }

  // TODO----------------------------------------------------------------------

  // Ideas: Error check to see if buildRoad/buildSettlement/buildCity can actually build

  /*
   * Builds a road given two Vertexes
   */
  /*public void buildRoad(BoardState boardState, int startSlot, int endSlot) {
    materials[1] -= 1;
    materials[2] -= 2;
    VertexNode node1 = boardState.vertices[startSlot];
    VertexNode node2 = boardState.vertices[endSlot];

    for (MutablePair pair : node1.listEdges) {
      if (pair.getSecond() == endSlot && pair.getFirst() == -1) {
        pair.setFirst(playerNumber);
      }
    }

    for (MutablePair pair : node2.listEdges) {
      if (pair.getSecond() == startSlot && pair.getFirst() == -1) {
        pair.setFirst(playerNumber);
      }
    }
  }*/

  /*
   * Draws a dev card from the bank and adds it to current player's hand
   *    True = Draw successful
   *    False = Draw unsuccessful
   */
  public boolean drawDevCard(int[] devCardPool) {
    return false;
  }

  /*
   * Calculate longestRoad that the player has
   */
  public int calculateLongestRoad() {
    return -1;
  }
}
