package finalproject.entities.environment;

import finalproject.components.markers.Collider;
import finalproject.components.renderables.sprite.RectangleSprite;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Platform implements Entity {
    public final static double HEIGHT = 5;

    public final Box<Vec2> pos;
    public double width;

    public Platform(Vec2 pos, double width) {
        this.pos = new Box<>(pos);
        this.width = width;
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addRenderable(new RectangleSprite(pos, (int) width, (int) HEIGHT, Color.BLUE));
    }

    public boolean isColliding(@NotNull Collider collider) {
        Vec2 colliderCenter = collider.getCenter();
        double colliderCX = colliderCenter.getX();
        double colliderCY = colliderCenter.getY();

        // check center is in the platform
        if(top() <= colliderCY && bottom() >= colliderCY &&
                left() <= colliderCX && right() >= colliderCX)
            return true;

        // check corner collision
        for(Vec2 corner : getCorners()) {
            if(collider.contains(corner)) {
                return true;
            }
        }

        return false;
    }

    public double top() {
        return pos.get().getY() - HEIGHT / 2;
    }

    public double bottom() {
        return pos.get().getY() + HEIGHT / 2;
    }

    public double left() {
        return pos.get().getX() - width / 2;
    }

    public double right() {
        return pos.get().getX() + width / 2;
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
