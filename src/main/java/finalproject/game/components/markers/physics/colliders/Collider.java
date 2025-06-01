package finalproject.game.components.markers.physics.colliders;

import finalproject.game.util.Box;
import finalproject.engine.Vec2;

public abstract class Collider {
    protected Box<Vec2> pos;

    protected Collider(Box<Vec2> pos) {
        this.pos = pos;
    }

    public abstract boolean contains(Vec2 point);
    public abstract boolean isColliding(Collider other);

    public Vec2 getCenter() {
        return pos.get();
    }

    public void setCenter(Vec2 center) {
        pos.set(center);
    }
}
