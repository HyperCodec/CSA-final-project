package finalproject.components.renderables.sprite;

import finalproject.engine.ecs.Renderable;
import finalproject.engine.util.Ref;
import finalproject.engine.util.Vec2;

import java.awt.*;

public abstract class Sprite implements Renderable {
    protected Ref<Vec2> pos;

    protected Sprite(Ref<Vec2> position) {
        this.pos = position;
    }

    public Ref<Vec2> getPos() { return pos; }
    public void setPos(Vec2 pos) {
        this.pos.set(pos);
    }

    public abstract void render(Graphics g);
}
