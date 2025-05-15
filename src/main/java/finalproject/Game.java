package finalproject;

import finalproject.engine.Engine;
import finalproject.entities.MouseInputTest;

import javax.swing.*;

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
        engine.addEntity(new MouseInputTest());
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}