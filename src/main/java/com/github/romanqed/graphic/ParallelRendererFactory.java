package com.github.romanqed.graphic;

public final class ParallelRendererFactory implements RendererFactory {
    @Override
    public Renderer create(Shader shader, Clipper clipper, Transformer transformer) {
        return new ParallelRenderer(shader, clipper, transformer);
    }
}
