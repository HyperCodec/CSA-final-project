package finalproject;

import finalproject.engine.Engine;
import finalproject.game.entities.ui.FpsDisplay;
import finalproject.game.util.SceneUtils;

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
        engine.getKeysManager().registerDefaultKeybinds();

        // static UI elements
        engine.addEntity(new FpsDisplay());

        // actual level data
        try {
            engine.addEntity(SceneUtils.parseFromResources("levels/test_level.json"));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}