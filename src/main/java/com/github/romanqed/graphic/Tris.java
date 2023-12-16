package com.github.romanqed.graphic;

import com.github.romanqed.math.MathUtil;
import com.github.romanqed.math.Transformable;
import com.github.romanqed.util.AbstractConfigurable;
import com.github.romanqed.util.PropertyStorage;
import eu.mihosoft.vvecmath.Transform;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.Objects;

public final class Tris extends AbstractConfigurable implements Transformable<Tris> {
    private final Triangle triangle;
    private final Vector3d normal;

    public Tris(Triangle triangle, Vector3d normal, PropertyStorage storage) {
        super(storage);
        this.triangle = triangle;
        this.normal = normal;
    }

    public Tris(Triangle triangle, Vector3d normal) {
        this(triangle, normal, null);
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public Vector3d getNormal() {
        return normal;
    }

    @Override
    public Tris apply(Transform transform) {
        var transformed = triangle.apply(transform);
        var normal = MathUtil.calculateNormal(transformed.first, transformed.second, transformed.third);
        return new Tris(transformed, normal, storage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tris)) return false;
        var tris = (Tris) o;
        return triangle.equals(tris.triangle) && normal.equals(tris.normal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triangle, normal);
    }

    @Override
    public String toString() {
        return "Tris{" +
                "triangle=" + triangle +
                ", normal=" + normal +
                '}';
    }
}
