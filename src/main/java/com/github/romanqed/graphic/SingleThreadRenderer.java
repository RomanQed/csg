package com.github.romanqed.graphic;

import java.util.List;

final class SingleThreadRenderer extends AbstractRenderer {
    public SingleThreadRenderer(Shader shader, Clipper clipper, Transformer transformer) {
        super(shader, clipper, transformer);
    }

    @Override
    public void render(Rasterizer rasterizer, List<Tris> trises) {
        for (var tris : trises) {
            render(tris, rasterizer);
        }
    }
}
