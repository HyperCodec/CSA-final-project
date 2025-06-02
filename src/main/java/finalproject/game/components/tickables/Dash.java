package finalproject.game.components.tickables;

import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.util.Timer;
import finalproject.game.util.physics.HorizontalDirection;

public class Dash implements Tickable {
    Box<Vec2> pos;
    Box<HorizontalDirection> direction;
    Box<Boolean> actionable;
    final Timer cooldown;
    final Timer duration;
    final double speed;
    boolean active = false;

    public Dash(Box<Vec2> pos, Box<HorizontalDirection> direction, Box<Boolean> actionable, double cooldown, double duration, double speed) {
        this.pos = pos;
        this.direction = direction;
        this.actionable = actionable;
        this.cooldown = new Timer(cooldown);
        this.duration = new Timer(duration);
        this.speed = speed;
    }

    public synchronized boolean activate() {
        if(!cooldown.isFinished() || active) return false;
        System.out.println("dash activated");
        active = true;
        actionable.set(false);
        duration.reset();
        return true;
    }

    private void endDash() {
        System.out.println("dash ended");
        active = false;
        cooldown.reset();
        actionable.set(true);
    }

    @Override
    public void tick(WorldAccessor world, double dt) {
        if(active) {
            if(duration.tick(dt)) {
                endDash();
                return;
            }
            pos.set(pos.get().add(direction.get().toVector().mul(speed * dt)));
            return;
        }

        cooldown.tick(dt);
    }
}
