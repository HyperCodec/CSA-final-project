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

public class Explosion implements Entity, Tickable {
    final static SpriteSheet EXPLOSION_TEXTURE;
    final static double GRAPHICS_Y_OFFSET = 15;

    static {
        try {
            EXPLOSION_TEXTURE = new SpriteSheet(ResourceUtils.readImage(Explosion.class, "assets/textures/dynamite/explosion.png"), 1, 8);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final HashSet<Entity> hit = new HashSet<>();
    public final Box<Vec2> pos;
    public final double radius;
    public final double damage;
    final Entity owner;

    Collider hitbox;

    public Explosion(Vec2 pos, double radius, double damage, Entity owner) {
        this.pos = new BasicBox<>(pos);
        this.radius = radius;
        this.damage = damage;
        this.owner = owner;
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        hitbox = new CircleCollider(pos, radius);
        r.addMarker(hitbox);

        AnimatedSprite sprite = new AnimatedSprite(new BasicBox<>(pos.get().subY(GRAPHICS_Y_OFFSET)), EXPLOSION_TEXTURE.getImages(), 0.1);
        r.addRenderable(sprite);
        r.addTickable(sprite);

        r.addTickable(new DespawnAfterTime(this, sprite.getCycleDuration()));
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double dt) {
        HashMap<Entity, Damageable> damageables = world.findEntitiesWithMarker(Damageable.class);

        damageables.remove(owner);

        // for some reason HashMap doesn't have removeAll
        for(Entity ent : hit)
            damageables.remove(ent);

        for(Map.Entry<Entity, Damageable> kv : damageables.entrySet()) {
            Entity ent = kv.getKey();
            Collider hurtbox = world.findMarkerInEntity(ent, Collider.class);

            if(hurtbox.isColliding(hitbox)) {
                kv.getValue().damage(world, damage);
                hit.add(ent);
            }
        }
    }
}
