package com.github.romanqed.graphic;

final class SynchronizedRasterizer implements Rasterizer {
    private final Rasterizer rasterizer;
    private final Object lock;

    SynchronizedRasterizer(Rasterizer rasterizer) {
        this.rasterizer = rasterizer;
        this.lock = new Object();
    }

    @Override
    public void rasterize(Triangle triangle, int color) {
        synchronized (lock) {
            rasterizer.rasterize(triangle, color);
        }
    }
}
