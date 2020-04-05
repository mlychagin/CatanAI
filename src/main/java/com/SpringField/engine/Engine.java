package com.SpringField.engine;

import com.SpringField.ai.Settlement;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static com.SpringField.engine.util.Util.*;
import static com.SpringField.engine.util.Util.KNIGHT;

public class Engine {
    private BoardState boardState;
    private Settlement settle;

    public Engine(BoardState b) throws IOException {
        boardState = b;
        settle = new Settlement(boardState);
        init();
        // while (gameNotOver)
        //      gameloop
    }

    public void init() throws IOException {
        while (boardState.inSettlementPhase()){
            byte next = settle.getBestPossibleSettle();
            boardState.buildSettlement(next);
        }
    }

    private void gameLoop(){
    }

    private BoardState getBoardState() {
        return boardState;
    }

    public boolean CurrentTurn(byte[] input) throws IOException {
        boolean confirmation;
        switch (input[0]) {
            case ROAD_COMMAND:
                confirmation = buildRoad(input[1]);
                return confirmation;
            case SETTLEMENT_COMMAND:
                confirmation = buildSettlement(input[1]);
                return confirmation;
            case CITY_COMMAND:
                confirmation = buildCity(input[1]);
                return confirmation;
            case DEV_CARD_COMMAND:
                confirmation = buyDevCard();
                return confirmation;
            case ROBBER_COMMAND:
                confirmation = playRobber(input[1], input[2]);
                return confirmation;
            case KNIGHT_COMMAND:
                confirmation = playKnightCard(input[1], input[2]);
                return confirmation;
            case ROAD_BUILDING_COMMAND:
                confirmation = playRoadBuilding(input[1], input[2]);
                return confirmation;
            case MONOPOLY_COMMAND:
                confirmation = playMonopoly(input[1]);
                return confirmation;
            case YEAR_OF_PLENTY_COMMAND:
                confirmation = playYearOfPlenty(input[1], input[2]);
                return confirmation;
            case TRADE_BANK_COMMAND:
                confirmation = tradeBank(input[1], input[2]);
                return confirmation;
            case ADVANCE_TURN_COMMAND:
                advanceTurn();
                return true;
            default:
                throw new RuntimeException("Invalid Command");
        }
    }

    private boolean playerTrade(byte[] giving, byte[] recieving){
        return false;
    }

    private boolean buildRoad(byte road) {
        try {
            boardState.buildRoad(road);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean buildSettlement(byte settle) {
        try {
            boardState.buildSettlement(settle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean buildCity(byte city) {
        try {
            boardState.buildCity(city);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean buyDevCard() {
        try {
            boardState.buyDevCard();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean playRobber(byte tile, byte stolenPlayer) throws IOException {
        try {
            boardState.playRobber(tile, stolenPlayer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean playKnightCard(byte tile, byte playerId) {
        try {
            boardState.playKnightCard(tile, playerId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean playRoadBuilding(byte first, byte second){
        try {
            boardState.playRoadBuilding(first, second);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean playMonopoly(byte resource){
        try {
            boardState.playMonopoly(resource);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean playYearOfPlenty(byte first, byte second){
        try {
            boardState.playYearOfPlenty(first, second);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean tradeBank(byte yours, byte ours){
        try {
            boardState.tradeBank(yours, ours);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean advanceTurn(){
        try {
            boardState.advanceTurn();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
