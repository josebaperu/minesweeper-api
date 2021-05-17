package com.root.model;


import com.google.gson.GsonBuilder;

public class Box {
    private final int id;
    private  boolean isVisible = false;
    private  boolean hasBomb = false;
    private int bombsAround = 0;

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void setHasBomb(boolean hasBomb) {
        this.hasBomb = hasBomb;
    }

    public void setBombsAround(int bombsAround) {
        this.bombsAround = bombsAround;
    }

    public int getId() {
        return id;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean isHasBomb() {
        return hasBomb;
    }

    public int getBombsAround() {
        return bombsAround;
    }

    public Box(final int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}