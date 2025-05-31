package finalproject.game.entities.character;

import finalproject.game.components.markers.physics.colliders.CharacterCollider;
import finalproject.game.components.markers.physics.Rigidbody;
import finalproject.game.components.markers.physics.colliders.EllipseCollider;
import finalproject.game.components.renderables.sprite.EllipseSprite;
import finalproject.game.components.renderables.sprite.Sprite;
import finalproject.game.components.tickables.physics.Drag;
import finalproject.game.components.tickables.physics.Gravity;
import finalproject.game.components.tickables.physics.CharacterController;
import finalproject.game.components.tickables.physics.VelocityPositionUpdater;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.game.util.Box;
import finalproject.game.util.CardinalDirection;
import finalproject.game.util.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

public class Player implements Entity, Tickable {
    public final static double MOVE_SPEED = 50;
    public final static double FAST_FALL_VELOCITY = 25;
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
    HashSet<CardinalDirection> heldDirections = new HashSet<>();

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
        CharacterCollider collider = new EllipseCollider(pos, ellipseDims);
        r.addMarker(collider);

        CharacterController controller = new CharacterController(collider, vel, grounded, fallDuration);
        r.addTickable(controller);

        Drag drag = new Drag(0.25, rb);
        r.addTickable(drag);

        VelocityPositionUpdater updater = new VelocityPositionUpdater(pos, vel);
        r.addTickable(updater);

        Sprite sprite = new EllipseSprite(pos, Color.cyan, ellipseDims);
        r.addRenderable(sprite);

        addMovementKeybind(r, CardinalDirection.UP, "W", "up");
        addMovementKeybind(r, CardinalDirection.UP, "UP", "up2");

        addMovementKeybind(r, CardinalDirection.DOWN, "S", "down");
        addMovementKeybind(r, CardinalDirection.DOWN, "DOWN", "down2");

        addMovementKeybind(r, CardinalDirection.LEFT, "A", "left");
        addMovementKeybind(r, CardinalDirection.LEFT, "LEFT", "left2");

        addMovementKeybind(r, CardinalDirection.RIGHT, "D", "right");
        addMovementKeybind(r, CardinalDirection.RIGHT, "RIGHT", "right2");

        r.addTickable(this);
    }

    @Override
    public void tick(WorldAccessor world, double dt) {
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

    private void addMovementKeybind(@NotNull EntityComponentRegistry r, CardinalDirection dir, String key, String name) {
        r.addKeybind(KeyStroke.getKeyStroke(key), name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                heldDirections.add(dir);
            }
        });
        r.addKeybind(KeyStroke.getKeyStroke("released " + key), "released" + name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                heldDirections.remove(dir);
            }
        });
    }
}
