package finalproject;

import finalproject.engine.Engine;

import javax.swing.*;
import java.awt.*;

public class Main {
    final static int WIDTH = 800;
    final static int HEIGHT = 600;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Canvas canvas = new Canvas();
        canvas.setSize(WIDTH, HEIGHT);

        frame.add(canvas);
        frame.pack();

        Graphics renderer = canvas.getGraphics();

        Engine engine = new Engine(renderer);
        setup(engine);

        frame.setVisible(true);

        renderer.setColor(Color.BLACK);
        renderer.drawRect(WIDTH/2, HEIGHT/2, WIDTH, HEIGHT);
        // TODO main event loop
    }

    static void setup(Engine engine) {

    }
}