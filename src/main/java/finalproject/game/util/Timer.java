package finalproject.game.util;

public class Timer {
    double elapsed = 0;
    final double duration;
    boolean repeat;

    public Timer(double duration, boolean repeat) {
        this.duration = duration;
        this.repeat = repeat;
    }

    public Timer(double endTime) {
        this(endTime, false);
    }

    public double incTime(double dt) {
        elapsed += dt;
        return elapsed;
    }

    public boolean tick(double dt) {
        incTime(dt);

        if(isFinished()) {
            if(repeat)
                nextLoop();

            return true;
        }

        return false;
    }

    public boolean isFinished() {
        return elapsed >= duration;
    }

    public void reset() {
        elapsed = 0;
    }

    public void nextLoop() {
        elapsed = Math.max(0, elapsed - duration);
    }

    public double getElapsed() {
        return elapsed;
    }

    public double getDuration() {
        return duration;
    }
}
