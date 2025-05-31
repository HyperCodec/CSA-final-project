package finalproject.game.components.renderables.sprite;

import finalproject.game.util.Box;
import finalproject.game.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ImageSprite extends Sprite {
    Image image;

    public ImageSprite(Box<Vec2> pos, Image image) {
        super(pos);
        this.image = image;
    }

    @Override
    public void render(@NotNull Graphics g) {
        g.drawImage(image, (int) pos.get().getX(), (int) pos.get().getY(), null);
    }
}
