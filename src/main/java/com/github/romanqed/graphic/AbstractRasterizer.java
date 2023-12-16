package com.github.romanqed.graphic;

abstract class AbstractRasterizer implements Rasterizer {
    protected final Raster raster;
    protected final int width;
    protected final int height;

    AbstractRasterizer(Raster raster) {
        this.raster = raster;
        this.width = raster.getWidth();
        this.height = raster.getHeight();
    }
}
