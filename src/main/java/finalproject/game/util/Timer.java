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

    public boolean tick(double dt) {
        elapsed += dt;

        if(isFinished()) {
            if(repeat)
                elapsed -= duration;

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

    public double getElapsed() {
        return elapsed;
    }

    public double getDuration() {
        return duration;
    }
}
