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
    public final Box<Vec2> pos;
    public final Box<Vec2> dimensions;

    public Platform(Vec2 pos, Vec2 dimensions) {
        this.pos = new Box<>(pos);
        this.dimensions = new Box<>(dimensions);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        Vec2 dims = dimensions.get();
        r.addRenderable(new RectangleSprite(pos, (int) dims.getX(), (int) dims.getY(), Color.BLUE));
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
}
