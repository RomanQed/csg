package com.github.romanqed.graphic;

abstract class AbstractRenderer implements Renderer {
    private final Shader shader;
    private final Clipper clipper;
    private final Transformer transformer;

    protected AbstractRenderer(Shader shader, Clipper clipper, Transformer transformer) {
        this.shader = shader;
        this.clipper = clipper;
        this.transformer = transformer;
    }

    protected void render(Tris tris, Rasterizer rasterizer) {
        var shade = shader.calculate(tris);
        if (shade.full) {
            return;
        }
        var color = shade.color;
        var clipped = clipper.clip(tris.getTriangle());
        for (var triangle : clipped) {
            var transformed = transformer.transform(triangle);
            rasterizer.rasterize(transformed, color);
        }
    }
}
