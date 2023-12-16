package com.github.romanqed.graphic;

import java.util.List;

public interface Renderer {
    void render(Rasterizer rasterizer, List<Tris> trises);
}
