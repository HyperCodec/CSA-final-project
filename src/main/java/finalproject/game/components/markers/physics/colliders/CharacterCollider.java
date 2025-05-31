package finalproject.game.components.markers.physics.colliders;

import finalproject.game.util.Box;
import finalproject.game.util.Vec2;

public abstract class CharacterCollider extends Collider {
    protected CharacterCollider(Box<Vec2> pos) {
        super(pos);
    }

    public abstract Vec2 getBottom();
    public abstract void alignBottom(double y);
}
