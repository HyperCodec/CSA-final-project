package finalproject.game.components.renderables.sprite;

import finalproject.engine.Camera;
import finalproject.engine.ecs.Renderable;
import finalproject.game.util.Box;
import finalproject.engine.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class Sprite implements Renderable {
    private final Box<Vec2> pos;

    protected Sprite(Box<Vec2> position) {
        this.pos = position;
    }

    public Vec2 getPos() { return pos.get(); }
    public void setPos(Vec2 pos) {
        this.pos.set(pos);
    }

    // TODO make renderAtPos abstract method
    // and make default impl for this
    // once camera is implemented.
    public void render(Graphics g, @NotNull Camera mainCamera) {
        Vec2 screenPos = mainCamera.getScreenPos(pos.get());

        renderAtPos(g, screenPos);
    }

    public abstract void renderAtPos(Graphics g, Vec2 pos);
}
