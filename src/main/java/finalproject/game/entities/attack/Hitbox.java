package finalproject.game.entities.attack;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.Damageable;
import finalproject.game.components.markers.physics.colliders.Collider;
import finalproject.game.components.tickables.DespawnAfterTime;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Hitbox implements Entity, Tickable {
    public final Box<Vec2> pos;
    public final Collider collider;
    public final Entity owner;
    public final double despawnTime;
    public final double damage;

    // allow importing box directly to
    // make the hitbox move with the parent entity
    public Hitbox(Box<Vec2> pos, Collider collider, Entity owner, double despawnTime, double damage) {
        this.pos = pos;
        this.collider = collider;
        this.owner = owner;
        this.despawnTime = despawnTime;
        this.damage = damage;
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addMarker(collider);
        r.addTickable(new DespawnAfterTime(this, despawnTime));
        r.addTickable(this);
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double dt) {
        HashMap<Entity, Damageable> damageables = world.findEntitiesWithMarker(Damageable.class);

        damageables.remove(owner);

        for(Map.Entry<Entity, Damageable> kv : damageables.entrySet()) {
            Entity ent = kv.getKey();

            Collider collider = world.findMarkerInEntity(ent, Collider.class);
            if(collider.isColliding(this.collider))
                kv.getValue().damage(world, damage);
        }
    }
}
