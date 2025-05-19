package finalproject.components.renderables.sprite;

import finalproject.engine.WorldAccessor;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.util.Ref;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class AnimatedSprite extends Sprite implements Tickable {
    List<Image> frames;
    int currentFrame = 0;
    final double frameTime;
    double currentElapsed = 0;

    public AnimatedSprite(Ref<Vec2> pos, List<Image> frames, double frameTime) {
        super(pos);
        this.frames = frames;
        this.frameTime = frameTime;
    }

    void incFrame() {
        currentFrame = (currentFrame + 1) % frames.size();
        currentElapsed = 0;
    }

    @Override
    public void render(@NotNull Graphics g) {
        Image img = frames.get(currentFrame);
        g.drawImage(img, (int) pos.get().getX(), (int) pos.get().getY(), null);
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        currentElapsed += dt;

        if (currentElapsed >= frameTime)
            incFrame();
    }
}
