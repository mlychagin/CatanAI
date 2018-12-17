package backend;

import java.util.ArrayList;

public class VertexNode {
  /*
   * first = recourse type
   *      0 = Desert
   *      1 = Wood
   *      2 = Brick
   *      3 = Sheep
   *      4 = Hay
   *      5 = Rock
   * second = probability number
   */
  public ArrayList<MutablePair> resources = new ArrayList<>();

  /*
   * first = Trade Amount
   * second = Recourse. 0 is Any
   */
  public MutablePair port = new MutablePair();

  /*
   * first = road status
   *      -1 = no road
   *      0 = Player 0 road
   *      1 = Player 1 road
   *      2 = Player 2 road
   *      ...
   * second = VertexNode number
   */
  public ArrayList<MutablePair> listEdges = new ArrayList<>();

  /*
   * first = player
   *      0 = Player 0 city
   *      1 = Player 1 city
   *      2 = Player 2 city
   *      ...
   * second = strength
   *      1 = Settlement
   *      2 = City
   */
  public MutablePair city = new MutablePair();

  public VertexNode() {}

  public boolean canBuildCity(BoardState boardState) {
    if (city.getSecond() > 0) {
      return false;
    }
    for (MutablePair pair : listEdges) {
      if (boardState.vertexes[pair.getSecond()].city.getSecond() > 0) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return "{ "
        + "\"resources\" : "
        + resources
        + ", \"port\" : "
        + port
        + ", \"listEdges\" : "
        + listEdges
        + ", \"city\" : "
        + city
        + "}";
  }

  public VertexNode clone(VertexNode blankNode) {
    port.clone(blankNode.port);
    city.clone(blankNode.city);
    for (MutablePair pair : resources) {
      blankNode.resources.add(pair.clone(new MutablePair()));
    }
    for (MutablePair pair : listEdges) {
      blankNode.listEdges.add(pair.clone(new MutablePair()));
    }
    return blankNode;
  }

  // Returns the slot of the first adjacent node that doesn't have a road already
  public int getRandomAdjNode() {
    for (MutablePair pair : listEdges) {
      if (pair.getFirst() == -1) {
        return pair.getSecond();
      }
    }
    return -1; // No available adjacent roads
  }

  // TODO----------------------------------------------------------------------
}
