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
  int[] buildMaterials = new int[] {15, 5, 4};

  /*
   * 0 = Knight
   * 1 = Victory
   * 2 = Road Building
   * 3 = Monopoly
   * 4 = Year of Plenty
   */
  int[] devCardsHolding = new int[] {0, 0, 0, 0, 0};

  // The devCards played by player
  int[] devCardsSeen = new int[]{0, 0, 0, 0, 0};

  /*
   * First = Vertex Number
   * Second = Strength
   */
  ArrayList<MutablePair> currentCities = new ArrayList<>();
  ArrayList<MutablePair> currentRoads = new ArrayList<>();

  int currentRoad = 0;
  private int victoryPoints = 0;

  Player(){}

  private boolean isRobbed(int[] robbedVertices, int slot) {
    for (int i = 1; i < robbedVertices.length; i++) {
      if (robbedVertices[i] == slot) {
        return true;
      }
    }
    return false;
  }

  public void addResources(int robberTile, int dieRoll) {
    int[] robbedVertices = Util.resourceDependencies[robberTile];
    for (MutablePair p : currentCities) {
      if (isRobbed(robbedVertices, p.getFirst())) {
        continue;
      }
      VertexNode node = GameEngine.vertices[p.getFirst()];
      // Look at resources adjacent to city nodes and add to player resources
      for (MutablePair pair : node.resources) {
        if (pair.getSecond() == dieRoll) {
          materials[pair.getFirst()] += p.getSecond();
        }
      }
    }
  }

  public int getVictoryPoints() {
    return this.victoryPoints;
  }

  public void buildSettlement(int slot, boolean pay) {
    if (pay) {
      materials[1] -=1;
      materials[2] -=1;
      materials[3] -=1;
      materials[4] -=1;
    }
    buildMaterials[1] -= 1;
    currentCities.add(new MutablePair(slot, 1));
  }

  MutablePair getSettlement(int slot) {
    for (MutablePair p : currentCities) {
      if (p.getFirst() == slot && p.getSecond() == 1) {
        return p;
      }
    }
    return null;
  }

  public void buildCity(int slot) {
    MutablePair p = getSettlement(slot);
    assert p != null;
    materials[4] -=2;
    materials[5] -=3;
    p.setSecond(2);
  }

  public void buyDevCard(int kind) {
    // Draw the devCard using the Util Class
    int devCard = Util.drawDevCard();
    materials[3] -=1;
    materials[4] -=1;
    materials[5] -=1;

    // Increment the devCard location
    devCardsHolding[devCard] += 1;
  }

  public void playDevCard(int cardToPlay) {
    if (cardToPlay > 5 || cardToPlay < 0) {
      return;
    }
    if (devCardsHolding[cardToPlay] == 0) {
      return;
    }
    devCardsHolding[cardToPlay]--;
    devCardsSeen[cardToPlay]++;
  }

  public void buildRoad(int startSlot, int endSlot) {
    materials[1] -= 1;
    materials[2] -= 2;
    currentRoads.add(new MutablePair(startSlot, endSlot));
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
        + "}";
  }

  public Player clone(Player player) {
    System.arraycopy(materials, 0, player.materials, 0, Util.MATERIALS_LENGTH);
    System.arraycopy(buildMaterials, 0, player.buildMaterials, 0, Util.BUILD_LENGTH);
    System.arraycopy(devCardsHolding, 0, player.devCardsHolding, 0, Util.DEV_LENGTH);
    System.arraycopy(devCardsSeen, 0, player.devCardsSeen, 0, Util.DEV_LENGTH);
    player.currentCities.addAll(currentCities);
    player.currentRoad = currentRoad;
    return player;
  }

  // TODO----------------------------------------------------------------------
  /*
   * Calculate longestRoad that the player has
   */
  public int calculateLongestRoad() {
    return -1;
  }

}
