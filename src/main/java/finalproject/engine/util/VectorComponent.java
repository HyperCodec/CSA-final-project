package finalproject.engine.util;

import org.jetbrains.annotations.NotNull;

public class VectorComponent {
    double val;
    Axis axis;

    public VectorComponent(double val, Axis axis) {
        this.val = val;
        this.axis = axis;
    }

    public double getVal() {
        return val;
    }

    public Axis getAxis() {
        return axis;
    }

    public String toString() {
        return axis + ": " + val;
    }

    public VectorComponent add(@NotNull Vec2 vec) {
        return new VectorComponent(val + vec.getValue(axis), axis);
    }

    public VectorComponent add(double val) {
        return new VectorComponent(this.val + val, axis);
    }

    public VectorComponent sub(@NotNull Vec2 vec) {
        return new VectorComponent(val - vec.getValue(axis), axis);
    }

    public VectorComponent sub(double val) {
        return new VectorComponent(this.val - val, axis);
    }

    public VectorComponent mul(@NotNull Vec2 vec) {
        return new VectorComponent(val * vec.getValue(axis), axis);
    }

    public VectorComponent mul(double scalar) {
        return new VectorComponent(val * scalar, axis);
    }

    public VectorComponent div(@NotNull Vec2 vec) {
        return new VectorComponent(val / vec.getValue(axis), axis);
    }

    public VectorComponent div(double scalar) {
        return new VectorComponent(val / scalar, axis);
    }
}
