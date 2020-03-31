package com.SpringField.ai;

import com.SpringField.engine.BoardState;
import com.SpringField.engine.BoardStateAI;

import java.util.ArrayList;
import java.util.HashSet;

public class AlphaBetaPruning {
    private BoardStateAI state;
    private byte playerId;

    public AlphaBetaPruning(BoardStateAI state) {
        this.state = state;
        this.playerId = state.getPlayerTurn();
    }

    /*
     * Magic?
     */
    public BoardState getBestPossiblyMove() {
        return null;
    }

    /*
     * Simple Cost function for now, we can improve later on
     */
    private byte costFunction(BoardStateAI b, byte playerId) {
        return b.computeVictoryPoints(playerId);
    }

    private byte AlphaBetaPruning(BoardStateAI board, byte depth, byte alpha, byte beta){
        if (depth == 3)
            return costFunction(board, board.getPlayerTurn());

        if (playerId == board.getPlayerTurn()){ // This should be replaced by a call to
            for (BoardStateAI a: board.getAllPossibleMoves()){
                alpha = (byte) Math.max(alpha, AlphaBetaPruning(a, depth++, alpha, beta));
                if (alpha >= beta)
                    break;
            }
            return alpha;
        }
        else{
            for (BoardStateAI b: board.getAllPossibleMoves()){
                beta = (byte) Math.max(alpha, AlphaBetaPruning(b, depth++, alpha, beta));
                if (alpha >= beta){
                    break;
                }
            }
            return beta;
        }
    }

    private byte maxN(BoardStateAI board, byte depth){
        depth += 1; // initialize depth to -1 when starting
        if (depth == 3){
            return costFunction(board, depth);
        }
        for (BoardStateAI state: board.getAllPossibleMoves()){
            costFunction(state, depth);
            // ψ = MaxN (child, p+) ^^^
            // if (α < ψp)
            //      α = ψp,
            //      ψmax = ψ
        }
        // return ψ max

        // ψ is our cost function
        return 0;
    }
}
