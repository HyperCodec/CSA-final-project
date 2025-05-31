package finalproject.components.markers.physics;

import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;

public abstract class PlatformerCollider extends Collider {
    protected PlatformerCollider(Box<Vec2> pos) {
        super(pos);
    }

    public abstract Vec2 getBottom();
    public abstract void alignBottom(double y);
}
