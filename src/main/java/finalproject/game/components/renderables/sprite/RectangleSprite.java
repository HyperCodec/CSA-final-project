package finalproject.game.components.renderables.sprite;

import finalproject.game.util.Box;
import finalproject.game.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class RectangleSprite extends Sprite {
    Box<Vec2> dimensions;
    Color color;

    public RectangleSprite(Box<Vec2> pos, Box<Vec2> dimensions, Color color) {
        super(pos);

        this.dimensions = dimensions;
        this.color = color;
    }

    public Vec2 getDimensions() {
        return dimensions.get();
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
        Vec2 size = dimensions.get();

        double height = size.getY();
        double width = size.getX();

        int top = (int) (center.getY() - height / 2);
        int left = (int) (center.getX() - width / 2);

        g.fillRect(left, top, (int) width, (int) height);
    }
}
