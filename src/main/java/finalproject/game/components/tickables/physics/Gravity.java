package finalproject.game.components.tickables.physics;

import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;

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

    public Gravity(Box<Vec2> velocity) {
        this(velocity, new BasicBox<>(true));
    }

    public Gravity(Box<Vec2> velocity, double acceleration) {
        this(velocity, new BasicBox<>(true), acceleration);
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        if(!active.get()) return;

        double dy = acceleration * dt;
        velocity.set(velocity.get().addY(dy));
    }
}
