package finalproject.game.components.markers.physics.colliders;

import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

public class CircleCollider extends AlignableCollider {
    double radius;
    double cornerComponent;

    public CircleCollider(Box<Vec2> pos, double radius) {
        super(pos);

        this.radius = radius;
        cornerComponent = radius / Math.sqrt(2);
    }

    @Override
    public boolean contains(@NotNull Vec2 point) {
        Vec2 center = pos.get();

        return point.sub(center).magSq() <= radius * radius;
    }

    @Override
    public boolean isColliding(@NotNull Collider other) {
        Vec2 center = pos.get();
        Vec2 otherCenter = other.getCenter();

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

        // check points around circle with ambiguous collider
        Vec2[] cardinals = {
                center.addX(radius),
                center.addY(radius),
                center.subX(radius),
                center.subY(radius),
        };
        for(Vec2 cardinal : cardinals)
            if(other.contains(cardinal))
                return true;

        // corners
        for(int xcoef = -1; xcoef <= 1; xcoef += 2) {
            for(int ycoef = -1; ycoef <= 1; ycoef += 2) {
                Vec2 delta = new Vec2(xcoef, ycoef).mulSingle(cornerComponent);
                Vec2 point = center.add(delta);

                if(other.contains(point))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void alignBottom(double y) {
        pos.set(new Vec2(pos.get().getX(), y - radius));
    }

    @Override
    public Vec2 getBottom() {
        return pos.get().addY(radius);
    }
}
