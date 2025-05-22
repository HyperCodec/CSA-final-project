package finalproject.engine.util;

import org.jetbrains.annotations.NotNull;

/**
 * Simple Euclid vector implementation.
 * Immutable type, all methods create a new object.
 */
public class Vec2 {
    public final static Vec2 ZERO = new Vec2(0, 0);

    final double x, y;

    public Vec2(double all) {
        this(all, all);
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(@NotNull Vec2 v) {
        return new Vec2(x + v.x, y + v.y);
    }
    public Vec2 addX(double v) {
        return new Vec2(x + v, y);
    }

    public Vec2 addY(double v) {
        return new Vec2(x, y + v);
    }

    public Vec2 sub(@NotNull Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }
    public Vec2 subX(double v) {
        return new Vec2(x - v, y);
    }

    public Vec2 subY(double v) {
        return new Vec2(x, y - v);
    }

    public Vec2 dot(@NotNull Vec2 v) {
        return new Vec2(x * v.x, y * v.y);
    }

    public Vec2 mulX(double v) {
        return new Vec2(x * v, y);
    }

    public Vec2 mulY(double v) {
        return new Vec2(x, y * v);
    }

    public Vec2 mulSingle(double val) {
        return new Vec2(x * val, y * val);
    }

    public Vec2 div(@NotNull Vec2 v) {
        return new Vec2(x / v.x, y / v.y);
    }

    public Vec2 divSingle(double val) {
        return new Vec2(x / val, y / val);
    }

    public double magSq() {
        return x * x + y * y;
    }

    public double mag() {
        return Math.sqrt(magSq());
    }

    public Vec2 norm() {
        double m = mag();
        if(m == 0) return ZERO;
        return divSingle(m);
    }

    public Vec2 xx() {
        return new Vec2(x, x);
    }

    public Vec2 yy() {
        return new Vec2(y, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
