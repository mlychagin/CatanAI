package com.SpringField.engine.util;

import com.SpringField.engine.BoardState;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static com.SpringField.engine.util.Util.*;

public class BoardStateReplay {
    private DataInputStream dis;
    private BoardState boardState;

    public BoardStateReplay(String inputFile) throws IOException {
        dis = new DataInputStream(new FileInputStream(inputFile));
        if(dis.readByte() != SEED_COMMAND) {
            throw new RuntimeException("Invalid Game Start");
        }
        initializedContext = false;
        boardState = new BoardState(null, dis.readByte(), dis.readLong());
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public boolean hasNext() throws IOException {
        return dis.available() > 0;
    }
    
    public void next() throws IOException {
        byte b = dis.readByte();
        System.out.println("Command : " + b);
        switch (b){
            case ROAD_COMMAND:
                buildRoad();
                return;
            case SETTLEMENT_COMMAND:
                buildSettlement();
                return;
            case CITY_COMMAND:
                buildCity();
                return;
            case DEV_CARD_COMMAND:
                buyDevCard();
                return;
            case ROBBER_COMMAND:
                playRobber();
                return;
            case KNIGHT_COMMAND:
                playKnightCard();
                return;
            case ROAD_BUILDING_COMMAND:
                playRoadBuilding();
                return;
            case MONOPOLY_COMMAND:
                playMonopoly();
                return;
            case YEAR_OF_PLENTY_COMMAND:
                playYearOfPlenty();
                return;
            case TRADE_BANK_COMMAND:
                tradeBank();
                return;
            case TRADE_PLAYER_COMMAND:
                tradePlayer();
                return;
            case ADVANCE_TURN_COMMAND:
                advanceTurn();
                return;
            default:
                throw new RuntimeException("Invalid Command");
        }
    }

    private void buildRoad() throws IOException {
        boardState.buildRoad(dis.readByte());
    }

    private void buildSettlement() throws IOException {
        boardState.buildSettlement(dis.readByte());
    }

    private void buildCity() throws IOException {
        boardState.buildCity(dis.readByte());
    }

    private void buyDevCard() throws IOException {
        boardState.buyDevCard();
    }

    private void playRobber() throws IOException {
        boardState.playRobber(dis.readByte(), dis.readByte());
    }

    private void playKnightCard() {
        boardState.getPlayers()[boardState.getPlayerTurn()].playDevCard(KNIGHT);
    }

    private void playRoadBuilding() throws IOException {
        boardState.playRoadBuilding(dis.readByte(), dis.readByte());
    }

    private void playMonopoly() throws IOException {
        boardState.playMonopoly(dis.readByte());
    }

    private void playYearOfPlenty() throws IOException {
        boardState.playYearOfPlenty(dis.readByte(), dis.readByte());
    }

    private void tradeBank() throws IOException {
        boardState.tradeBank(dis.readByte(), dis.readByte());
    }

    private void tradePlayer() throws IOException {
        boardState.tradePlayer(dis.readByte(), readByteArray(), readByteArray());
    }

    private void advanceTurn() throws IOException {
        boardState.advanceTurn();
    }

    private byte[] readByteArray() throws IOException {
        byte length = dis.readByte();
        byte[] a = new byte[length];
        for(int i = 0 ; i < length; i++){
            a[i] = dis.readByte();
        }
        return a;
    }

}
