package finalproject.engine;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Engine extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
    final static int WIDTH = 800;
    final static int HEIGHT = 600;
    final static Color BACKGROUND_COLOR = new Color(255,255,255);

    public Engine() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(BACKGROUND_COLOR);
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(@NotNull MouseEvent e) {
        Graphics g = getGraphics();

        g.setColor(Color.BLACK);
        g.drawOval(e.getX(), e.getY(), 10, 10);
        repaint();
    }
}
