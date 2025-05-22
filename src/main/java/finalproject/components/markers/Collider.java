package finalproject.components.markers;

import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;

public abstract class Collider {
    protected Box<Vec2> pos;

    protected Collider(Box<Vec2> pos) {
        this.pos = pos;
    }

    public abstract boolean contains(Vec2 point);
}
