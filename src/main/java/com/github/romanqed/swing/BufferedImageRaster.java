package com.github.romanqed.swing;

import com.github.romanqed.graphic.Raster;

import java.awt.image.BufferedImage;

final class BufferedImageRaster implements Raster {
    final BufferedImage image;

    BufferedImageRaster(BufferedImage image) {
        this.image = image;
    }

    @Override
    public int getPixel(int x, int y) {
        return image.getRGB(x, y);
    }

    @Override
    public void setPixel(int x, int y, int value) {
        image.setRGB(x, y, value);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }
}
