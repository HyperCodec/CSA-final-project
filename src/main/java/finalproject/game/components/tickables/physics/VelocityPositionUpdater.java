package finalproject.game.components.tickables.physics;

import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.util.Box;
import finalproject.game.util.physics.UnitConversions;
import finalproject.engine.util.Vec2;

public class VelocityPositionUpdater implements Tickable {
    Box<Vec2> pos;
    Box<Vec2> vel;

    public VelocityPositionUpdater(Box<Vec2> pos, Box<Vec2> vel) {
        this.pos = pos;
        this.vel = vel;
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        // pos is in px, vel is in m/s.
        Vec2 delta = vel.get().mulSingle(dt * UnitConversions.PIXELS_PER_METER);
        pos.set(pos.get().add(delta));
    }
}
