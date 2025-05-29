package finalproject.entities;

import finalproject.components.markers.CircleCollider;
import finalproject.components.markers.Collider;
import finalproject.components.renderables.sprite.PointSprite;
import finalproject.components.tickables.physics.Gravity;
import finalproject.components.tickables.physics.Platformer;
import finalproject.components.tickables.physics.VelocityPositionUpdater;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

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
    }

    // TODO transition to new input system
    // https://docs.oracle.com/javase/tutorial/uiswing/misc/keybinding.html
    public void keyPressed(@NotNull KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyChar());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: case KeyEvent.VK_W:
                System.out.println("jump pressed");
                if(grounded.get()) {
                    System.out.println("jumping");
                    vel.set(vel.get().subY(10));
                    grounded.set(false);
                }
                break;
        }
    }
}
