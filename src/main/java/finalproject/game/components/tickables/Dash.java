package finalproject.game.components.tickables;

import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.util.Timer;
import finalproject.game.util.physics.HorizontalDirection;

public class Dash implements Tickable {
    final Box<Vec2> pos;
    final Box<Vec2> vel;
    final Box<HorizontalDirection> direction;
    final Box<Boolean> actionable;
    final Timer cooldown;
    final Timer duration;
    final double speed;
    boolean active = false;

    public Dash(Box<Vec2> pos, Box<Vec2> vel, Box<HorizontalDirection> direction, Box<Boolean> actionable, double cooldown, double duration, double speed) {
        this.pos = pos;
        this.vel = vel;
        this.direction = direction;
        this.actionable = actionable;
        this.cooldown = new Timer(cooldown);
        this.duration = new Timer(duration);
        this.speed = speed;
    }

    public synchronized boolean activate() {
        if(!cooldown.isFinished() || active) return false;
        active = true;
        actionable.set(false);
        duration.reset();
        return true;
    }

    private void endDash() {
        active = false;
        cooldown.reset();
        actionable.set(true);
        vel.set(Vec2.ZERO);
    }

    public void abruptlyCancel() {
        if(!active) return;
        active = false;
        cooldown.reset();
    }

    public Timer getCooldownTimer() {
        return cooldown;
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
