package finalproject.game.components.tickables;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;

public class DespawnAfterTime implements Tickable {
    Entity parent;
    double despawnTime;

    public DespawnAfterTime(Entity parent, double despawnTime) {
        this.parent = parent;
        this.despawnTime = despawnTime;
    }

    @Override
    public void tick(WorldAccessor world, double dt) {
        despawnTime -= dt;

        if(despawnTime <= 0) {
            world.destroyEntity(parent);
        }
    }
}
