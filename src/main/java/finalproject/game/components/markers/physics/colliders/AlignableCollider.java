package finalproject.game.components.markers.physics.colliders;

import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;

public abstract class AlignableCollider extends Collider {
    protected AlignableCollider(Box<Vec2> pos) {
        super(pos);
    }

    public abstract Vec2 getBottom();
    public abstract void alignBottom(double y);
}
