package com.github.romanqed.csg;

import com.github.romanqed.math.MathUtil;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.ArrayList;
import java.util.List;

final class BSPPlane {
    private static final double EPSILON = 1e-8;
    private static final int COPLANAR = 0;
    private static final int FRONT = 1;
    private static final int BACK = 2;
    private static final int SPANNING = 3;

    private Vector3d normal;
    private double distance;

    BSPPlane(Vector3d normal, double distance) {
        this.normal = normal.normalized();
        this.distance = distance;
    }

    static BSPPlane createFromVertices(List<Vertex> vertices) {
        var a = vertices.get(0).vector;
        var b = vertices.get(1).vector;
        var c = vertices.get(2).vector;
        var n = MathUtil.calculateNormal(a, b, c);
        return new BSPPlane(n, n.dot(a));
    }

    void flip() {
        normal = normal.negated();
        distance = -distance;
    }

    void splitPolygon(
            Polygon polygon,
            List<Polygon> coplanarFront,
            List<Polygon> coplanarBack,
            List<Polygon> front,
            List<Polygon> back) {
        // Classify each point as well as the entire polygon into one of the 
        // above four classes.
        var polygonType = 0;
        var types = new ArrayList<Integer>(polygon.vertices.size());
        for (var i = 0; i < polygon.vertices.size(); ++i) {
            var t = this.normal.dot(polygon.vertices.get(i).vector) - this.distance;
            var type = (t < -BSPPlane.EPSILON) ? BACK : (t > BSPPlane.EPSILON) ? FRONT : COPLANAR;
            polygonType |= type;
            types.add(type);
        }
        switch (polygonType) {
            case COPLANAR:
                (this.normal.dot(polygon.getPlane().getNormal()) > 0 ? coplanarFront : coplanarBack).add(polygon);
                break;
            case FRONT:
                front.add(polygon);
                break;
            case BACK:
                back.add(polygon);
                break;
            case SPANNING:
                var f = new ArrayList<Vertex>();
                var b = new ArrayList<Vertex>();
                for (var i = 0; i < polygon.vertices.size(); ++i) {
                    var j = (i + 1) % polygon.vertices.size();
                    var ti = types.get(i);
                    var tj = types.get(j);
                    var vi = polygon.vertices.get(i);
                    var vj = polygon.vertices.get(j);
                    if (ti != BACK) {
                        f.add(vi);
                    }
                    if (ti != FRONT) {
                        b.add(ti != BACK ? vi.copy() : vi);
                    }
                    if ((ti | tj) == SPANNING) {
                        var t = (this.distance - this.normal.dot(vi.vector))
                                / this.normal.dot(vj.vector.minus(vi.vector));
                        var v = vi.interpolate(vj, t);
                        f.add(v);
                        b.add(v.copy());
                    }
                }
                if (f.size() >= 3) {
                    var temp = Polygon.fromVertices(f, polygon.getStorage());
                    if (temp != null) {
                        front.add(temp);
                    }
                }
                if (b.size() >= 3) {
                    var temp = Polygon.fromVertices(b, polygon.getStorage());
                    if (temp != null) {
                        back.add(temp);
                    }
                }
                break;
        }
    }
}
