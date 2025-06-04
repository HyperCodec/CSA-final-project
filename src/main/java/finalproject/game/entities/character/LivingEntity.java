package finalproject.game.entities.character;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.Damageable;
import finalproject.game.components.markers.physics.Rigidbody;
import finalproject.game.components.markers.physics.colliders.AlignableCollider;
import finalproject.game.components.markers.physics.colliders.EllipseCollider;
import finalproject.game.components.tickables.physics.Drag;
import finalproject.game.components.tickables.physics.GeneralCollision;
import finalproject.game.components.tickables.physics.Gravity;
import finalproject.game.components.tickables.physics.VelocityPositionUpdater;
import finalproject.game.entities.environment.Platform;
import org.jetbrains.annotations.NotNull;

public abstract class LivingEntity implements Entity {
    public final Box<Vec2> pos;
    public final Box<Vec2> vel;
    public final Box<Boolean> grounded = new BasicBox<>(false);
    public final Box<Boolean> fallThroughPlatforms = new BasicBox<>(false);
    public final Box<Boolean> useGravity = new BasicBox<>(true);
    public final Box<Boolean> intangible = new BasicBox<>(false);
    public final Box<Double> fallDuration = new BasicBox<>(0.0);
    public final Damageable health;
    public final AlignableCollider collider;
    public final Rigidbody rb;

    protected LivingEntity(Vec2 pos, double curHealth, double maxHealth, Vec2 colliderDims, double mass) {
        this.pos = new BasicBox<>(pos);
        this.vel = new BasicBox<>(Vec2.ZERO);
        this.health = new Damageable(curHealth, maxHealth, intangible) {
            @Override
            public void onDeath(WorldAccessor world) {
                LivingEntity.this.onDeath(world);
            }

            @Override
            public void onDamage(WorldAccessor world, double amount) {
                LivingEntity.this.onDamage(world, amount);
            }
        };
        this.collider = new EllipseCollider(this.pos, new BasicBox<>(colliderDims));
        this.rb = new Rigidbody(mass, this.vel);
    }

    protected LivingEntity(Vec2 pos, double maxHealth, Vec2 colliderDims, double mass) {
        this(pos, maxHealth, maxHealth, colliderDims, mass);
    }

    // probably not the most eloquent solution,
    // but I'm tired of making event systems.
    protected abstract void onDamage(WorldAccessor world, double amount);
    protected abstract void onDeath(WorldAccessor world);

    protected abstract void onTouchPlatform(WorldAccessor world, Platform platform);

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addMarker(health);
        r.addMarker(collider);
        r.addMarker(rb);

        r.addTickable(new GeneralCollision(collider, vel, grounded, fallThroughPlatforms, fallDuration, this) {
            @Override
            protected void onTouchPlatform(WorldAccessor world, Platform platform) {
                LivingEntity.this.onTouchPlatform(world, platform);
            }
        });
        r.addTickable(new Gravity(vel, useGravity));
        r.addTickable(new Drag(0.25, rb));
        r.addTickable(new VelocityPositionUpdater(pos, vel));
    }
}
