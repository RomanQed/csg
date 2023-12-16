package com.github.romanqed.graphic;

public interface Raster {
    int getPixel(int x, int y);

    void setPixel(int x, int y, int value);

    int getWidth();

    int getHeight();
}
