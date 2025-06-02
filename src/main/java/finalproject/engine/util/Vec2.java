package finalproject.engine.util;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

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

    public Vec2 add(double v) {
        return new Vec2(x + v, y + v);
    }

    public Vec2 add(@NotNull VectorComponent v) {
        return switch(v.getAxis()) {
            case X -> addX(v.getVal());
            case Y -> addY(v.getVal());
        };
    }

    public VectorComponent addComponent(@NotNull VectorComponent v) {
        Axis axis = v.getAxis();
        return new VectorComponent(getValue(axis) + v.getVal(), axis);
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

    public Vec2 sub(double v) {
        return new Vec2(x - v, y - v);
    }

    public Vec2 sub(@NotNull VectorComponent v) {
        return switch(v.getAxis()) {
            case X -> subX(v.getVal());
            case Y -> subY(v.getVal());
        };
    }

    public VectorComponent subComponent(@NotNull VectorComponent v) {
        Axis axis = v.getAxis();
        return new VectorComponent(getValue(axis) - v.getVal(), axis);
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

    public Vec2 mul(double val) {
        return new Vec2(x * val, y * val);
    }

    public Vec2 mul(@NotNull VectorComponent v) {
        return switch(v.getAxis()) {
            case X -> mulX(v.getVal());
            case Y -> mulY(v.getVal());
        };
    }

    public VectorComponent mulComponent(@NotNull VectorComponent v) {
        Axis axis = v.getAxis();
        return new VectorComponent(getValue(axis) * v.getVal(), axis);
    }

    public Vec2 div(@NotNull Vec2 v) {
        return new Vec2(x / v.x, y / v.y);
    }

    public Vec2 div(double val) {
        return new Vec2(x / val, y / val);
    }

    public Vec2 divX(double val) {
        return new Vec2(x / val, y);
    }

    public Vec2 divY(double val) {
        return new Vec2(x, y / val);
    }

    public Vec2 div(@NotNull VectorComponent v) {
        return switch(v.getAxis()) {
            case X -> divX(v.getVal());
            case Y -> divY(v.getVal());
        };
    }

    public VectorComponent divComponent(@NotNull VectorComponent v) {
        Axis axis = v.getAxis();
        return new VectorComponent(getValue(axis) / v.getVal(), axis);
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
        return div(m);
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

    public double getValue(@NotNull Axis axis) {
        return switch(axis) {
            case X -> x;
            case Y -> y;
        };
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

    public Vec2 getClosest(@NotNull Collection<Vec2> others) {
        double smallestDist = Double.POSITIVE_INFINITY;
        Vec2 closest = null;
        for(Vec2 other : others) {
            double dist = this.sub(other).magSq();
            if(dist < smallestDist) {
                smallestDist = dist;
                closest = other;
            }
        }
        return closest;
    }

    public VectorComponent getClosestComponent(@NotNull Collection<VectorComponent> others) {
        double smallestDist = Double.POSITIVE_INFINITY;
        VectorComponent closest = null;
        for(VectorComponent other : others) {
            double dist = Math.abs(this.getValue(other.getAxis()) - other.getVal());
            if(dist < smallestDist) {
                smallestDist = dist;
                closest = other;
            }
        }

        return closest;
    }

    public Vec2 withComponent(@NotNull VectorComponent v) {
        return switch(v.getAxis()) {
            case X -> new Vec2(v.getVal(), y);
            case Y -> new Vec2(x, v.getVal());
        };
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
