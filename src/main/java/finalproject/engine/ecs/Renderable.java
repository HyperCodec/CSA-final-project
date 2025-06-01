package finalproject.engine.ecs;

import finalproject.engine.Camera;

import java.awt.*;

public interface Renderable {
    void render(Graphics g, Camera mainCamera);

    // UI: -2, foreground: -1, general: 0, background: 1, etc.
    default int getLayer() {
        return 0;
    }
}
