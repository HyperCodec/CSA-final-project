package finalproject.game.components.markers.physics.colliders;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.util.physics.CardinalDirection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EllipseCollider extends AlignableCollider {
    public final static double ANGLE_BETWEEN_CHECKS = Math.PI / 12;

    final Box<Vec2> dimensions;

    public EllipseCollider(Box<Vec2> pos, Box<Vec2> dimensions) {
        super(pos);

        this.dimensions = dimensions;
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
    public List<Vec2> getEdgePoints() {
        ArrayList<Vec2> points = new ArrayList<>();

        for(double angle = 0; angle <= 2 * Math.PI; angle += ANGLE_BETWEEN_CHECKS) {
            Vec2 point = getEdgePoint(angle);
            points.add(point);
        }

        return points;
    }

    public Vec2 getEdgePoint(double angle) {
        // ofc when I derive it myself, it works perfectly.
        // never trust math stackexchange.
        Vec2 center = pos.get();
        Vec2 dimensions2 = dimensions.get();

        double a = dimensions2.getX() / 2;
        double b = dimensions2.getY() / 2;

        double x = a * Math.cos(angle);
        double y = b * Math.sin(angle);

        return new Vec2(x, y).add(center);
    }

    @Override
    public double left() {
        return pos.get().getX() - dimensions.get().getX() / 2;
    }

    @Override
    public double right() {
        return pos.get().getX() + dimensions.get().getX() / 2;
    }

    @Override
    public double top() {
        return pos.get().getY() - dimensions.get().getY() / 2;
    }

    @Override
    public double bottom() {
        return pos.get().getY() + dimensions.get().getY() / 2;
    }

    public Vec2 getDimensions() {
        return dimensions.get();
    }
}
