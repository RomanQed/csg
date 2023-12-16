package com.github.romanqed.graphic;

import com.github.romanqed.math.Line;
import com.github.romanqed.math.PlaneUtil;
import eu.mihosoft.vvecmath.Plane;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.Collections;
import java.util.List;

public final class ClipUtil {
    private ClipUtil() {
    }

    public static List<Triangle> clip1(Plane plane, Vector3d invisible, Vector3d v1, Vector3d v2) {
        var l13 = new Line(v1, invisible);
        var l23 = new Line(v2, invisible);
        var v31 = PlaneUtil.intersection(plane, l13);
        var v32 = PlaneUtil.intersection(plane, l23);
        return List.of(
                new Triangle(v1, v32, v2),
                new Triangle(v1, v32, v31)
        );
    }

    public static Triangle clip2(Plane plane, Vector3d i1, Vector3d i2, Vector3d v) {
        var l12 = new Line(v, i1);
        var l13 = new Line(v, i2);
        var v2 = PlaneUtil.intersection(plane, l12);
        var v3 = PlaneUtil.intersection(plane, l13);
        return new Triangle(v, v2, v3);
    }

    public static List<Triangle> clip(Plane plane, Triangle triangle) {
        // Vertices
        var v1 = triangle.first;
        var v2 = triangle.second;
        var v3 = triangle.third;
        // Invisible check
        var check1 = plane.compare(v1) < 0 ? 1 : 0;
        var check2 = plane.compare(v2) < 0 ? 1 : 0;
        var check3 = plane.compare(v3) < 0 ? 1 : 0;
        var invisible = check1 + check2 + check3;
        // Trivial cases checks
        if (invisible == 0) {
            return List.of(triangle);
        }
        if (invisible == 3) {
            return Collections.emptyList();
        }
        // One vertex invisible
        if (invisible == 1) {
            if (check1 == 1) {
                return clip1(plane, v1, v2, v3);
            }
            if (check2 == 1) {
                return clip1(plane, v2, v1, v3);
            }
            return clip1(plane, v3, v1, v2);
        }
        // Two vertices invisible
        if (check1 == 0) {
            return List.of(clip2(plane, v2, v3, v1));
        }
        if (check2 == 0) {
            return List.of(clip2(plane, v1, v3, v2));
        }
        return List.of(clip2(plane, v1, v2, v3));
    }
}
