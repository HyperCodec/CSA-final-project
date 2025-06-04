package finalproject.game.components.tickables.ai;

import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.Box;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class PathTweener implements Tickable {
    protected final Box<Vec2> pos;
    protected final Box<Boolean> active;
    protected List<Vec2> path;
    protected int currentNode;
    protected int direction = 1;
    protected double distanceMargin;

    public PathTweener(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, int startNode, double distanceMargin) {
        this.pos = pos;
        this.active = active;
        this.path = path;
        this.currentNode = startNode;
        this.distanceMargin = distanceMargin;
    }

    public PathTweener(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, int startNode) {
        this(pos, active, path, startNode, 1.0);
    }

    public PathTweener(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path, double distanceMargin) {
        this.pos = pos;
        this.active = active;
        this.path = path;
        this.currentNode = getClosestNode();
        this.distanceMargin = distanceMargin;
    }

    public PathTweener(Box<Vec2> pos, Box<Boolean> active, List<Vec2> path) {
        this(pos, active, path, 1.0);
    }

    public int getClosestNode() {
        return pos.get().getClosestIndex(path);
    }

    public int reverse() {
        direction = -direction;
        return direction;
    }

    public Vec2 getCurrentNode() {
        if(path.isEmpty()) return null;
        return path.get(currentNode);
    }

    public void setPath(List<Vec2> path, int defaultNode) {
        this.path = path;
        currentNode = defaultNode;
    }

    public void setPath(@NotNull List<Vec2> path) {
        if(path.equals(this.path)) return;
        setPath(path, 0);
    }

    public void nextNode(WorldAccessor world, double dt) {
        currentNode += direction;

        if(currentNode >= path.size() || currentNode < 0)
            onReachGoal(world, dt);
    }

    public void randomNextNode() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int randomNode = rand.nextInt(1, path.size());
        if(randomNode == currentNode)
            // 0 is excluded from rng, so currentNode
            // counts as 0 instead to even odds without
            // ever letting currentNode be picked.
            randomNode = 0;

        currentNode = randomNode;
    }

    public abstract void step(WorldAccessor world, double dt);
    protected abstract void onReachGoal(WorldAccessor world, double dt);

    @Override
    public void tick(WorldAccessor world, double dt) {
        if(!active.get() || path.isEmpty()) return;

        step(world, dt);

        if(getCurrentNode().sub(pos.get()).magSq() <= distanceMargin * distanceMargin)
            nextNode(world, dt);
    }
}
