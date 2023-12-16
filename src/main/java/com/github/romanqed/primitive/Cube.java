package com.github.romanqed.primitive;

import com.github.romanqed.csg.Polygon;
import com.github.romanqed.csg.Vertex;
import com.github.romanqed.util.AbstractConfigurable;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.ArrayList;
import java.util.List;

public final class Cube extends AbstractConfigurable implements Primitive {
    private static final int[][][] CUBE_TEMPLATE = {
            // position     // normal
            {{0, 4, 6, 2}, {-1, 0, 0}},
            {{1, 3, 7, 5}, {1, 0, 0}},
            {{0, 1, 5, 4}, {0, -1, 0}},
            {{2, 6, 7, 3}, {0, 1, 0}},
            {{0, 2, 3, 1}, {0, 0, -1}},
            {{4, 5, 7, 6}, {0, 0, 1}}
    };

    private Vector3d center;
    private Vector3d dimensions;

    public Cube(Vector3d center, Vector3d dimensions) {
        super(null);
        this.center = center;
        this.dimensions = dimensions;
    }

    public Cube(Vector3d dimensions) {
        this(Vector3d.ZERO, dimensions);
    }

    public Cube(double w, double h, double d) {
        this(Vector3d.ZERO, Vector3d.xyz(w, h, d));
    }

    public Cube(double size) {
        this(size, size, size);
    }

    public Vector3d getCenter() {
        return center;
    }

    public void setCenter(Vector3d center) {
        this.center = center;
    }

    public Vector3d getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vector3d dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public List<Polygon> toPolygons() {
        var ret = new ArrayList<Polygon>();
        for (var info : CUBE_TEMPLATE) {
            var vertices = new ArrayList<Vertex>();
            for (var i : info[0]) {
                var vector = Vector3d.xyz(
                        center.x() + dimensions.x() * (Math.min(1, i & 1) - 0.5),
                        center.y() + dimensions.y() * (Math.min(1, i & 2) - 0.5),
                        center.z() + dimensions.z() * (Math.min(1, i & 4) - 0.5)
                );
                vertices.add(new Vertex(vector, Vector3d.xyz(info[1][0], info[1][1], info[1][2])));
            }
            var polygon = Polygon.fromVertices(vertices, storage);
            if (polygon == null) {
                continue;
            }
            polygon.setStorage(storage);
            ret.add(polygon);
        }
        return ret;
    }
}
