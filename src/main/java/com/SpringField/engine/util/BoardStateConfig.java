package com.SpringField.engine.util;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static com.SpringField.engine.util.Util.*;

public class BoardStateConfig {
    private DataOutputStream dos;
    private byte[] tilesResource;
    private byte[] tilesNumber;
    private Random r;

    public BoardStateConfig(String outputFile, int numPlayers, long seed) throws IOException {
        if(outputFile != null){
            dos = new DataOutputStream(new FileOutputStream(outputFile));
            dos.writeByte(SEED_COMMAND);
            dos.writeByte(numPlayers);
            dos.writeLong(seed);
        }
        tilesResource = Util.tilesResource.clone();
        tilesNumber = Util.tilesNumber.clone();
        r = new Random(seed);
        dos = null;
    }

    public DataOutputStream getLogger(){
        return dos;
    }

    public Random getRandom() {
        return r;
    }

    public byte[] getTilesResource() {
        return tilesResource;
    }

    public byte[] getTilesNumber() {
        return tilesNumber;
    }

    public boolean isLoggerActive(){
        return dos != null;
    }

    private void initializeTiles() {
        if (tilesResource.length != tilesNumber.length) {
            throw new RuntimeException("Resources and Numbers not aligned");
        }
        shuffleArray(tilesResource, r);
        shuffleArray(tilesNumber, r);
        byte desertIndex = -1;
        for (int i = 0; i < tilesResource.length; i++) {
            if (tilesResource[i] == DESERT) {
                desertIndex = (byte) i;
                break;
            }
        }
        for (int i = 0; i < tilesNumber.length; i++) {
            if (tilesNumber[i] == 7) {
                tilesResource[desertIndex] = tilesResource[i];
                tilesResource[i] = DESERT;
                break;
            }
        }
    }

    public void writeByteArray(byte[] a) throws IOException {
        dos.writeByte(a.length);
        for (byte b : a) {
            dos.writeByte(b);
        }
    }

}
