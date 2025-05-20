package finalproject.entities;

import finalproject.engine.WorldAccessor;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.ecs.Tickable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FpsDisplay implements Entity, Tickable, Renderable {
    double fps;
    double timeSinceLastUpdate = 0;

    public static double getFps(double dt) {
        return (double) Math.round(100 / dt) / 100;
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addTickable(this);
        r.addRenderable(this);
    }

    @Override
    public void render(@NotNull Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("FPS: " + fps, 10, 20);
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        // update every second to make it more readable
        timeSinceLastUpdate += dt;
        if(timeSinceLastUpdate >= 1.0) {
            timeSinceLastUpdate = 0;
            fps = getFps(dt);
        }
    }
}
