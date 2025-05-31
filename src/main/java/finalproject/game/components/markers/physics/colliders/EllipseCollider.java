package finalproject.game.components.markers.physics.colliders;

import finalproject.game.util.Box;
import finalproject.game.util.Vec2;
import org.jetbrains.annotations.NotNull;

public class EllipseCollider extends CharacterCollider {
    public final static double ANGLE_BETWEEN_CHECKS = Math.PI / 6;

    Box<Vec2> dimensions;

    public EllipseCollider(Box<Vec2> pos, Box<Vec2> dimensions) {
        super(pos);

        this.dimensions = dimensions;
    }

    @Override
    public Vec2 getBottom() {
        return pos.get().addY(dimensions.get().getY() / 2);
    }

    @Override
    public void alignBottom(double y) {
        double height = dimensions.get().getY();
        double dy = height / 2;
        pos.set(new Vec2(pos.get().getX(), y - dy));
    }

    @Override
    public boolean contains(@NotNull Vec2 point) {
        Vec2 pos2 = pos.get();
        Vec2 dimensions2 = dimensions.get();

        double a = dimensions2.getX() / 2;
        double b = dimensions2.getY() / 2;

        double dx = point.getX() - pos2.getX();
        double dy = point.getY() - pos2.getY();

        return (dx * dx) / (a * a) +
                (dy * dy) / (b * b)
                <= 1;
    }

    @Override
    public boolean isColliding(@NotNull Collider other) {
        Vec2 otherCenter = other.getCenter();

        if(contains(otherCenter))
            return true;

        for(double angle = 0; angle <= 2 * Math.PI; angle += ANGLE_BETWEEN_CHECKS) {
            Vec2 point = getEdgePoint(angle);
            if(other.contains(point))
                return true;
        }

        return false;
    }

    // https://math.stackexchange.com/questions/22064/calculating-a-point-that-lies-on-an-ellipse-given-an-angle
    public Vec2 getEdgePoint(double angle) {
        // a lot of these operations are really
        // expensive, should probably optimize or
        // derive my own formula.
        double sign = (0 <= angle && angle < Math.PI / 2) ||
                (3 * Math.PI / 2 < angle && angle <= 2 * Math.PI ) ?
                1 : -1;

        Vec2 center = pos.get();
        Vec2 dimensions2 = dimensions.get();

        double cx = center.getX();
        double cy = center.getY();

        double a = dimensions2.getX() / 2;
        double b = dimensions2.getY() / 2;

        double tan = Math.tan(angle);
        double divisor = Math.sqrt(b * b + a * a * tan * tan);

        double x = (a * b / divisor) + cx;
        double y = (a * b * tan / divisor) - cy;

        Vec2 edgePoint = new Vec2(x, y);
        return edgePoint.mulSingle(sign);
    }
}
