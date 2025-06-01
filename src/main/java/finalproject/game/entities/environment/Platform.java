package finalproject.game.entities.environment;

import finalproject.game.components.markers.physics.colliders.RectCollider;
import finalproject.game.components.renderables.sprite.RectangleSprite;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.game.util.Box;
import finalproject.engine.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Platform implements Entity {
    public final Box<Vec2> pos;
    public final Box<Vec2> dimensions;
    public final RectCollider collider;

    public Platform(Vec2 pos, Vec2 dimensions) {
        this.pos = new Box<>(pos);
        this.dimensions = new Box<>(dimensions);
        collider = new RectCollider(this.pos, this.dimensions);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addRenderable(new RectangleSprite(pos, dimensions, Color.BLUE));
        r.addMarker(collider);
    }
}
