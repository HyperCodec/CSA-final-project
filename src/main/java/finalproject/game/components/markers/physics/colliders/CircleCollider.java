package finalproject.game.components.markers.physics.colliders;

import finalproject.engine.util.VectorComponent;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.util.physics.CardinalDirection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        for(Vec2 point : getEdgePoints()) {
            if(other.contains(point))
                return true;
        }

        return false;
    }

    @Override
    public ArrayList<Vec2> getEdgePoints() {
        Vec2 center = pos.get();
        ArrayList<Vec2> points = new ArrayList<>(List.of(getCardinalPoints()));

        // corners
        for(int xcoef = -1; xcoef <= 1; xcoef += 2) {
            for(int ycoef = -1; ycoef <= 1; ycoef += 2) {
                Vec2 delta = new Vec2(xcoef, ycoef).mul(cornerComponent);
                Vec2 point = center.add(delta);

                points.add(point);
            }
        }

        return points;
    }

    @Override
    public void alignBottom(double y) {
        pos.set(new Vec2(pos.get().getX(), y - radius));
    }

    @Override
    public void alignTop(double y) {
        pos.set(new Vec2(pos.get().getX(), y + radius));
    }

    @Override
    public void alignLeft(double x) {
        pos.set(new Vec2(x + radius, pos.get().getY()));
    }

    @Override
    public void alignRight(double x) {
        pos.set(new Vec2(x - radius, pos.get().getY()));
    }

    @Override
    public double left() {
        return pos.get().getX() - radius;
    }

    @Override
    public double right() {
        return pos.get().getX() + radius;
    }

    @Override
    public double top() {
        return pos.get().getY() - radius;
    }

    @Override
    public double bottom() {
        return pos.get().getY() + radius;
    }
}
