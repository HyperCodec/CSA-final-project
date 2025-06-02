package finalproject.game.components.renderables;

import finalproject.engine.Camera;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class PointCollection implements Renderable {
    // TODO maybe an IVec2 type or something.
    public ArrayList<Vec2> points = new ArrayList<>();

    Color color;
    int radius;

    public PointCollection(Color color, int radius) {
        this.color = color;
        this.radius = radius;
    }

    @Override
    public void render(@NotNull Graphics g, Camera mainCamera) {
        g.setColor(color);
        int diameter = radius * 2;
        for(Vec2 point : points) {
            Vec2 center = mainCamera.getScreenPos(point.sub(radius));

            g.fillOval((int) center.getX(), (int) center.getY(), diameter, diameter);
        }
    }
}
