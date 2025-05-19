package finalproject.entities;

import finalproject.engine.WorldAccessor;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.ecs.Tickable;

import java.awt.*;

public class FpsDisplay implements Entity, Tickable, Renderable {
    // TODO finish

    public static double getFps(double dt) {
        return 1 / dt;
    }

    @Override
    public void spawn(EntityComponentRegistry r) {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void tick(WorldAccessor world, double dt) {

    }
}
