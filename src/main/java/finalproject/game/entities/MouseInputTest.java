package finalproject.game.entities;

import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.renderables.sprite.geometry.CircleSprite;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputTest implements Entity, MouseListener, MouseMotionListener {
    public final static Color HOVER_COLOR = Color.BLUE;
    public final static Color CLICK_COLOR = Color.RED;
    public final static int RADIUS = 5;

    Box<Vec2> mousePos = new BasicBox<>(Vec2.ZERO);
    CircleSprite sprite = new CircleSprite(mousePos, HOVER_COLOR, RADIUS);

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addMouseListener(this);
        r.addMouseMotionListener(this);
        r.addRenderable(sprite);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        sprite.setColor(CLICK_COLOR);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        sprite.setColor(HOVER_COLOR);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(@NotNull MouseEvent e) {
        sprite.setPos(new Vec2(e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(@NotNull MouseEvent e) {
        sprite.setPos(new Vec2(e.getX(), e.getY()));
    }
}
