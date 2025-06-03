package finalproject.game.entities.attack.hitbox;

import finalproject.engine.ecs.Entity;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.physics.colliders.Collider;
import org.jetbrains.annotations.NotNull;

public class RepelHitbox extends Hitbox {
    protected double newtons;

    public RepelHitbox(Box<Vec2> pos, Collider collider, Entity owner, double despawnTime, double damage, double newtonsPerDamage) {
        super(pos, collider, owner, despawnTime, damage);
        this.newtons = newtonsPerDamage * damage;
    }

    public RepelHitbox(Box<Vec2> pos, Collider collider, Entity owner, double damage, double newtonsPerDamage) {
        super(pos, collider, owner, damage);
        this.newtons = newtonsPerDamage * damage;
    }

    public RepelHitbox(Vec2 pos, Collider collider, Entity owner, double damage, double newtonsPerDamage) {
        this(new BasicBox<>(pos), collider, owner, damage, newtonsPerDamage);
    }

    public RepelHitbox(Vec2 pos, Collider collider, Entity owner, double despawnTime, double damage, double newtonsPerDamage) {
        this(new BasicBox<>(pos), collider, owner, despawnTime, damage, newtonsPerDamage);
    }

    @Override
    protected Vec2 getKnockbackForce(Entity other, @NotNull Collider otherCollider) {
        Vec2 direction = otherCollider.getCenter().sub(pos.get()).norm();
        return direction.mul(newtons);
    }
}
