package finalproject;

import finalproject.engine.Camera;
import finalproject.engine.Engine;
import finalproject.engine.Vec2;
import finalproject.game.entities.environment.Platform;
import finalproject.game.entities.ui.FpsDisplay;
import finalproject.game.entities.character.Player;
import finalproject.game.util.LevelUtils;

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
        engine.getKeybindManager().registerDefaultKeybinds();

        // static UI elements
        engine.addEntity(new FpsDisplay());

        // actual level data
        try {
            engine.addEntity(LevelUtils.parseFromResources("test_level.json"));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}