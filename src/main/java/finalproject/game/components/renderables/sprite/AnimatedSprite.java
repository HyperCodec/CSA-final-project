package finalproject.game.components.renderables.sprite;

import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.ecs.Tickable;
import finalproject.game.util.Box;
import finalproject.engine.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class AnimatedSprite extends Sprite implements Tickable {
    List<Image> frames;
    int currentFrame = 0;
    final double frameTime;
    double currentElapsed = 0;

    public AnimatedSprite(Box<Vec2> pos, List<Image> frames, double frameTime) {
        super(pos);
        this.frames = frames;
        this.frameTime = frameTime;
    }

    void incFrame() {
        currentFrame = (currentFrame + 1) % frames.size();
        currentElapsed = 0;
    }

    @Override
    public void renderAtPos(@NotNull Graphics g, @NotNull Vec2 pos) {
        Image img = frames.get(currentFrame);
        g.drawImage(img, (int) pos.getX(), (int) pos.getY(), null);
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        currentElapsed += dt;

        if (currentElapsed >= frameTime)
            incFrame();
    }
}
