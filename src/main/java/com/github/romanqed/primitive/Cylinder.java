package com.github.romanqed.primitive;

import com.github.romanqed.csg.Polygon;
import com.github.romanqed.csg.Vertex;
import com.github.romanqed.util.AbstractConfigurable;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Cylinder extends AbstractConfigurable implements Primitive {
    private Vector3d start;
    private Vector3d end;
    private double startRadius;
    private double endRadius;
    private int slices;

    public Cylinder(Vector3d start, Vector3d end, double startRadius, double endRadius, int slices) {
        super(null);
        this.start = start;
        this.end = end;
        this.startRadius = startRadius;
        this.endRadius = endRadius;
        this.slices = slices;
    }

    public Cylinder(Vector3d start, Vector3d end, double startRadius, double endRadius) {
        this(start, end, startRadius, endRadius, 16);
    }

    public Cylinder(Vector3d start, Vector3d end, double radius, int slices) {
        this(start, end, radius, radius, slices);
    }

    public Cylinder(Vector3d start, Vector3d end, double radius) {
        this(start, end, radius, radius, 16);
    }

    public Cylinder(double radius, double height, int slices) {
        this(Vector3d.ZERO, Vector3d.Z_ONE.times(height), radius, radius, slices);
    }

    public Cylinder(double radius, double height) {
        this(Vector3d.ZERO, Vector3d.Z_ONE.times(height), radius, radius, 16);
    }

    public Cylinder(double startRadius, double endRadius, double height, int slices) {
        this(Vector3d.ZERO, Vector3d.Z_ONE.times(height), startRadius, endRadius, slices);
    }

    public Cylinder(double startRadius, double endRadius, double height) {
        this(Vector3d.ZERO, Vector3d.Z_ONE.times(height), startRadius, endRadius, 16);
    }

    private static Vertex makeVertex(
            Vector3d x, Vector3d y, Vector3d z, Vector3d ray, Vector3d s,
            double r, double stack, double slice, double normalBlend) {
        var angle = slice * Math.PI * 2;
        var out = x.times(Math.cos(angle)).plus(y.times(Math.sin(angle)));
        var vector = s.plus(ray.times(stack)).plus(out.times(r));
        var normal = out.times(1.0 - Math.abs(normalBlend)).plus(z.times(normalBlend));
        return new Vertex(vector, normal);
    }

    public Vector3d getStart() {
        return start;
    }

    public void setStart(Vector3d start) {
        this.start = start;
    }

    public Vector3d getEnd() {
        return end;
    }

    public void setEnd(Vector3d end) {
        this.end = end;
    }

    public double getStartRadius() {
        return startRadius;
    }

    public void setStartRadius(double startRadius) {
        this.startRadius = startRadius;
    }

    public double getEndRadius() {
        return endRadius;
    }

    public void setEndRadius(double endRadius) {
        this.endRadius = endRadius;
    }

    public int getSlices() {
        return slices;
    }

    public void setSlices(int slices) {
        this.slices = slices;
    }

    @Override
    public List<Polygon> toPolygons() {
        var start = this.start;
        var end = this.end;
        var ray = end.minus(start);
        var z = ray.normalized();
        var isY = Math.abs(z.y()) > 0.5;
        var x = Vector3d.xyz(isY ? 1 : 0, !isY ? 1 : 0, 0).crossed(z).normalized();
        var y = x.crossed(z).normalized();
        var startV = new Vertex(start, z.negated());
        var endV = new Vertex(end, z.normalized());
        var polygons = new ArrayList<Polygon>();
        for (var i = 0; i < slices; ++i) {
            var t0 = i / (double) slices;
            var t1 = (i + 1) / (double) slices;
            polygons.add(Polygon.fromVertices(
                    Arrays.asList(
                            startV,
                            makeVertex(x, y, z, ray, start, startRadius, 0, t0, -1),
                            makeVertex(x, y, z, ray, start, startRadius, 0, t1, -1)
                    ),
                    storage
            ));
            polygons.add(Polygon.fromVertices(Arrays.asList(
                            makeVertex(x, y, z, ray, start, startRadius, 0, t1, 0),
                            makeVertex(x, y, z, ray, start, startRadius, 0, t0, 0),
                            makeVertex(x, y, z, ray, start, endRadius, 1, t0, 0),
                            makeVertex(x, y, z, ray, start, endRadius, 1, t1, 0)
                    ),
                    storage
            ));
            polygons.add(Polygon.fromVertices(
                    Arrays.asList(
                            endV,
                            makeVertex(x, y, z, ray, start, endRadius, 1, t1, 1),
                            makeVertex(x, y, z, ray, start, endRadius, 1, t0, 1)),
                    storage
            ));
        }
        return polygons;
    }
}
