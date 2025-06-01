package finalproject.game.components.markers.physics;

import finalproject.engine.Vec2;

public class Surface {
    public double staticFriction;
    public double kineticFriction;

    public Surface(double staticFriction, double kineticFriction) {
        this.staticFriction = staticFriction;
        this.kineticFriction = kineticFriction;
    }

    // TODO maybe use coefficient between two surfaces
    public Vec2 getKineticFriction(double surfaceAngle, Vec2 netForce) {
        netForce = netForce.rotate(surfaceAngle);

        double parallel = netForce.getX();
        double parallelMag = Math.abs(parallel);
        double direction = parallel / parallelMag;

        double normal = netForce.getY();
        double friction = Math.min(normal * kineticFriction, parallelMag);

        Vec2 frictionVec = new Vec2(friction * direction, 0);
        return frictionVec.rotate(-surfaceAngle);
    }

    public Vec2 getFriction(double surfaceAngle, Vec2 netForce) {
        netForce = netForce.rotate(surfaceAngle);

        double parallel = netForce.getX();
        if(parallel == 0)
            return Vec2.ZERO;

        double parallelMag = Math.abs(parallel);
        double direction = parallel / parallelMag;

        double normal = netForce.getY();
        if(normal <= 0) return Vec2.ZERO;

        double friction = normal * staticFriction;
        if(parallelMag > friction)
            friction = normal * kineticFriction;
        friction = Math.min(friction, parallelMag);

        Vec2 frictionVec = new Vec2(friction * direction, 0);
        return frictionVec.rotate(-surfaceAngle);
    }
}
