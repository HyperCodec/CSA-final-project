package finalproject.entities;

import finalproject.components.renderables.PointCollection;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputTest implements Entity, MouseListener, MouseMotionListener {
    PointCollection points;

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        points = new PointCollection(Color.BLUE, 5);
        r.addMouseListener(this);
        r.addMouseMotionListener(this);
        r.addRenderable(points);
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(@NotNull MouseEvent e) {
        Vec2 pos = new Vec2(e.getX(), e.getY());
        points.points.add(pos);
        System.out.println(pos);
    }
}
