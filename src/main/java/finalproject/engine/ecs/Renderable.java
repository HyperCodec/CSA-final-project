package finalproject.engine.ecs;

import finalproject.engine.Camera;

import java.awt.*;

public interface Renderable {
    void render(Graphics g, Camera mainCamera);
}
