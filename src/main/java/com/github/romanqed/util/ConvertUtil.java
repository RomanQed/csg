package com.github.romanqed.util;

import com.github.romanqed.csg.Polygon;
import com.github.romanqed.csg.PolygonUtil;
import com.github.romanqed.csg.Solid;
import com.github.romanqed.graphic.Triangle;
import com.github.romanqed.graphic.Tris;
import com.github.romanqed.scene.ModelObject;

import java.util.LinkedList;

public final class ConvertUtil {
    private ConvertUtil() {
    }

    private static Tris polygonToTris(Polygon polygon, PropertyStorage storage) {
        var vertices = polygon.getVertices();
        var triangle = new Triangle(
                vertices.get(0).getVector(),
                vertices.get(1).getVector(),
                vertices.get(2).getVector()
        );
        return new Tris(triangle, polygon.getNormal(), storage);
    }

    public static ModelObject solidToModel(Solid solid) {
        var polygons = solid.getPolygons();
        var ret = new LinkedList<Tris>();
        for (var polygon : polygons) {
            var triangulated = PolygonUtil.toTriangles(polygon);
            for (var triangle : triangulated) {
                if (triangle == null) {
                    continue;
                }
                ret.add(polygonToTris(triangle, solid.getStorage()));
            }
        }
        return new ModelObject(ret, solid.getStorage());
    }
}
