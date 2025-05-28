package finalproject.components.markers;

import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

public class CircleCollider extends Collider {
    double radius;

    public CircleCollider(Box<Vec2> pos, double radius) {
        super(pos);

        this.radius = radius;
    }

    @Override
    public boolean contains(@NotNull Vec2 point) {
        Vec2 center = pos.get();

        return point.sub(center).magSq() <= radius * radius;
    }

    // TODO https://www.jeffreythompson.org/collision-detection/circle-rect.php
    @Override
    public boolean isColliding(@NotNull Collider other) {
        Vec2 center = pos.get();
        Vec2 otherCenter = other.getCenter();

        if(other instanceof RectCollider rect) {
            // find closest edges. if point is in circle, there is a collision.
            double x;
            double y;

            if(center.getX() < rect.left())
                x = rect.left();
            else
                x = rect.right();

            if(center.getY() < rect.top())
        }

        double distSq = center.sub(otherCenter).magSq();

        if(other instanceof CircleCollider circle) {
            // simplified check since the radius is known
            double maxDist = radius + circle.radius;
            return distSq <= maxDist * maxDist;
        }

        // check if center is in circle
        // (not using contains since distSq
        // was already calculated)
        if(distSq <= radius * radius)
            return true;

        // TODO check points along edges

        return false;
    }

    @Override
    public void alignBottom(double y) {
        pos.set(new Vec2(pos.get().getX(), y - radius));
    }
}
