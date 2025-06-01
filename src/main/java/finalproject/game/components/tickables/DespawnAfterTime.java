package finalproject.game.components.tickables;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.game.util.Timer;

public class DespawnAfterTime implements Tickable {
    Entity parent;
    Timer timer;

    public DespawnAfterTime(Entity parent, double despawnTime) {
        this.parent = parent;
        timer = new Timer(despawnTime);
    }

    @Override
    public void tick(WorldAccessor world, double dt) {
        if(timer.tick(dt)) {
            world.destroyEntity(parent);
        }
    }
}
