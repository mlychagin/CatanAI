package com.succ.engine.board;

import static com.succ.engine.util.Util.STATUS_EMPTY;
import static com.succ.engine.util.Util.UNASSIGNED_PLAYER;

public class Vertex {
  private byte playerNumber;
  private byte building;

  public Vertex() {
    playerNumber = UNASSIGNED_PLAYER;
    building = STATUS_EMPTY;
  }

  public void setPlayerNumber(byte playerNumber) {
    this.playerNumber = playerNumber;
  }

  public void setBuilding(byte building) {
    this.building = building;
  }

  public byte getPlayerNumber() {
    return playerNumber;
  }

  public byte getBuilding() {
    return building;
  }

  public boolean isAssigned(){
    return playerNumber != UNASSIGNED_PLAYER;
  }
}
