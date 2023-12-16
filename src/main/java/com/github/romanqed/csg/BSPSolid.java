package com.github.romanqed.csg;

import com.github.romanqed.util.AbstractConfigurable;
import com.github.romanqed.util.PropertyStorage;
import eu.mihosoft.vvecmath.Transform;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

final class BSPSolid extends AbstractConfigurable implements Solid {
    private final List<Polygon> polygons;

    BSPSolid(List<Polygon> polygons, PropertyStorage storage) {
        super(storage);
        this.polygons = polygons;
    }

    private static List<Polygon> copy(List<Polygon> polygons) {
        var stream = polygons.size() > 200 ? polygons.parallelStream() : polygons.stream();
        return stream.map(Polygon::copy).collect(Collectors.toList());
    }

    @Override
    public List<Polygon> getPolygons() {
        return Collections.unmodifiableList(polygons);
    }

    @Override
    public Solid union(Solid solid, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger) {
        var a = new BSPNode(copy(polygons));
        var b = new BSPNode(copy(solid.getPolygons()));
        a.clipTo(b);
        b.clipTo(a);
        b.invert();
        b.clipTo(a);
        b.invert();
        a.build(b.getAllPolygons());
        return new BSPSolid(a.getAllPolygons(), merger.apply(storage, solid.getStorage()));
    }

    @Override
    public Solid union(List<Solid> solids, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger) {
        var ret = (Solid) this;
        for (var solid : solids) {
            ret = ret.union(solid, merger);
        }
        return ret;
    }

    @Override
    public Solid difference(Solid solid, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger) {
        var a = new BSPNode(copy(this.polygons));
        var b = new BSPNode(copy(solid.getPolygons()));
        a.invert();
        a.clipTo(b);
        b.clipTo(a);
        b.invert();
        b.clipTo(a);
        b.invert();
        a.build(b.getAllPolygons());
        a.invert();
        return new BSPSolid(a.getAllPolygons(), merger.apply(storage, solid.getStorage()));
    }

    @Override
    public Solid difference(List<Solid> solids, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger) {
        if (solids.isEmpty()) {
            return this;
        }
        var union = solids.get(0);
        for (var i = 1; i < solids.size(); i++) {
            union = union.union(solids.get(i), merger);
        }
        return difference(union);
    }

    @Override
    public Solid intersect(Solid solid, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger) {
        var a = new BSPNode(copy(this.polygons));
        var b = new BSPNode(copy(solid.getPolygons()));
        a.invert();
        b.clipTo(a);
        b.invert();
        a.clipTo(b);
        b.clipTo(a);
        a.build(b.getAllPolygons());
        a.invert();
        return new BSPSolid(a.getAllPolygons(), merger.apply(storage, solid.getStorage()));
    }

    @Override
    public Solid intersect(List<Solid> solids, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger) {
        if (solids.isEmpty()) {
            return this;
        }
        var union = solids.get(0);
        for (var i = 1; i < solids.size(); ++i) {
            union = union.union(solids.get(i), merger);
        }
        return intersect(union);
    }

    @Override
    public Solid apply(Transform transform) {
        if (polygons.isEmpty()) {
            return this;
        }
        var stream = polygons.size() > 200 ? polygons.parallelStream() : polygons.stream();
        var transformed = stream
                .map(p -> p.apply(transform))
                .collect(Collectors.toList());
        return new BSPSolid(transformed, storage);
    }
}
