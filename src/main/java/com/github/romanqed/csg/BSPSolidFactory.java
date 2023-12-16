package com.github.romanqed.csg;

import com.github.romanqed.primitive.Primitive;

public final class BSPSolidFactory implements SolidFactory {

    @Override
    public Solid create(Primitive primitive) {
        return new BSPSolid(primitive.toPolygons(), primitive.getStorage());
    }
}
