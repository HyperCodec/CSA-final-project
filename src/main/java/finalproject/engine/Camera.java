package finalproject.engine;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

public class Camera {
    final Engine engine;
    final Box<Vec2> pos;

    public Camera(Engine engine, Box<Vec2> pos) {
        this.engine = engine;
        this.pos = pos;
    }

    public Vec2 getScreenPos(@NotNull Vec2 absolutePos) {
        return getScreenPos(absolutePos, 1);
    }

    public Vec2 getScreenPos(@NotNull Vec2 absolutePos, double parallax) {
        return absolutePos.sub(pos.get().mul(parallax)).add(engine.getScreenDimensions().div(2));
    }

    public Vec2 getAbsolutePos(@NotNull Vec2 screenPos) {
        return getAbsolutePos(screenPos, 1);
    }

    public Vec2 getAbsolutePos(@NotNull Vec2 screenPos, double parallax) {
        return screenPos.add(pos.get().mul(parallax)).sub(engine.getScreenDimensions().div(2));
    }

    public Vec2 getScreenDimensions() {
        return engine.getScreenDimensions();
    }

    public int getWidth() {
        return engine.getWidth();
    }

    public int getHeight() {
        return engine.getHeight();
    }
}
