package finalproject.game.components.renderables.sprite.flippable;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.components.renderables.sprite.ImageSprite;
import finalproject.game.util.physics.HorizontalDirection;
import finalproject.game.util.rendering.ImageUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FlippableImageSprite extends ImageSprite {
    Box<HorizontalDirection> direction;
    Image flippedImage;

    // right = normal, left = flipped
    public FlippableImageSprite(Box<Vec2> pos, BufferedImage image, Box<HorizontalDirection> direction) {
        super(pos, image);

        flippedImage = ImageUtils.flipHorizontal(image);
        this.direction = direction;
    }

    public HorizontalDirection getDirection() {
        return direction.get();
    }

    public void setDirection(HorizontalDirection direction) {
        this.direction.set(direction);
    }

    @Override
    public void renderAtPos(@NotNull Graphics g, @NotNull Vec2 pos) {
        if (getDirection() == HorizontalDirection.LEFT) {
            int w = image.getWidth(null);
            int h = image.getHeight(null);

            g.drawImage(flippedImage, (int) pos.getX() - w/2, (int) pos.getY() - h/2, null);

            return;
        }

        super.renderAtPos(g, pos);
    }
}
