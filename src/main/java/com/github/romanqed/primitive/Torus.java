package com.github.romanqed.primitive;

import com.github.romanqed.csg.Polygon;
import com.github.romanqed.csg.Vertex;
import com.github.romanqed.util.AbstractConfigurable;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Torus extends AbstractConfigurable implements Primitive {
    private double centerRadius;
    private double innerRadius;
    private int stacks;
    private int slices;

    public Torus(double centerRadius, double innerRadius, int stacks, int slices) {
        super(null);
        this.centerRadius = centerRadius;
        this.innerRadius = innerRadius;
        this.stacks = stacks;
        this.slices = slices;
    }

    public Torus(double centerRadius, double innerRadius) {
        this(centerRadius, innerRadius, 16, 8);
    }

    public double getCenterRadius() {
        return centerRadius;
    }

    public void setCenterRadius(double centerRadius) {
        this.centerRadius = centerRadius;
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(double r) {
        this.innerRadius = r;
    }

    public int getStacks() {
        return stacks;
    }

    public void setStacks(int stacks) {
        this.stacks = stacks;
    }

    public int getSlices() {
        return slices;
    }

    public void setSlices(int slices) {
        this.slices = slices;
    }

    @Override
    public List<Polygon> toPolygons() {
        var du = 2 * Math.PI / stacks;
        var dv = 2 * Math.PI / slices;
        var vertices = new LinkedList<Vertex>();
        for (var i = 0; i < stacks; ++i) {
            var u = i * du;
            for (var j = 0; j <= slices; ++j) {
                var v = (j % slices) * dv;
                for (var k = 0; k < 2; ++k) {
                    var uu = u + k * du;
                    // compute vector
                    var t = centerRadius + innerRadius * Math.cos(v);
                    var x = t * Math.cos(uu);
                    var y = t * Math.sin(uu);
                    var z = innerRadius * Math.sin(v);
                    var vector = Vector3d.xyz(x, y, z);
                    // compute normal
                    var nx = Math.cos(v) * Math.cos(uu);
                    var ny = Math.cos(v) * Math.sin(uu);
                    var nz = Math.sin(v);
                    var normal = Vector3d.xyz(nx, ny, nz);
                    var vertex = new Vertex(vector, normal);
                    vertices.add(vertex);
                }
            }
        }
        var ret = new ArrayList<Polygon>();
        for (var i = 0; i < vertices.size() - 2; i += 2) {
            var first = Polygon.fromVertices(
                    List.of(vertices.get(i), vertices.get(i + 1), vertices.get(i + 2))
            );
            var second = Polygon.fromVertices(
                    List.of(vertices.get(i + 3), vertices.get(i + 2), vertices.get(i + 1))
            );
            if (first != null) {
                ret.add(first);
            }
            if (second != null) {
                ret.add(second);
            }
        }
        return ret;
    }
}
