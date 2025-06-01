package finalproject.game.components.renderables.sprite;

import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.util.Timer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class AnimatedSprite extends Sprite implements Tickable {
    int currentFrame = 0;
    final List<Image> frames;
    final Timer timer;

    public AnimatedSprite(Box<Vec2> pos, List<Image> frames, double frameTime) {
        super(pos);
        this.frames = frames;
        this.timer = new Timer(frameTime, true);
    }

    private void incFrame() {
        currentFrame = (currentFrame + 1) % frames.size();
    }

    public double getCycleDuration() {
        return timer.getDuration() * frames.size();
    }

    @Override
    public void renderAtPos(@NotNull Graphics g, @NotNull Vec2 pos) {
        Image image = frames.get(currentFrame);

        int w = image.getWidth(null);
        int h = image.getHeight(null);

        g.drawImage(image, (int) pos.getX() - w/2, (int) pos.getY() - h/2, null);
    }

    @Override
    public void tick(WorldAccessor _world, double dt) {
        if (timer.tick(dt))
            incFrame();
    }
}
