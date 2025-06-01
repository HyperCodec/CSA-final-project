package finalproject.game.entities.character;

import finalproject.game.components.markers.physics.colliders.AlignableCollider;
import finalproject.game.components.markers.physics.Rigidbody;
import finalproject.game.components.markers.physics.colliders.EllipseCollider;
import finalproject.game.components.renderables.sprite.EllipseSprite;
import finalproject.game.components.renderables.sprite.Sprite;
import finalproject.game.components.tickables.physics.Drag;
import finalproject.game.components.tickables.physics.Gravity;
import finalproject.game.components.tickables.physics.PlatformCollision;
import finalproject.game.components.tickables.physics.VelocityPositionUpdater;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Box;
import finalproject.game.entities.projectile.Dynamite;
import finalproject.game.util.physics.CardinalDirection;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashSet;

public class Player implements Entity, Tickable {
    public final static double MOVE_SPEED = 50;
    public final static double FAST_FALL_VELOCITY = 20;
    public final static double JUMP_SPEED = 100;
    public final static double JUMP_END_VELOCITY = 5;
    public final static double MAX_JUMP_TIME = 0.5;

    // prob shouldn't be making these public, but
    // I hate using getters/setters and entities
    // aren't part of the main abstraction anyway
    public final Box<Vec2> pos;
    public final Box<Vec2> vel = new Box<>(Vec2.ZERO);
    public final Box<Boolean> grounded = new Box<>(false);
    public final Box<Double> fallDuration = new Box<>(0.0);
    public final Box<Boolean> useGravity = new Box<>(true);

    double jumpHeldDuration = 0;
    boolean isJumping = false;

    public Player(Vec2 pos) {
        this.pos = new Box<>(pos);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        Gravity gravity = new Gravity(vel, useGravity);
        r.addTickable(gravity);

        Rigidbody rb = new Rigidbody(10, vel);
        r.addMarker(rb);

        Box<Vec2> ellipseDims = new Box<>(new Vec2(10, 20));
        AlignableCollider collider = new EllipseCollider(pos, ellipseDims);
        r.addMarker(collider);

        PlatformCollision controller = new PlatformCollision(collider, vel, grounded, fallDuration);
        r.addTickable(controller);

        Drag drag = new Drag(0.25, rb);
        r.addTickable(drag);

        VelocityPositionUpdater updater = new VelocityPositionUpdater(pos, vel);
        r.addTickable(updater);

        Sprite sprite = new EllipseSprite(pos, Color.cyan, ellipseDims);
        r.addRenderable(sprite);

        r.addTickable(this);
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double dt) {
        handleMovement(world, dt);
        handleAttack(world);
    }

    private void handleMovement(@NotNull WorldAccessor world, double dt) {
        HashSet<CardinalDirection> heldDirections = world.getHeldDirections();

        Vec2 pos2 = pos.get();

        CardinalDirection[] lr = {CardinalDirection.LEFT, CardinalDirection.RIGHT};
        for(CardinalDirection dir : lr)
            if(heldDirections.contains(dir))
                pos2 = pos2.add(dir.toVector().mulSingle(MOVE_SPEED * dt));

        if(heldDirections.contains(CardinalDirection.DOWN))
            vel.set(new Vec2(vel.get().getX(), FAST_FALL_VELOCITY));

        pos.set(pos2);

        if(heldDirections.contains(CardinalDirection.UP)) {
            if(grounded.get()) {
                isJumping = true;
                grounded.set(false);

                // disable gravity since
                // we're manually handling velocity
                // and don't want to accumulate anything
                // while moving
                useGravity.set(false);
            }

            if(isJumping) {
                jumpHeldDuration += dt;

                if(jumpHeldDuration <= MAX_JUMP_TIME) {
                    pos.set(pos.get().subY(JUMP_SPEED * dt));
                    vel.set(new Vec2(vel.get().getX(), -0.001));
                    return;
                }
            }
        }

        if(isJumping)
            vel.set(new Vec2(vel.get().getX(), -JUMP_END_VELOCITY));
        isJumping = false;
        jumpHeldDuration = 0;
        useGravity.set(true);
    }

    public final static double THROW_VELOCITY = 15;
    public final static double DYNAMITE_TIME = 3;

    private void handleAttack(@NotNull WorldAccessor world) {
        if(world.mouseJustStartedClick()) {
            Vec2 pos2 = pos.get();
            Vec2 mousePos = world.getMousePos();
            Vec2 absoluteMousePos = world.getMainCamera().getAbsolutePos(mousePos);

            Vec2 direction = absoluteMousePos.sub(pos2).norm();
            Vec2 projVel = direction.mulSingle(THROW_VELOCITY).add(vel.get());

            world.addEntity(new Dynamite(pos2, projVel, DYNAMITE_TIME, this));
        }
    }
}
