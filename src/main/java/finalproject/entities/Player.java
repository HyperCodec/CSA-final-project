package finalproject.entities;

import finalproject.components.renderables.sprite.AnimatedSprite;
import finalproject.components.renderables.sprite.PointSprite;
import finalproject.components.tickables.physics.Gravity;
import finalproject.components.tickables.physics.VelocityPositionUpdater;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Ref;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Player implements Entity, MouseListener, KeyListener {
    Ref<Vec2> pos;
    Ref<Vec2> vel = new Ref<>(Vec2.ZERO);

    public Player(Vec2 pos) {
        this.pos = new Ref<>(pos);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        Gravity gravity = new Gravity(vel);
        r.addTickable(gravity);

        VelocityPositionUpdater updater = new VelocityPositionUpdater(pos, vel);
        r.addTickable(updater);

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
