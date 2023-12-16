package com.github.romanqed;

import com.github.romanqed.csg.Solid;
import com.github.romanqed.csg.SolidFactory;

public final class MergeNode extends AbstractNode {
    private SolidNode first;
    private SolidNode second;
    private SolidOperation operation;

    public SolidNode getFirst() {
        return first;
    }

    public void setFirst(SolidNode first) {
        this.first = first;
    }

    public SolidNode getSecond() {
        return second;
    }

    public void setSecond(SolidNode second) {
        this.second = second;
    }

    public SolidOperation getOperation() {
        return operation;
    }

    public void setOperation(SolidOperation operation) {
        this.operation = operation;
    }

    @Override
    public Solid toSolid(SolidFactory factory) {
        if (first == null || second == null || operation == null) {
            return null;
        }
        var firstSolid = first.toSolid(factory);
        var secondSolid = second.toSolid(factory);
        switch (operation) {
            case UNION:
                return apply(firstSolid.union(secondSolid));
            case DIFFERENCE:
                return apply(firstSolid.difference(secondSolid));
            case INTERSECTION:
                return apply(firstSolid.intersect(secondSolid));
            default:
                throw new IllegalArgumentException("Unknown solid operation");
        }
    }
}
