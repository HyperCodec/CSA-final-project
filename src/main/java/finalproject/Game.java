package finalproject;

import finalproject.engine.Engine;
import finalproject.engine.util.Vec2;
import finalproject.game.entities.environment.BackgroundImage;
import finalproject.game.entities.ui.FpsDisplay;
import finalproject.game.entities.ui.Score;
import finalproject.game.util.SceneUtils;
import finalproject.game.util.rendering.TextureManager;
import finalproject.game.util.rendering.TileMap;

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

        setTitle("CSA Final Game");
    }

    public void start() {
        setVisible(true);
        setup();

        engine.startGameLoop();
    }

    private void setup() {
        engine.getKeysManager().registerDefaultKeybinds();

        // static stuff
        engine.addEntity(new FpsDisplay());
        engine.addEntity(new Score());
        engine.addEntity(new BackgroundImage(new TileMap(TextureManager.Environment.BACKGROUND_IMAGE).tileRect(2379, 793), new Vec2(0, -75), 0.2));

        // actual level data
        try {
            engine.addEntity(SceneUtils.parseFromResources("levels/level1.json"));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}