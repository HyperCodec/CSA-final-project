package finalproject.engine.util.box;

import finalproject.engine.Camera;
import finalproject.engine.util.Vec2;

/**
 * Wraps a Box containing the absolute
 * position and converts it to/from
 * screen positioning when getting/setting.
 */
public class AbsoluteToScreen extends ScreenToAbsolute {
    public AbsoluteToScreen(Vec2 inner, Camera camera) {
        super(inner, camera);
    }

    public AbsoluteToScreen(Box<Vec2> inner, Camera camera) {
        super(inner, camera);
    }

    @Override
    public Vec2 get() {
        return camera.getScreenPos(super.get());
    }

    @Override
    public void set(Vec2 val) {
        inner.set(camera.getAbsolutePos(val));
    }
}
