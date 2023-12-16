package com.github.romanqed.graphic;

import com.github.romanqed.math.Transformable;
import eu.mihosoft.vvecmath.Transform;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.Objects;

public final class Triangle implements Transformable<Triangle> {
    final Vector3d first;
    final Vector3d second;
    final Vector3d third;

    public Triangle(Vector3d first, Vector3d second, Vector3d third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Vector3d getFirst() {
        return first;
    }

    public Vector3d getSecond() {
        return second;
    }

    public Vector3d getThird() {
        return third;
    }

    @Override
    public Triangle apply(Transform transform) {
        return new Triangle(
                first.transformed(transform),
                second.transformed(transform),
                third.transformed(transform)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Triangle)) return false;
        var triangle = (Triangle) o;
        return first.equals(triangle.first)
                && second.equals(triangle.second)
                && third.equals(triangle.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
