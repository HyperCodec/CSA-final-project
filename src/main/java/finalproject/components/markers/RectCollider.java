package finalproject.components.markers;

import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

public class RectCollider extends Collider {
    Box<Vec2> dimensions;

    public RectCollider(Box<Vec2> pos, Box<Vec2> dimensions) {
        super(pos);
        this.dimensions = dimensions;
    }

    @Override
    public boolean contains(@NotNull Vec2 point) {
        return point.getX() <= left() && point.getX() >= right() &&
                point.getY() >= top() && point.getY() <= bottom();
    }

    @Override
    public boolean isColliding(@NotNull Collider other) {
        if(other instanceof RectCollider rect)
            return left() < rect.right() && right() > rect.left() &&
                    top() > rect.bottom() && bottom() < rect.top();

        Vec2 center = pos.get();
        Vec2 otherCenter = other.getCenter();

        if(contains(otherCenter))
            return true;

        // TODO point checks

        return false;
    }

    @Override
    public void alignBottom(double y) {

    }

    public double top() {
        return pos.get().getY() - dimensions.get().getY() / 2;
    }

    public double bottom() {
        return pos.get().getY() + dimensions.get().getY() / 2;
    }

    public double left() {
        return pos.get().getX() - dimensions.get().getX() / 2;
    }

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
}
