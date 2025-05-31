package finalproject.game.components.renderables.sprite;

import finalproject.game.util.Box;
import finalproject.game.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EllipseSprite extends Sprite {
    Color color;
    Box<Vec2> dimensions;

    public EllipseSprite(Box<Vec2> pos, Color color, Box<Vec2> dimensions) {
        super(pos);

        this.color = color;
        this.dimensions = dimensions;
    }

    @Override
    public void render(@NotNull Graphics g) {
        g.setColor(color);

        Vec2 pos2 = pos.get();
        Vec2 dimensions2 = dimensions.get();

        int top = (int) (pos2.getY() - dimensions2.getY() / 2);
        int left = (int) (pos2.getX() - dimensions2.getX() / 2);

        g.fillOval(left, top, (int) dimensions2.getX(), (int) dimensions2.getY());
    }
}
