package com.github.romanqed.graphic;

import java.util.Arrays;

public final class AreaRasterizerFactory implements RasterizerFactory {

    @Override
    public Rasterizer create(Raster raster) {
        var buffer = new double[raster.getWidth() * raster.getHeight()];
        Arrays.fill(buffer, Double.NEGATIVE_INFINITY);
        return new AreaRasterizer(raster, buffer);
    }
}
