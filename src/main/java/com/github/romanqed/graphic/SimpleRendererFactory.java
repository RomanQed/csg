package com.github.romanqed.graphic;

public final class SimpleRendererFactory implements RendererFactory {
    @Override
    public Renderer create(Shader shader, Clipper clipper, Transformer transformer) {
        return new SingleThreadRenderer(shader, clipper, transformer);
    }
}
