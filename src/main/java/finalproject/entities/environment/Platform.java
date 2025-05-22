package finalproject.entities.environment;

import finalproject.components.renderables.sprite.RectangleSprite;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Platform implements Entity {
    Box<Vec2> pos;

    double width;

    public Platform(Vec2 pos, double width) {
        this.pos = new Box<>(pos);
        this.width = width;
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addRenderable(new RectangleSprite(pos, (int) width, 5, Color.BLUE));
    }
}
