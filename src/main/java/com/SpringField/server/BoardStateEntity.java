package com.SpringField.server;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class BoardStateEntity {
    private @Id @GeneratedValue Long id;
    private byte numPlayers;

    protected BoardStateEntity() {
        numPlayers = 4;
    }

    public BoardStateEntity(byte numPlayers) {
        this.numPlayers = numPlayers;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getNumPlayers() {
        return this.numPlayers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BoardStateEntity))
            return false;
        BoardStateEntity boardStateEntity = (BoardStateEntity) o;
        return Objects.equals(this.id, boardStateEntity.id) && Objects.equals(this.numPlayers, boardStateEntity.numPlayers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.numPlayers);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", numPlayers='" + this.numPlayers+'}';
    }
}
