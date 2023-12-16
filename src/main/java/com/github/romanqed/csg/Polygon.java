package com.github.romanqed.csg;

import com.github.romanqed.math.MathUtil;
import com.github.romanqed.math.Transformable;
import com.github.romanqed.util.AbstractConfigurable;
import com.github.romanqed.util.PropertyStorage;
import eu.mihosoft.vvecmath.Plane;
import eu.mihosoft.vvecmath.Transform;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Polygon extends AbstractConfigurable implements Transformable<Polygon> {
    final List<Vertex> vertices;
    private final Plane plane;

    private Polygon(List<Vertex> vertices, Plane plane, PropertyStorage storage) {
        super(storage);
        this.vertices = vertices;
        this.plane = plane;
    }

    public static Polygon fromPoints(List<Vector3d> points, PropertyStorage storage) {
        var vertices = new ArrayList<Vertex>();
        for (var point : points) {
            var vertex = new Vertex(point.clone(), null);
            vertices.add(vertex);
        }
        return fromVertices(vertices, storage);
    }

    public static Polygon fromVertices(List<Vertex> vertices, PropertyStorage storage) {
        if (vertices.size() < 3) {
            throw new RuntimeException("Invalid polygon: at least 3 vertices expected, got: " + vertices.size());
        }
        var normal = MathUtil.calculateNormal(
                vertices.get(0).vector,
                vertices.get(1).vector,
                vertices.get(2).vector
        );
        if (Vector3d.ZERO.equals(normal)) {
            return null;
        }
        for (var vertex : vertices) {
            vertex.normal = normal;
        }
        return new Polygon(vertices, Plane.fromPointAndNormal(getCentroid(vertices), normal), storage);
    }

    public static Polygon fromVertices(List<Vertex> vertices) {
        return fromVertices(vertices, null);
    }

    private static Vector3d getCentroid(List<Vertex> vertices) {
        var sum = Vector3d.zero();
        for (var vertex : vertices) {
            sum = sum.plus(vertex.vector);
        }
        return sum.times(1.0 / vertices.size());
    }

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public Plane getPlane() {
        return plane;
    }

    public Vector3d getNormal() {
        return plane.getNormal();
    }

    public Vector3d getCentroid() {
        return getCentroid(vertices);
    }

    public Polygon flip() {
        var flipped = new ArrayList<Vertex>();
        for (var i = vertices.size() - 1; i >= 0; --i) {
            flipped.add(vertices.get(i).flip());
        }
        return new Polygon(flipped, plane.flipped(), storage);
    }

    @Override
    public Polygon apply(Transform transform) {
        var transformedVertices = new ArrayList<Vertex>();
        vertices.forEach(v -> transformedVertices.add(v.apply(transform)));
        var a = transformedVertices.get(0).vector;
        var b = transformedVertices.get(1).vector;
        var c = transformedVertices.get(2).vector;
        var normal = MathUtil.calculateNormal(a, b, c);
        var transformedPlane = Plane.fromPointAndNormal(getCentroid(), normal);
        transformedVertices.forEach(v -> v.normal = normal);
        var ret = new Polygon(transformedVertices, transformedPlane, storage);
        if (transform.isMirror()) {
            return ret.flip();
        }
        return ret;
    }

    public Polygon copy() {
        var copied = vertices
                .stream()
                .map(Vertex::copy)
                .collect(Collectors.toList());
        return new Polygon(copied, plane.clone(), storage);
    }
}
