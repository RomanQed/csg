package com.github.romanqed.math;

import eu.mihosoft.vvecmath.Vector3d;

import java.util.Objects;

public final class Vector4D {
    double x;
    double y;
    double z;
    double w;

    public Vector4D() {
    }

    public Vector4D(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4D(Vector3d vector) {
        this.x = vector.x();
        this.y = vector.y();
        this.z = vector.z();
        this.w = 1;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW() {
        return w;
    }

    public Vector3d to3D() {
        return Vector3d.xyz(x / w, y / w, z / w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector4D)) return false;
        var that = (Vector4D) o;
        return Double.compare(that.x, x) == 0
                && Double.compare(that.y, y) == 0
                && Double.compare(that.z, z) == 0
                && Double.compare(that.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public String toString() {
        return "Vector4D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
