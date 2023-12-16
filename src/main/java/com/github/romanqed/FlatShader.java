package com.github.romanqed;

import com.github.romanqed.graphic.Shade;
import com.github.romanqed.graphic.Shader;
import com.github.romanqed.graphic.Tris;
import eu.mihosoft.vvecmath.Vector3d;

import java.awt.*;

public final class FlatShader implements Shader {
    private Vector3d normal;

    public FlatShader(Vector3d normal) {
        this.normal = normal;
    }

    private static Color getShade(Color color, double shade) {
        var redLinear = Math.pow(color.getRed(), 2.2) * shade;
        var greenLinear = Math.pow(color.getGreen(), 2.2) * shade;
        var blueLinear = Math.pow(color.getBlue(), 2.2) * shade;
        var red = (int) Math.pow(redLinear, 1 / 2.2);
        var green = (int) Math.pow(greenLinear, 1 / 2.2);
        var blue = (int) Math.pow(blueLinear, 1 / 2.2);
        return new Color(red, green, blue);
    }

    @Override
    public Shade calculate(Tris tris) {
        var color = tris
                .getStorage()
                .<Color>get("color")
                .orElseThrow();
        // Calculate light
        var intensity = tris.getNormal().dot(normal);
        return new Shade(getShade(color, intensity).getRGB(), false);
    }

    public void setNormal(Vector3d normal) {
        this.normal = normal;
    }
}
