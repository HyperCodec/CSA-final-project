package finalproject.game.components.renderables.ui.bar;

import finalproject.engine.Camera;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.Box;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class PercentageBar implements Renderable {
    Box<Vec2> pos;
    Box<Vec2> dimensions;
    Color backgroundColor;
    Color barColor;

    public PercentageBar(Box<Vec2> pos, Box<Vec2> dimensions, Color backgroundColor, Color barColor) {
        this.pos = pos;
        this.dimensions = dimensions;
        this.backgroundColor = backgroundColor;
        this.barColor = barColor;
    }

    public abstract double getPercentage();

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
        g.setColor(backgroundColor);
        g.fillRect(left, top, (int) w, (int) h);

        // actual percent bar
        double percentW = Math.max(0, Math.min(1, getPercentage())) * w;

        g.setColor(barColor);
        g.fillRect(left, top, (int) percentW, (int) h);
    }

    @Override
    public int getLayer() {
        return -1;
    }
}
