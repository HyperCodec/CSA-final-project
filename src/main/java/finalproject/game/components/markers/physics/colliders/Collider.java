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
        return contains(other.getCenter()) || outerIntersect(other);
    }

    /**
     * Returns true if the collider intersects with the other collider,
     * without checking if the other collider contains the center of this collider.
     * @param other the other collider to check for intersection with.
     * @return true if the colliders intersect, false otherwise.
     */
    public boolean outerIntersect(Collider other) {
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
