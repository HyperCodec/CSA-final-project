package finalproject.engine.ecs;

public interface Tickable {
    void tick(WorldAccessor world, double dt);
}
