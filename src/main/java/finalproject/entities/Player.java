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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Player implements Entity, MouseListener, KeyListener {
    public Box<Vec2> pos;
    public Box<Vec2> vel = new Box<>(Vec2.ZERO);

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

        Platformer platformController = new Platformer(collider, vel);
        r.addTickable(platformController);

        PointSprite sprite = new PointSprite(pos, Color.cyan, 10);
        r.addRenderable(sprite);

        r.addMouseListener(this);
        r.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
