package finalproject.components.tickables.physics;

import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Ref;
import finalproject.engine.util.Vec2;

public class Gravity implements Tickable {
    Ref<Vec2> velocity;
    double acceleration;

    public Gravity(Ref<Vec2> velocity, double acceleration) {
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public Gravity(Ref<Vec2> velocity) {
        this(velocity, 9.8);
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        double dy = acceleration * dt;
        velocity.set(velocity.get().addY(dy));
    }
}
