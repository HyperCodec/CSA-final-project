package finalproject.game.components.tickables.physics;

import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.game.util.Box;
import finalproject.game.util.Vec2;

public class Gravity implements Tickable {
    Box<Vec2> velocity;
    Box<Boolean> active;
    double acceleration;

    public Gravity(Box<Vec2> velocity, Box<Boolean> active, double acceleration) {
        this.velocity = velocity;
        this.active = active;
        this.acceleration = acceleration;
    }

    public Gravity(Box<Vec2> velocity, Box<Boolean> active) {
        this(velocity, active, 9.8);
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        if(!active.get()) return;

        double dy = acceleration * dt;
        velocity.set(velocity.get().addY(dy));
    }
}
