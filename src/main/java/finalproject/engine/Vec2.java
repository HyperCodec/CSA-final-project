package finalproject.engine;

import org.apache.commons.lang3.builder.HashCodeBuilder;
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

    public Vec2 addSingle(double v) {
        return new Vec2(x + v, y + v);
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

    public Vec2 subSingle(double v) {
        return new Vec2(x - v, y - v);
    }

    public Vec2 mul(@NotNull Vec2 v) {
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

    public double dot(@NotNull Vec2 v) {
        return x * v.x + y * v.y;
    }

    public double magSq() {
        return dot(this);
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

    public Vec2 rotate(double radians) {
        return new Vec2(
                x * Math.cos(radians) - y * Math.sin(radians),
                x * Math.sin(radians) + y * Math.cos(radians)
        );
    }

    public double getAngle() {
        // expensive operation
        return Math.atan2(y, x);
    }

    public double angleBetween(@NotNull Vec2 v) {
        return Math.acos(dot(v) / (mag() * v.mag()));
    }

    @Override
    public String toString() {
        return String.format("Vec2(%.2f, %.2f)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vec2 other)
            return x == other.x && y == other.y;
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31)
                .append(x)
                .append(y)
                .toHashCode();
    }
}
