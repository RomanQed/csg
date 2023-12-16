package com.github.romanqed.csg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final class BSPNode {
    private List<Polygon> polygons;
    private BSPPlane plane;
    private BSPNode front;
    private BSPNode back;

    BSPNode(List<Polygon> polygons) {
        this.polygons = new ArrayList<>();
        if (polygons != null) {
            this.build(polygons);
        }
    }

    BSPNode() {
        this(null);
    }

    void invert() {
        var stream = polygons.size() > 200 ? polygons.parallelStream() : polygons.stream();
        polygons = stream.map(Polygon::flip).collect(Collectors.toList());
        if (this.plane == null) {
            if (polygons.isEmpty()) {
                throw new IllegalStateException("Plane is null and polygons is empty");
            }
            this.plane = BSPPlane.createFromVertices(polygons.get(0).vertices);
        }
        this.plane.flip();
        if (this.front != null) {
            this.front.invert();
        }
        if (this.back != null) {
            this.back.invert();
        }
        var temp = this.front;
        this.front = this.back;
        this.back = temp;
    }

    private List<Polygon> clipPolygons(List<Polygon> polygons) {
        if (this.plane == null) {
            return new ArrayList<>(polygons);
        }
        var frontP = (List<Polygon>) new ArrayList<Polygon>();
        var backP = (List<Polygon>) new ArrayList<Polygon>();
        for (var polygon : polygons) {
            this.plane.splitPolygon(polygon, frontP, backP, frontP, backP);
        }
        if (this.front != null) {
            frontP = this.front.clipPolygons(frontP);
        }
        if (this.back != null) {
            backP = this.back.clipPolygons(backP);
        } else {
            backP = new ArrayList<>(0);
        }
        frontP.addAll(backP);
        return frontP;
    }

    void clipTo(BSPNode bsp) {
        this.polygons = bsp.clipPolygons(this.polygons);
        if (this.front != null) {
            this.front.clipTo(bsp);
        }
        if (this.back != null) {
            this.back.clipTo(bsp);
        }
    }

    List<Polygon> getAllPolygons() {
        var ret = new ArrayList<>(this.polygons);
        if (this.front != null) {
            ret.addAll(this.front.getAllPolygons());
        }
        if (this.back != null) {
            ret.addAll(this.back.getAllPolygons());
        }
        return ret;
    }

    void build(List<Polygon> polygons) {
        if (polygons.isEmpty()) {
            return;
        }
        if (this.plane == null) {
            this.plane = BSPPlane.createFromVertices(polygons.get(0).vertices);
        }
        var frontP = (List<Polygon>) new ArrayList<Polygon>();
        var backP = (List<Polygon>) new ArrayList<Polygon>();
        // do not use parallel version
        polygons.forEach(polygon -> this.plane.splitPolygon(polygon, this.polygons, this.polygons, frontP, backP));
        if (frontP.size() > 0) {
            if (this.front == null) {
                this.front = new BSPNode();
            }
            this.front.build(frontP);
        }
        if (backP.size() > 0) {
            if (this.back == null) {
                this.back = new BSPNode();
            }
            this.back.build(backP);
        }
    }
}
