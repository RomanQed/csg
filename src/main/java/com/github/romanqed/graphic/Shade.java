package com.github.romanqed.graphic;

public final class Shade {
    final int color;
    final boolean full;

    public Shade(int color, boolean full) {
        this.color = color;
        this.full = full;
    }

    public int getColor() {
        return color;
    }

    public boolean isFull() {
        return full;
    }
}
