package finalproject.game.components.markers.physics;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

/**
 * Provides control over force/velocity.
 */
public class Rigidbody {
    double mass;
    Box<Vec2> vel;

    public Rigidbody(double mass, Box<Vec2> vel) {
        this.mass = mass;
        this.vel = vel;
    }

    public void applyAccel(@NotNull Vec2 accel, double dt) {
        vel.set(vel.get().add(accel.mul(dt)));
    }

    public Vec2 applyForce(@NotNull Vec2 force, double dt) {
        Vec2 accel = force.div(mass);
        applyAccel(accel, dt);
        return accel;
    }

    public Vec2 getVelocity() {
        return vel.get();
    }

    public void setVelocity(Vec2 vel) {
        this.vel.set(vel);
    }

    public Vec2 getForce() {
        return vel.get().mul(mass);
    }
}
