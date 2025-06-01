package finalproject.game.components.renderables.sprite;

import finalproject.engine.Camera;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class Sprite implements Renderable {
    private final Box<Vec2> pos;
    int layer;

    protected Sprite(Box<Vec2> pos, int layer) {
        this.pos = pos;
        this.layer = layer;
    }

    protected Sprite(Box<Vec2> pos) {
        this(pos, 0);
    }

    public Vec2 getPos() { return pos.get(); }
    public void setPos(Vec2 pos) {
        this.pos.set(pos);
    }

    public abstract void renderAtPos(Graphics g, Vec2 pos);

    @Override
    public void render(Graphics g, @NotNull Camera mainCamera) {
        Vec2 screenPos = mainCamera.getScreenPos(pos.get());

        renderAtPos(g, screenPos);
    }

    @Override
    public int getLayer() {
        return layer;
    }
}
