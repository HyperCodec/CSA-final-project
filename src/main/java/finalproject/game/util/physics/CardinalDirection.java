package finalproject.game.util.physics;

import finalproject.engine.util.Vec2;
import finalproject.engine.util.VectorComponent;
import org.jetbrains.annotations.NotNull;

public enum CardinalDirection {
    UP(new Vec2(0, -1)),
    DOWN(new Vec2(0, 1)),
    LEFT(new Vec2(-1, 0)),
    RIGHT(new Vec2(1, 0));

    final Vec2 val;

    CardinalDirection(Vec2 val) {
        this.val = val;
    }

    public Vec2 toVector() {
        return val;
    }

    public final static CardinalDirection[] HORIZONTAL = {RIGHT, LEFT};
    public final static CardinalDirection[] VERTICAL = {UP, DOWN};

    public HorizontalDirection toHorizontal() {
        return switch (this) {
            case RIGHT -> HorizontalDirection.RIGHT;
            case LEFT -> HorizontalDirection.LEFT;
            default -> throw new IllegalArgumentException("CardinalDirection must be horizontal");
        };
    }

    public CardinalDirection opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public static CardinalDirection fromVector(@NotNull Vec2 vec) {
        if(vec.getX() == 0) {
            if(vec.getY() < 0) return UP;
            else return DOWN;
        } else if(vec.getY() == 0) {
            if(vec.getX() > 0) return RIGHT;
            else return LEFT;
        } else {
            // maybe do angle-based
            throw new IllegalArgumentException("Vector must be horizontal or vertical");
        }
    }

    public static CardinalDirection fromComponent(@NotNull VectorComponent v) {
        return switch(v.getAxis()) {
            case X -> v.getVal() > 0 ? RIGHT : LEFT;
            case Y -> v.getVal() > 0 ? DOWN : UP;
        };
    }
}
