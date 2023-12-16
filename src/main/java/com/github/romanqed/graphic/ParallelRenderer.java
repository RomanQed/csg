package com.github.romanqed.graphic;

import java.util.List;

final class ParallelRenderer extends AbstractRenderer {
    public ParallelRenderer(Shader shader, Clipper clipper, Transformer transformer) {
        super(shader, clipper, transformer);
    }

    @Override
    public void render(Rasterizer rasterizer, List<Tris> trises) {
        var synced = new SynchronizedRasterizer(rasterizer);
        trises.parallelStream().forEach(tris -> render(tris, synced));
    }
}
