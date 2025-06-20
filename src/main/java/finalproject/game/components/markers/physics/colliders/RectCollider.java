package finalproject.game.components.markers.physics.colliders;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RectCollider extends AlignableCollider {
    final static double DIST_BETWEEN_POINTS = 2.5;

    Box<Vec2> dimensions;

    public RectCollider(Box<Vec2> pos, Box<Vec2> dimensions) {
        super(pos);
        this.dimensions = dimensions;
    }

    @Override
    public boolean contains(@NotNull Vec2 point) {
        return point.getX() >= left() && point.getX() <= right() &&
                point.getY() >= top() && point.getY() <= bottom();
    }

    @Override
    public boolean isColliding(@NotNull Collider other) {
        if (other instanceof RectCollider rect)
            return left() < rect.right() && right() > rect.left() &&
                    top() > rect.bottom() && bottom() < rect.top();

        Vec2 center = pos.get();
        Vec2 otherCenter = other.getCenter();

        if (contains(otherCenter))
            return true;

        List<Vec2> edgePoints = getEdgePoints();

        for (Vec2 point : edgePoints) {
            if (other.contains(point))
                return true;
        }

        return false;
    }

    @Override
    public void alignTop(double y) {
        pos.set(new Vec2(pos.get().getX(), y + dimensions.get().getY() / 2));
    }

    @Override
    public void alignLeft(double x) {
        pos.set(new Vec2(x + dimensions.get().getX() / 2, pos.get().getY()));
    }

    @Override
    public void alignRight(double x) {
        pos.set(new Vec2(x - dimensions.get().getX() / 2, pos.get().getY()));
    }

    @Override
    public ArrayList<Vec2> getEdgePoints() {
        ArrayList<Vec2> points = new ArrayList<>(List.of(getCorners()));

        // top and bottom edges
        double[] tby = {top(), bottom()};
        for(double x = left(); x < right(); x += DIST_BETWEEN_POINTS) {
            for(double y : tby) {
                points.add(new Vec2(x, y));
            }
        }

        // left and right edges
        double[] lrx = {left(), right()};
        for(double y = top(); y < bottom(); y += DIST_BETWEEN_POINTS) {
            for(double x : lrx) {
                points.add(new Vec2(x, y));
            }
        }

        return points;
    }

    @Override
    public void alignBottom(double y) {
        double height = dimensions.get().getY();
        double dy = height / 2;
        pos.set(new Vec2(pos.get().getX(), y - dy));
    }

    @Override
    public double top() {
        return pos.get().getY() - dimensions.get().getY() / 2;
    }

    @Override
    public double bottom() {
        return pos.get().getY() + dimensions.get().getY() / 2;
    }

    @Override
    public double left() {
        return pos.get().getX() - dimensions.get().getX() / 2;
    }

    @Override
    public double right() {
        return pos.get().getX() + dimensions.get().getX() / 2;
    }

    public Vec2[] getCorners() {
        double top = top();
        double bottom = bottom();
        double left = left();
        double right = right();

        return new Vec2[]{
                new Vec2(left, top),
                new Vec2(right, bottom),
                new Vec2(right, top),
                new Vec2(left, bottom),
        };
    }

    public Vec2 getDimensions() {
        return dimensions.get();
    }

}
