package finalproject.game.components.renderables.sprite.geometry;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.components.renderables.sprite.Sprite;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CircleSprite extends Sprite {
    Color color;
    int radius;

    public CircleSprite(Box<Vec2> pos, Color color, int radius) {
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
    public void renderAtPos(@NotNull Graphics g, @NotNull Vec2 pos) {
        g.setColor(color);

        int diameter = radius * 2;

        int cx = (int) pos.getX() - radius;
        int cy = (int) pos.getY() - radius;

        g.fillOval(cx, cy, diameter, diameter);
    }
}
