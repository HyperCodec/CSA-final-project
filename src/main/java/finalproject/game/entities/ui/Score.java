package finalproject.game.entities.ui;

import finalproject.engine.Camera;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Score implements Entity, Renderable {
    public final Box<Integer> val = new BasicBox<>(0);

    public void removeScore(int amount) {
        val.set(Math.max(val.get() - amount, 0));
    }

    public void addScore(int amount) {
        val.set(val.get() + amount);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addRenderable(this);
    }

    @Override
    public void render(@NotNull Graphics g, Camera mainCamera) {
        g.setColor(Color.BLACK);

        g.drawString("Score: " + val.get(), 75, 20);
    }

    @Override
    public int getLayer() {
        return -1;
    }
}
