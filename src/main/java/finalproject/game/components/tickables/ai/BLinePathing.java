package finalproject.game.components.tickables.ai;

import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Axis;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.VectorComponent;
import finalproject.engine.util.box.Box;

import java.util.List;

public abstract class BLinePathing extends PathTweener {
    double speed;

    public BLinePathing(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, int startNode, double distanceMargin, double speed) {
        super(pos, active, path, startNode, distanceMargin);
        this.speed = speed;
    }

    public BLinePathing(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, int startNode, double speed) {
        this(pos, active, path, startNode, 1.0, speed);
    }

    public BLinePathing(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, double distanceMargin, double speed) {
        super(pos, active, path, distanceMargin);
        this.speed = speed;
    }

    public BLinePathing(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, double speed) {
        super(pos, active, path);
        this.speed = speed;
    }

    @Override
    public void step(WorldAccessor world, double dt) {
        Vec2 currentPos = pos.get();
        double x = getCurrentNode().getX() > currentPos.getX() ? 1 : -1;

        pos.set(currentPos.addX(x * speed * dt));
    }
}
