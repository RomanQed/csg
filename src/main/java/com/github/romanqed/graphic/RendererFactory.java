package com.github.romanqed.graphic;

public interface RendererFactory {
    Renderer create(Shader shader, Clipper clipper, Transformer transformer);
}
