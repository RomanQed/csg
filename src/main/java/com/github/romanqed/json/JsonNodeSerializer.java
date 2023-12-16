package com.github.romanqed.json;

import com.github.romanqed.MergeNode;
import com.github.romanqed.NodeSerializer;
import com.github.romanqed.PrimitiveNode;
import com.github.romanqed.SolidNode;
import com.github.romanqed.primitive.*;
import com.google.gson.Gson;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.HashMap;
import java.util.Map;

public final class JsonNodeSerializer implements NodeSerializer {
    private final Gson gson = new Gson();

    private static Map<String, Object> nodeToMap(SolidNode node) {
        var ret = new HashMap<String, Object>();
        ret.put("rotate", node.getRotate());
        ret.put("translate", node.getTranslate());
        return ret;
    }

    private static Vector3d mapToVec(Map<String, Double> map) {
        return Vector3d.xyz(
                map.get("x"),
                map.get("y"),
                map.get("z")
        );
    }

    @SuppressWarnings("unchecked")
    private static void mapToNode(SolidNode node, Map<String, Object> map) {
        var rot = (Map<String, Double>) map.get("rotate");
        var tr = (Map<String, Double>) map.get("translate");
        if (rot != null) {
            node.setRotate(mapToVec(rot));
        }
        if (tr != null) {
            node.setTranslate(mapToVec(tr));
        }
    }

    private static Map<String, Object> primitiveToMap(Primitive primitive) {
        var clazz = primitive.getClass();
        if (clazz == Cube.class) {
            var cube = (Cube) primitive;
            return Map.of(
                    "type", "cube",
                    "dimensions", cube.getDimensions()
            );
        }
        if (clazz == Cylinder.class) {
            var cylinder = (Cylinder) primitive;
            return Map.of(
                    "type", "cylinder",
                    "radius", cylinder.getStartRadius(),
                    "height", cylinder.getEnd().z()
            );
        }
        if (clazz == Sphere.class) {
            var sphere = (Sphere) primitive;
            return Map.of(
                    "type", "sphere",
                    "radius", sphere.getRadius()
            );
        }
        if (clazz == Torus.class) {
            var torus = (Torus) primitive;
            return Map.of(
                    "type", "torus",
                    "centerRadius", torus.getCenterRadius(),
                    "innerRadius", torus.getInnerRadius()
            );
        }
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("unchecked")
    private static Primitive mapToPrimitive(Map<String, Object> values) {
        var type = (String) values.get("type");
        switch (type) {
            case "cube":
                var vec = (Map<String, Double>) values.get("dimensions");
                var x = vec.get("x");
                var y = vec.get("y");
                var z = vec.get("z");
                if (x <= 0 || y <= 0 || z <= 0) {
                    throw new IllegalArgumentException();
                }
                return new Cube(x, y, z);
            case "cylinder":
                var cylRad = (Double) values.get("radius");
                var cylH = (Double) values.get("height");
                if (cylRad <= 0 || cylH <= 0) {
                    throw new IllegalArgumentException();
                }
                return new Cylinder(cylRad, cylH);
            case "sphere":
                var sRad = (Double) values.get("radius");
                if (sRad <= 0) {
                    throw new IllegalArgumentException();
                }
                return new Sphere(sRad);
            case "torus":
                var centerRad = (Double) values.get("centerRadius");
                var innerRad = (Double) values.get("innerRadius");
                if (centerRad <= 0 || innerRad <= 0 || innerRad >= centerRad) {
                    throw new IllegalArgumentException();
                }
                return new Torus(centerRad, innerRad);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static SolidNode entryToPrimitive(JsonEntry entry) {
        var primitive = mapToPrimitive(entry.primitive);
        var ret = new PrimitiveNode(primitive);
        mapToNode(ret, entry.body);
        return ret;
    }

    private static SolidNode entryToNode(JsonEntry entry) {
        if (entry.type == EntryType.PRIMITIVE) {
            return entryToPrimitive(entry);
        }
        var ret = new MergeNode();
        mapToNode(ret, entry.body);
        ret.setOperation(entry.operation);
        ret.setFirst(entryToNode(entry.first));
        ret.setSecond(entryToNode(entry.second));
        return ret;
    }

    private JsonEntry toEntry(PrimitiveNode node) {
        var ret = new JsonEntry();
        ret.type = EntryType.PRIMITIVE;
        ret.body = nodeToMap(node);
        ret.primitive = primitiveToMap(node.getPrimitive());
        return ret;
    }

    private JsonEntry toEntry(SolidNode node) {
        if (node instanceof PrimitiveNode) {
            return toEntry((PrimitiveNode) node);
        }
        var merge = (MergeNode) node;
        var ret = new JsonEntry();
        ret.type = EntryType.MERGE;
        ret.body = nodeToMap(node);
        ret.operation = merge.getOperation();
        ret.first = toEntry(merge.getFirst());
        ret.second = toEntry(merge.getSecond());
        return ret;
    }

    @Override
    public String serialize(SolidNode node) {
        return gson.toJson(toEntry(node));
    }

    @Override
    public SolidNode deserialize(String spec) {
        var entry = gson.fromJson(spec, JsonEntry.class);
        return entryToNode(entry);
    }
}
