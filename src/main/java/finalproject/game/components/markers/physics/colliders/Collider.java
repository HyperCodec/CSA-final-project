package finalproject.game.components.markers.physics.colliders;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Collider {
    protected Box<Vec2> pos;

    protected Collider(Box<Vec2> pos) {
        this.pos = pos;
    }

    public abstract boolean contains(Vec2 point);
    public abstract List<Vec2> getEdgePoints();

    public boolean isColliding(@NotNull Collider other) {
        if(contains(other.getCenter())) return true;

        for(Vec2 point : getEdgePoints()) {
            if(other.contains(point))
                return true;
        }

        return false;
    }

    public Vec2 getCenter() {
        return pos.get();
    }

    public void setCenter(Vec2 center) {
        pos.set(center);
    }
}
