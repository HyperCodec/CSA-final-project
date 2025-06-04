package finalproject.game.entities.attack;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.physics.colliders.CircleCollider;
import finalproject.game.components.markers.physics.colliders.Collider;
import finalproject.game.components.renderables.sprite.ImageSprite;
import finalproject.game.components.tickables.physics.VelocityPositionUpdater;
import finalproject.game.entities.attack.hitbox.Hitbox;
import finalproject.game.util.ResourceUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Arrow extends Hitbox {
    final static Image ARROW_IMAGE;

    static {
        try {
            ARROW_IMAGE = ResourceUtils.readImage(Arrow.class, "assets/textures/arrow.png");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static double DESPAWN_TIME = 20;
    public final static double DAMAGE = 5;
    public final static double KNOCKBACK = 100;
    public final Box<Vec2> vel;

    public Arrow(Vec2 pos, Vec2 vel, Entity owner) {
        super(pos, null, owner, DESPAWN_TIME, DAMAGE);
        this.vel = new BasicBox<>(vel);
        // cursed way of doing this, but I don't have time
        // to make a good abstraction here.
        collider = new CircleCollider(this.pos, 5);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        super.spawn(r);
        r.addRenderable(new ImageSprite(pos, ARROW_IMAGE));
        r.addTickable(new VelocityPositionUpdater(pos, vel));
    }

    @Override
    protected Vec2 getKnockbackForce(Entity other, @NotNull Collider otherCollider) {
        return otherCollider.getCenter().sub(pos.get()).norm().mul(new Vec2(KNOCKBACK, 0));
    }

    @Override
    protected void onCollide(@NotNull WorldAccessor world, Entity other, Collider otherCollider) {
        world.destroyEntity(this);
    }
}
