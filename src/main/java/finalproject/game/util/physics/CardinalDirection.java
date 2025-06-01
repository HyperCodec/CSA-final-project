package finalproject.game.util.physics;

import finalproject.engine.util.Vec2;

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
}
