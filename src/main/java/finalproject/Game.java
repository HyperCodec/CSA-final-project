package finalproject;

import finalproject.engine.Engine;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    Engine engine;

    public Game() {
        super();

        engine = new Engine();
        add(engine);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
    }

    public void start() {
        setVisible(true);
        setup();

        engine.startGameLoop();
    }

    private void setup() {
        // TODO add components
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}