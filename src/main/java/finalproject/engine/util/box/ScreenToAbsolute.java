package finalproject.engine.util.box;

import finalproject.engine.Camera;
import finalproject.engine.util.Vec2;

/**
 * Wraps a Box containing the screen
 * position and converts it to/from
 * absolute positioning when getting/setting.
 */
public class ScreenToAbsolute implements Box<Vec2> {
    final Camera camera;
    final Box<Vec2> inner;

    public ScreenToAbsolute(Box<Vec2> inner, Camera camera) {
        this.inner = inner;
        this.camera = camera;
    }

    public ScreenToAbsolute(Vec2 inner, Camera camera) {
        this(new BasicBox<>(inner), camera);
    }

    @Override
    public Vec2 get() {
        return camera.getAbsolutePos(inner.get());
    }

    @Override
    public void set(Vec2 val) {
        inner.set(camera.getScreenPos(val));
    }
}
