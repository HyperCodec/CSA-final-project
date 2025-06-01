package finalproject.engine;

import finalproject.engine.util.Box;
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
        return absolutePos.sub(pos.get()).add(engine.getScreenDimensions().divSingle(2));
    }

    public Vec2 getAbsolutePos(@NotNull Vec2 screenPos) {
        return screenPos.add(pos.get()).sub(engine.getScreenDimensions().divSingle(2));
    }
}
