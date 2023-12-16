package com.github.romanqed.primitive;

import com.github.romanqed.csg.Polygon;
import com.github.romanqed.csg.Vertex;
import com.github.romanqed.util.AbstractConfigurable;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.ArrayList;
import java.util.List;

public final class Sphere extends AbstractConfigurable implements Primitive {
    private Vector3d center;
    private double radius;
    private int slices;
    private int stacks;

    public Sphere(Vector3d center, double radius, int slices, int stacks) {
        super(null);
        this.center = center;
        this.radius = radius;
        this.slices = slices;
        this.stacks = stacks;
    }

    public Sphere(Vector3d center, double radius) {
        this(center, radius, 16, 8);
    }

    public Sphere(double radius, int slices, int stacks) {
        this(Vector3d.ZERO, radius, slices, stacks);
    }

    public Sphere(double radius) {
        this(Vector3d.ZERO, radius, 16, 8);
    }

    private static Vertex makeVertex(Vector3d center, double radius, double theta, double phi) {
        theta *= Math.PI * 2;
        phi *= Math.PI;
        var dir = Vector3d.xyz(
                Math.cos(theta) * Math.sin(phi),
                Math.cos(phi),
                Math.sin(theta) * Math.sin(phi)
        );
        return new Vertex(center.plus(dir.times(radius)), dir);
    }

    public Vector3d getCenter() {
        return center;
    }

    public void setCenter(Vector3d center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getSlices() {
        return slices;
    }

    public void setSlices(int slices) {
        this.slices = slices;
    }

    public int getStacks() {
        return stacks;
    }

    public void setStacks(int stacks) {
        this.stacks = stacks;
    }

    @Override
    public List<Polygon> toPolygons() {
        var ret = new ArrayList<Polygon>();
        for (var i = 0; i < slices; i++) {
            for (var j = 0; j < stacks; j++) {
                var vertices = new ArrayList<Vertex>();
                vertices.add(makeVertex(center, radius, i / (double) slices, j / (double) stacks));
                if (j > 0) {
                    vertices.add(makeVertex(center, radius, (i + 1) / (double) slices, j / (double) stacks));
                }
                if (j < stacks - 1) {
                    vertices.add(makeVertex(center,
                            radius,
                            (i + 1) / (double) slices,
                            (j + 1) / (double) stacks));
                }
                vertices.add(makeVertex(center, radius, i / (double) slices, (j + 1) / (double) stacks));
                var polygon = Polygon.fromVertices(vertices, storage);
                if (polygon == null) {
                    continue;
                }
                ret.add(polygon);
            }
        }
        return ret;
    }
}
