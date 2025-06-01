package finalproject.game.entities.ui;

import finalproject.engine.Camera;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.ecs.Tickable;
import finalproject.game.util.Timer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FpsDisplay implements Entity, Tickable, Renderable {
    double fps;
    final Timer timer = new Timer(1, true);

    public static double getFps(double dt) {
        return (double) Math.round(100 / dt) / 100;
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addTickable(this);
        r.addRenderable(this);
    }

    @Override
    public void render(@NotNull Graphics g, Camera _mainCamera) {
        g.setColor(Color.BLACK);
        g.drawString("FPS: " + fps, 10, 20);
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        // only update once every second to make it more readable
        if(timer.tick(dt))
            fps = getFps(dt);
    }
}
