package finalproject.engine;

import finalproject.game.util.Box;
import org.jetbrains.annotations.NotNull;

public class Camera {
    Box<Vec2> pos;

    public Camera(Box<Vec2> pos) {
        this.pos = pos;
    }

    public Vec2 getScreenPos(@NotNull Vec2 absolutePos) {
        return absolutePos.sub(pos.get()).add(Engine.SCREEN_DIMENSIONS.divSingle(2));
    }

    public Vec2 getAbsolutePos(@NotNull Vec2 screenPos) {
        return screenPos.add(pos.get());
    }
}
