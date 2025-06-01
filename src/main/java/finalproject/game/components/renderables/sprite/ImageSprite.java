package finalproject.game.components.renderables.sprite;

import finalproject.game.util.Box;
import finalproject.engine.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ImageSprite extends Sprite {
    Image image;

    public ImageSprite(Box<Vec2> pos, Image image) {
        super(pos);
        this.image = image;
    }

    @Override
    public void renderAtPos(@NotNull Graphics g, @NotNull Vec2 pos) {
        g.drawImage(image, (int) pos.getX(), (int) pos.getY(), null);
    }
}
