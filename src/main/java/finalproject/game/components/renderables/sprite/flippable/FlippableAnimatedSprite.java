package finalproject.game.components.renderables.sprite.flippable;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.components.renderables.sprite.AnimatedSprite;
import finalproject.game.util.physics.HorizontalDirection;
import finalproject.game.util.rendering.ImageUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class FlippableAnimatedSprite extends AnimatedSprite {
    Box<HorizontalDirection> direction;
    List<BufferedImage> flippedFrames;

    public FlippableAnimatedSprite(Box<Vec2> pos, List<BufferedImage> frames, double frameTime, Box<HorizontalDirection> direction) {
        super(pos, ImageUtils.flatCastImages(frames), frameTime);

        this.direction = direction;
        flippedFrames = frames.stream()
                .map(ImageUtils::flipHorizontal)
                .toList();
    }

    public HorizontalDirection getDirection() {
        return direction.get();
    }

    public void setDirection(HorizontalDirection direction) {
        this.direction.set(direction);
    }

    @Override
    public void renderAtPos(@NotNull Graphics g, @NotNull Vec2 pos) {
        if(direction.get() == HorizontalDirection.LEFT) {
            Image current = frames.get(currentFrame);

            int w = current.getWidth(null);
            int h = current.getHeight(null);

            Image flipped = flippedFrames.get(currentFrame);
            g.drawImage(flipped, (int) pos.getX() - w/2, (int) pos.getY() - h/2, null);

            return;
        }

        super.renderAtPos(g, pos);
    }
}
