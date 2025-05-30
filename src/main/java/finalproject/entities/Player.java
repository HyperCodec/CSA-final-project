package finalproject.entities;

import finalproject.components.markers.CircleCollider;
import finalproject.components.markers.Collider;
import finalproject.components.markers.physics.Rigidbody;
import finalproject.components.renderables.sprite.PointSprite;
import finalproject.components.tickables.physics.Drag;
import finalproject.components.tickables.physics.Gravity;
import finalproject.components.tickables.physics.Platformer;
import finalproject.components.tickables.physics.VelocityPositionUpdater;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Player implements Entity {
    // bad practice but using getters
    // and setters is super annoying
    public final Box<Vec2> pos;
    public final Box<Vec2> vel = new Box<>(Vec2.ZERO);
    public final Box<Boolean> grounded = new Box<>(false);

    public Player(Vec2 pos) {
        this.pos = new Box<>(pos);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        Gravity gravity = new Gravity(vel);
        r.addTickable(gravity);

        VelocityPositionUpdater updater = new VelocityPositionUpdater(pos, vel);
        r.addTickable(updater);

        Collider collider = new CircleCollider(pos, 10);
        r.addMarker(collider);

        Platformer platformController = new Platformer(collider, vel, grounded);
        r.addTickable(platformController);

        PointSprite sprite = new PointSprite(pos, Color.cyan, 10);
        r.addRenderable(sprite);

        Rigidbody rb = new Rigidbody(10, vel);
        r.addMarker(rb);

        Drag drag = new Drag(0.1, rb);
        r.addTickable(drag);

        r.addKeybind(KeyStroke.getKeyStroke("W"), "jump", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent _e) {
                System.out.println("jump pressed");
                if(grounded.get()) {
                    System.out.println("jumping");
                    vel.set(vel.get().subY(10));
                    grounded.set(false);
                }
            }
        });

        r.addKeybind(KeyStroke.getKeyStroke("D"), "right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vec2 vel2 = vel.get();
                if(vel2.getX() > 10) return;

                vel.set(new Vec2(5, vel2.getY()));
            }
        });

        r.addKeybind(KeyStroke.getKeyStroke("A"), "left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vec2 vel2 = vel.get();
                if(vel2.getX() < -10) return;

                vel.set(new Vec2(-5, vel2.getY()));
            }
        });
    }
}
