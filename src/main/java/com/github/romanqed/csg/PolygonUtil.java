package com.github.romanqed.csg;

import java.util.LinkedList;
import java.util.List;

public final class PolygonUtil {
    private PolygonUtil() {
    }

    public static List<Polygon> toTriangles(Polygon polygon) {
        var ret = new LinkedList<Polygon>();
        var vertices = polygon.vertices;
        var size = vertices.size();
        if (size <= 3) {
            return List.of(polygon);
        }
        var first = vertices.get(0);
        for (var i = 0; i < size - 2; i++) {
            var toAdd = Polygon.fromPoints(
                    List.of(first.vector,
                            vertices.get(i + 1).vector,
                            vertices.get(i + 2).vector
                    ),
                    polygon.getStorage()
            );
            ret.add(toAdd);
        }
        return ret;
    }
}
