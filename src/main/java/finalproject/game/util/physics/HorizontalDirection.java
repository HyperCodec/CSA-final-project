package finalproject.game.util.physics;

import finalproject.engine.util.Vec2;

public enum HorizontalDirection {
    LEFT(CardinalDirection.LEFT.toVector()),
    RIGHT(CardinalDirection.RIGHT.toVector());

    final Vec2 val;

    HorizontalDirection(Vec2 val) {
        this.val = val;
    }

    public Vec2 toVector() {
        return val;
    }
}
