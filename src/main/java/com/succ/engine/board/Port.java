package com.succ.engine.board;

public class Port {
    public final byte resource;
    public final byte exchangeRate;

    public Port(byte resource, byte exchangeRate){
        this.resource = resource;
        this.exchangeRate = exchangeRate;
    }

    public byte getExchangeRate() {
        return exchangeRate;
    }

    public byte getResource() {
        return resource;
    }

}
