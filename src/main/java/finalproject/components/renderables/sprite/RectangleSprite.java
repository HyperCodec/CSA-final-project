package finalproject.components.renderables.sprite;

import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class RectangleSprite extends Sprite {
    int width;
    int height;
    Color color;

    public RectangleSprite(Box<Vec2> pos, int width, int height, Color color) {
        super(pos);

        this.width = width;
        this.height = height;
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void render(@NotNull Graphics g) {
        g.setColor(color);

        Vec2 center = pos.get();

        int top = (int) center.getY() - height / 2;
        int left = (int) center.getX() - width / 2;

        g.fillRect(left, top, width, height);
    }
}
