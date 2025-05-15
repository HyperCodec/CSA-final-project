package finalproject.components.renderables;

import finalproject.engine.ecs.Renderable;
import finalproject.engine.util.Ref;
import finalproject.engine.util.Vec2;

import java.awt.*;

public abstract class Sprite implements Renderable {
    protected Ref<Vec2> position;

    protected Sprite(Ref<Vec2> position) {
        this.position = position;
    }

    public abstract void render(Graphics g);
}
