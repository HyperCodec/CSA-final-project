package finalproject.game.entities.ui;

import finalproject.engine.Camera;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.util.Timer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FpsDisplay implements Entity, Tickable, Renderable {
    double fps;
    int framesSinceLastDisplay = 0;
    final Timer timer = new Timer(1, true);
    Box<Boolean> visible = new BasicBox<>(false);

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addTickable(this);
        r.addRenderable(this);

        r.subscribeKeyDown("toggle_debug", () -> visible.set(!visible.get()));
    }

    @Override
    public void render(@NotNull Graphics g, Camera mainCamera) {
        if(!visible.get()) return;
        g.setColor(Color.BLACK);
        g.drawString(String.format("FPS: %.2f", fps), mainCamera.getWidth() - 75, 20);
    }

    @Override
    public int getLayer() {
        return -2;
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        // only update once every second to make it more readable
        framesSinceLastDisplay++;
        double timeSinceLastDisplay = timer.incTime(dt);
        if(timer.isFinished()) {
            timer.nextLoop();
            fps = getFps(timeSinceLastDisplay);
            framesSinceLastDisplay = 0;
        }
    }

    private double getFps(double elapsed) {
        return (double) framesSinceLastDisplay / elapsed;
    }
}
