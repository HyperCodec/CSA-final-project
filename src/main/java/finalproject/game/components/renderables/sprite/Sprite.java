package finalproject.game.components.renderables.sprite;

import finalproject.engine.ecs.Renderable;
import finalproject.game.util.Box;
import finalproject.game.util.Vec2;

import java.awt.*;

public abstract class Sprite implements Renderable {
    protected Box<Vec2> pos;

    protected Sprite(Box<Vec2> position) {
        this.pos = position;
    }

    public Box<Vec2> getPos() { return pos; }
    public void setPos(Vec2 pos) {
        this.pos.set(pos);
    }

    // TODO make renderAtPos abstract method
    // and make default impl for this
    // once camera is implemented.
    public abstract void render(Graphics g);
}
