package finalproject.game.components.tickables.physics;

import finalproject.game.components.markers.physics.Rigidbody;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Vec2;

public class Drag implements Tickable {
    double dragCoefficient;
    Rigidbody rb;

    public Drag(double dragCoefficient, Rigidbody rb) {
        this.dragCoefficient = dragCoefficient;
        this.rb = rb;
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        Vec2 vel = rb.getVelocity();
        Vec2 drag = vel.norm().mul(-dragCoefficient * vel.magSq());
        rb.applyForce(drag, dt);
    }
}
