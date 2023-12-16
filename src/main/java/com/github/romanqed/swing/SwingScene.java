package com.github.romanqed.swing;

import com.github.romanqed.graphic.Renderer;
import com.github.romanqed.graphic.*;
import com.github.romanqed.math.MathUtil;
import com.github.romanqed.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class SwingScene extends JPanel {
    private final RasterizerFactory factory;
    private final Renderer renderer;
    private final MatrixTransformer transformer;
    private final FrustumClipper clipper;
    private Scene scene;
    private Color color;

    public SwingScene(RasterizerFactory rasterizerFactory, RendererFactory rendererFactory, Shader shader) {
        this.factory = Objects.requireNonNull(rasterizerFactory);
        this.transformer = new MatrixTransformer();
        this.clipper = new FrustumClipper();
        this.renderer = rendererFactory.create(shader, this.clipper, this.transformer);
        this.color = Color.WHITE;
    }

    public SwingScene(RasterizerFactory rasterizerFactory, RendererFactory rendererFactory) {
        this(rasterizerFactory, rendererFactory, new DefaultShader());
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (scene == null) {
            return;
        }
        // Clear scene
        graphics.setColor(color);
        var width = getWidth();
        var height = getHeight();
        graphics.fillRect(0, 0, width, height);
        // Prepare render
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var raster = new BufferedImageRaster(image);
        var camera = scene.getCamera();
        var projection = scene.getProjection();
        var aspect = (double) width / (double) height;
        // Clipper configure
        clipper.setFrustum(projection.getFrustum(camera, aspect));
        // Transform configure
        var viewport = MathUtil.createViewportMatrix(0, 0, width, height);
        var proj = scene.getProjection().toMatrix(aspect);
        var view = scene.getCamera().toMatrix();
        transformer.setMatrix(viewport.multiply(proj).multiply(view));
        // Rasterizer
        var rasterizer = factory.create(raster);
        // Render
        scene.getObjects().forEach(e -> e.accept(
                obj -> renderer.render(rasterizer, obj.getTrises())
        ));
        // Flush buffer
        graphics.drawImage(image, 0, 0, null);
    }
}
