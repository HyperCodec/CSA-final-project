package finalproject;

import finalproject.engine.Engine;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    Engine engine;

    public Game() {
        super();

        engine = new Engine();
        add(engine);
        addKeyListener(engine);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
    }

    public void start() {
        setVisible(true);

        Graphics g = engine.getGraphics();
        setup();

        // testing
        g.setColor(Color.BLACK);
        g.drawRect(WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT);

        engine.paint(g);
        // TODO main event loop
    }

    private void setup() {

    }

    public static void main(String[] args) {
        // prevent graphics processing from blocking the UI (keypresses and such)
        // by running it on a swing-managed thread
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();

            game.start();
        });
    }
}