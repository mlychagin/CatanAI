package com.succ.engine;

import java.util.Random;

public class GameEngine {
  static Random randomGen = new Random();
  public BoardState board;

  public void initGame(int numPlayers){
    board = new BoardState(numPlayers);
  }

  private void checkCurrentPlayer(int playerId){
    if(board.getPlayerTurn() != playerId){
      throw new RuntimeException("Not correct players turn");
    }
  }

  public void setUpSettlement(int playerId, byte vertexNum) {
    checkCurrentPlayer(playerId);
    board.placeSettlement(playerId, vertexNum);
  }

  public void setUpRoad(int playerId, byte edgeNum){
    checkCurrentPlayer(playerId);

  }

  public void settlementPhase(){

  }

  public boolean playRound(){
    return false;
  }

}
