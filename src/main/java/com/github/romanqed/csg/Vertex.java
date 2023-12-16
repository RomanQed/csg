package com.github.romanqed.csg;

import com.github.romanqed.math.Transformable;
import eu.mihosoft.vvecmath.Transform;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.Objects;

public final class Vertex implements Transformable<Vertex> {
    final Vector3d vector;
    Vector3d normal;

    public Vertex(Vector3d vector, Vector3d normal) {
        this.vector = vector;
        this.normal = normal;
    }

    public Vector3d getVector() {
        return vector;
    }

    public Vector3d getNormal() {
        return normal;
    }

    public Vertex flip() {
        return new Vertex(vector, normal.negated());
    }

    public Vertex interpolate(Vertex other, double t) {
        return new Vertex(vector.lerp(other.vector, t), normal.lerp(other.normal, t));
    }

    @Override
    public Vertex apply(Transform transform) {
        return new Vertex(vector.transformed(transform), normal);
    }

    public Vertex copy() {
        return new Vertex(vector.clone(), normal.clone());
    }

    @Override
    public int hashCode() {
        return this.vector.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var other = (Vertex) obj;
        return Objects.equals(this.vector, other.vector);
    }

    @Override
    public String toString() {
        return vector.toString();
    }
}
