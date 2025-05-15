package finalproject.components.renderables.sprite;

import finalproject.engine.util.Ref;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PointSprite extends Sprite {
    Color color;
    int radius;

    public PointSprite(Ref<Vec2> pos, Color color, int radius) {
        super(pos);

        this.color = color;
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void render(@NotNull Graphics g) {
        g.setColor(color);

        int diameter = radius * 2;

        Vec2 pos2 = pos.get();
        int cx = (int) pos2.getX() - radius;
        int cy = (int) pos2.getY() - radius;

        g.fillOval(cx, cy, diameter, diameter);
    }
}
