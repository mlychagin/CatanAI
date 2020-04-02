package com.SpringField.ui.util;

import com.SpringField.engine.BoardState;
import com.SpringField.ui.DrawBoard;

import java.io.File;
import java.io.FileOutputStream;
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

    /*
     * Draw Board Files
     */
    public final static String outputFileName = "tmp";

    public static void drawBoard(BoardState b) throws IOException {
        File f = new File(outputFileName);
        f.delete();

        byte[] serializedBoardState = b.serialize();
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        fileOutputStream.write(serializedBoardState);
        fileOutputStream.close();
        DrawBoard.main(new String[0]);
    }
}
