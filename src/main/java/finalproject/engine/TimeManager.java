package finalproject.engine;

// TODO maybe use better/faster/more precise time
public class TimeManager {
    long lastFrame;

    public TimeManager() {
        lastFrame = System.currentTimeMillis();
    }

    public double deltaSecs() {
        return (double)(System.currentTimeMillis() - lastFrame) / 1000;
    }

    public void endTick() {
        lastFrame = System.currentTimeMillis();
    }
}
