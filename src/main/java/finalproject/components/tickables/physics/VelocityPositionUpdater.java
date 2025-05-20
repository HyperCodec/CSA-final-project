package finalproject.components.tickables.physics;

import finalproject.engine.WorldAccessor;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.util.Ref;
import finalproject.engine.util.Vec2;

public class VelocityPositionUpdater implements Tickable {
    Ref<Vec2> pos;
    Ref<Vec2> vel;

    public VelocityPositionUpdater(Ref<Vec2> pos, Ref<Vec2> vel) {
        this.pos = pos;
        this.vel = vel;
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        Vec2 delta = vel.get().mulSingle(dt);
        pos.set(pos.get().add(delta));
    }
}
