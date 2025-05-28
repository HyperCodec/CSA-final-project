package finalproject;

import finalproject.engine.Engine;
import finalproject.engine.util.Vec2;
import finalproject.entities.environment.Platform;
import finalproject.entities.ui.FpsDisplay;
import finalproject.entities.Player;

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
        engine.addEntity(new FpsDisplay());
        Player p = new Player(new Vec2((double) Engine.WIDTH / 2, (double) Engine.HEIGHT / 2));
        engine.addEntity(p);
        engine.addEntity(new Platform(p.pos.get().addY(15), new Vec2(50, 5)));
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}