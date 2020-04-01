package com.SpringField.ui.Util;

import com.SpringField.engine.BoardState;
import com.SpringField.ui.DrawBoard;

import java.io.IOException;

public class Util {

    /*
     * Player Color
     */
    public final static byte PLAYER_RED = 0;
    public final static byte PLAYER_BLUE = 1;
    public final static byte PLAYER_ORANGE = 2;
    public final static byte PLAYER_GREEN = 3;
    public final static byte PLAYER_WHITE = 4;
    public final static byte PLAYER_BROWN = 5;

    public static void drawBoard(BoardState b) throws IOException {
        byte[] serializedBoardState = b.serialize();
        String[] input = new String[]{new String(serializedBoardState)};
        DrawBoard.main(input);
    }
}
