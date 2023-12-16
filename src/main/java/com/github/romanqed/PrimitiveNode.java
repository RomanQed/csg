package com.github.romanqed;

import com.github.romanqed.csg.Solid;
import com.github.romanqed.csg.SolidFactory;
import com.github.romanqed.primitive.Primitive;

public final class PrimitiveNode extends AbstractNode {
    private final Primitive primitive;

    public PrimitiveNode(Primitive primitive) {
        this.primitive = primitive;
    }

    public Primitive getPrimitive() {
        return primitive;
    }

    @Override
    public Solid toSolid(SolidFactory factory) {
        var ret = factory.create(primitive);
        return apply(ret);
    }
}
