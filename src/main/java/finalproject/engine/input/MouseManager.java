package finalproject.engine.input;

import finalproject.engine.Engine;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.HashSet;

public class MouseManager implements MouseListener, MouseMotionListener {
    boolean isClicking = false;
    HashSet<MouseListener> mouseListeners = new HashSet<>();
    HashSet<MouseMotionListener> mouseMotionListeners = new HashSet<>();

    public MouseManager(@NotNull Engine engine) {
        engine.addMouseListener(this);
        engine.addMouseMotionListener(this);
    }

    public synchronized void addMouseListener(MouseListener sub) {
        mouseListeners.add(sub);
    }

    public synchronized void addMouseMotionListener(MouseMotionListener sub) {
        mouseMotionListeners.add(sub);
    }

    public synchronized void removeMouseListener(MouseListener sub) {
        mouseListeners.remove(sub);
    }

    public synchronized void removeAllMouseListeners(Collection<MouseListener> subs) {
        mouseListeners.removeAll(subs);
    }

    public synchronized void removeMouseMotionListener(MouseMotionListener sub) {
        mouseMotionListeners.remove(sub);
    }

    public synchronized void removeAllMouseMotionListeners(Collection<MouseMotionListener> subs) {
        mouseMotionListeners.removeAll(subs);
    }

    @Override
    public synchronized void mouseClicked(MouseEvent e) {
        for(MouseListener sub : mouseListeners)
            sub.mouseClicked(e);
    }

    @Override
    public synchronized void mousePressed(MouseEvent e) {
        isClicking = true;
        for(MouseListener sub : mouseListeners)
            sub.mousePressed(e);
    }

    @Override
    public synchronized void mouseReleased(MouseEvent e) {
        isClicking = false;
        for(MouseListener sub : mouseListeners)
            sub.mouseReleased(e);
    }

    @Override
    public synchronized void mouseEntered(MouseEvent e) {
        for(MouseListener sub : mouseListeners)
            sub.mouseEntered(e);
    }

    @Override
    public synchronized void mouseExited(MouseEvent e) {
        for(MouseListener sub : mouseListeners)
            sub.mouseExited(e);
    }

    @Override
    public synchronized void mouseDragged(MouseEvent e) {
        for(MouseMotionListener sub : mouseMotionListeners)
            sub.mouseDragged(e);
    }

    @Override
    public synchronized void mouseMoved(MouseEvent e) {
        for(MouseMotionListener sub : mouseMotionListeners)
            sub.mouseMoved(e);
    }
}
