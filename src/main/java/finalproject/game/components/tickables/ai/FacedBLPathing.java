package finalproject.game.components.tickables.ai;

import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Axis;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.Box;
import finalproject.game.util.physics.CardinalDirection;
import finalproject.game.util.physics.HorizontalDirection;

import java.util.List;

public abstract class FacedBLPathing extends BLinePathing {
    final Box<HorizontalDirection> facing;

    public FacedBLPathing(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, double speed, Box<HorizontalDirection> facing) {
        super(pos, active, path, speed);
        this.facing = facing;
    }

    public FacedBLPathing(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, double distanceMargin, double speed, Box<HorizontalDirection> facing) {
        super(pos, active, path, distanceMargin, speed);
        this.facing = facing;
    }

    public FacedBLPathing(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, int startNode, double speed, Box<HorizontalDirection> facing) {
        super(pos, active, path, startNode, speed);
        this.facing = facing;
    }

    public FacedBLPathing(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, int startNode, double distanceMargin, double speed, Box<HorizontalDirection> facing) {
        super(pos, active, path, startNode, distanceMargin, speed);
        this.facing = facing;
    }

    @Override
    public void step(WorldAccessor world, double dt) {
        // might want to add a call to super or something
        Vec2 currentPos = pos.get();
        Vec2 direction = getCurrentNode().sub(currentPos).norm();
        pos.set(currentPos.add(direction.mul(speed * dt)));
        facing.set(CardinalDirection.fromComponent(direction.getComponent(Axis.X)).toHorizontal());
    }
}
