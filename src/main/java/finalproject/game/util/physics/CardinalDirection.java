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
}
