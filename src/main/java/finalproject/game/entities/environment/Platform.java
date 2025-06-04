package finalproject.game.entities.environment;

import finalproject.engine.util.box.BasicBox;
import finalproject.game.components.markers.physics.colliders.RectCollider;
import finalproject.game.components.renderables.sprite.ImageSprite;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.util.rendering.TextureManager;
import finalproject.game.util.rendering.TileMap;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class Platform implements Entity {
    public final Box<Vec2> pos;
    public final Vec2 dimensions;
    public final RectCollider collider;
    public final HashSet<Entity> fallingEntities = new HashSet<>();
    Image image;

    public Platform(Vec2 pos, @NotNull Vec2 dimensions) {
        this.pos = new BasicBox<>(pos);
        this.dimensions = dimensions;
        collider = new RectCollider(this.pos, new BasicBox<>(this.dimensions));
        image = TextureManager.Environment.PLACEHOLDER_TILE.tileRect((int) dimensions.getX(), (int) dimensions.getY());
    }

    public Platform(Vec2 pos, Vec2 dimensions, @NotNull TileMap tileMap) {
        this(pos, dimensions);
        this.image = tileMap.tileRect((int) dimensions.getX(), (int) dimensions.getY());
    }

    public List<Vec2> getTopEdges() {
        Vec2 pos2 = pos.get();

        return List.of(
                pos2.sub(dimensions.div(2)),
                pos2.add(dimensions.div(2).mulY(-1))
        );
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addRenderable(new ImageSprite(pos, image));
        r.addMarker(collider);
    }
}
