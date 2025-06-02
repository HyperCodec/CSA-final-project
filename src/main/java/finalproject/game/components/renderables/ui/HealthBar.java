package finalproject.game.components.renderables.ui;

import finalproject.engine.Camera;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.Damageable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HealthBar implements Renderable {
    public final Box<Vec2> pos;
    public final Box<Vec2> dimensions;
    public final Damageable health;

    public HealthBar(Box<Vec2> pos, Box<Vec2> dimensions, Damageable health) {
        this.pos = pos;
        this.dimensions = dimensions;
        this.health = health;
    }

    @Override
    public void render(@NotNull Graphics g, @NotNull Camera mainCamera) {
        Vec2 screenPos = mainCamera.getScreenPos(pos.get());
        double cx = screenPos.getX();
        double cy = screenPos.getY();

        Vec2 dimensions2 = dimensions.get();
        double w = dimensions2.getX();
        double h = dimensions2.getY();

        int left = (int)(cx - w/2);
        int top = (int)(cy - h/2);

        // background of bar
        g.setColor(Color.GRAY);
        g.fillRect(left, top, (int) w, (int) h);

        // health
        double healthW = health.getPercentHealth() * w;

        g.setColor(Color.RED);
        g.fillRect(left, top, (int) healthW, (int) h);
    }

    @Override
    public int getLayer() {
        return -1;
    }
}
