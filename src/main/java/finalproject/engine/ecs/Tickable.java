package finalproject.engine.ecs;

import finalproject.engine.WorldAccessor;

public interface Tickable {
    void tick(WorldAccessor world, double dt);
}
