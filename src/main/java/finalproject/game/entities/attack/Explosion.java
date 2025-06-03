package finalproject.game.entities.attack;

import finalproject.engine.util.Vec2;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.BasicBox;
import finalproject.game.components.markers.Damageable;
import finalproject.game.components.markers.physics.colliders.CircleCollider;
import finalproject.game.components.markers.physics.colliders.Collider;
import finalproject.game.components.renderables.sprite.AnimatedSprite;
import finalproject.game.components.tickables.DespawnAfterTime;
import finalproject.engine.util.box.Box;
import finalproject.game.util.ResourceUtils;
import finalproject.game.util.rendering.SpriteSheet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Explosion extends Hitbox implements Tickable {
    // probably should just move this texture stuff to TextureManager.
    final static SpriteSheet EXPLOSION_TEXTURE;

    static {
        try {
            EXPLOSION_TEXTURE = new SpriteSheet(ResourceUtils.readImage(Explosion.class, "assets/textures/dynamite/explosion.png"), 1, 8);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    final static double GRAPHICS_Y_OFFSET = 15;
    final static double ANIMATION_FRAME_DELAY = 0.1;
    final static double DESPAWN_TIME = EXPLOSION_TEXTURE.countImages() * ANIMATION_FRAME_DELAY;

    public Explosion(Vec2 pos, double radius, Entity owner, double damage) {
        super(pos, new CircleCollider(new BasicBox<>(pos), radius), owner, DESPAWN_TIME, damage);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        AnimatedSprite sprite = new AnimatedSprite(new BasicBox<>(pos.get().subY(GRAPHICS_Y_OFFSET)), EXPLOSION_TEXTURE.getImages(), ANIMATION_FRAME_DELAY);
        r.addRenderable(sprite);
        r.addTickable(sprite);
    }
}
