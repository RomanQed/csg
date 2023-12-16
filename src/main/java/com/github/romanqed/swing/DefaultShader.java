package com.github.romanqed.swing;

import com.github.romanqed.graphic.Shade;
import com.github.romanqed.graphic.Shader;
import com.github.romanqed.graphic.Tris;

import java.awt.*;

public final class DefaultShader implements Shader {
    private static final String COLOR_KEY = "color";

    @Override
    public Shade calculate(Tris tris) {
        var color = tris
                .getStorage()
                .<Color>get(COLOR_KEY)
                .orElseThrow();
        return new Shade(color.getRGB(), false);
    }
}
