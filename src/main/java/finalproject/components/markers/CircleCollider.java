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
}
