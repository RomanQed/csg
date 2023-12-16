package com.github.romanqed.graphic;

final class AreaRasterizer extends AbstractRasterizer {
    private final double[] buffer;

    AreaRasterizer(Raster raster, double[] buffer) {
        super(raster);
        this.buffer = buffer;
    }

    @Override
    public void rasterize(Triangle triangle, int value) {
        // Coordinates
        // First vertex
        var x1 = triangle.first.x();
        var y1 = triangle.first.y();
        var z1 = triangle.first.z();
        // Second vertex
        var x2 = triangle.second.x();
        var y2 = triangle.second.y();
        var z2 = triangle.second.z();
        // Third vertex
        var x3 = triangle.third.x();
        var y3 = triangle.third.y();
        var z3 = triangle.third.z();
        // Bounds
        var minX = (int) Math.max(0, Math.ceil(Math.min(x1, Math.min(x2, x3))));
        var maxX = (int) Math.min(width - 1, Math.floor(Math.max(x1, Math.max(x2, x3))));
        var minY = (int) Math.max(0, Math.ceil(Math.min(y1, Math.min(y2, y3))));
        var maxY = (int) Math.min(height - 1, Math.floor(Math.max(y1, Math.max(y2, y3))));
        // Calculate triangle area
        var area = (y1 - y3) * (x2 - x3) + (y2 - y3) * (x3 - x1);
        // Point-by-point rendering
        for (var y = minY; y <= maxY; ++y) {
            for (var x = minX; x <= maxX; ++x) {
                var b1 = ((y - y3) * (x2 - x3) + (y2 - y3) * (x3 - x)) / area;
                var b2 = ((y - y1) * (x3 - x1) + (y3 - y1) * (x1 - x)) / area;
                var b3 = ((y - y2) * (x1 - x2) + (y1 - y2) * (x2 - x)) / area;
                if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                    var depth = b1 * z1 + b2 * z2 + b3 * z3;
                    var index = y * width + x;
                    if (buffer[index] < depth) {
                        raster.setPixel(x, y, value);
                        buffer[index] = depth;
                    }
                }
            }
        }
    }
}
