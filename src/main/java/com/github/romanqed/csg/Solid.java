package com.github.romanqed.csg;

import com.github.romanqed.math.Transformable;
import com.github.romanqed.util.Configurable;
import com.github.romanqed.util.PropertyStorage;

import java.util.List;
import java.util.function.BiFunction;

public interface Solid extends Transformable<Solid>, Configurable {
    BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> DEFAULT_MERGER = (l, r) -> null;

    List<Polygon> getPolygons();

    Solid union(Solid solid, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger);

    default Solid union(Solid solid) {
        return union(solid, DEFAULT_MERGER);
    }

    Solid union(List<Solid> solids, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger);

    default Solid union(List<Solid> solids) {
        return union(solids, DEFAULT_MERGER);
    }

    Solid difference(Solid solid, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger);

    default Solid difference(Solid solid) {
        return difference(solid, DEFAULT_MERGER);
    }

    Solid difference(List<Solid> solids, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger);

    default Solid difference(List<Solid> solids) {
        return difference(solids, DEFAULT_MERGER);
    }

    Solid intersect(Solid solid, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger);

    default Solid intersect(Solid solid) {
        return intersect(solid, DEFAULT_MERGER);
    }

    Solid intersect(List<Solid> solids, BiFunction<PropertyStorage, PropertyStorage, PropertyStorage> merger);

    default Solid intersect(List<Solid> solids) {
        return intersect(solids, DEFAULT_MERGER);
    }
}
