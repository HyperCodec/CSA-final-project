package finalproject.game.components.renderables.sprite;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ImageSprite extends Sprite {
    protected Image image;

    public ImageSprite(Box<Vec2> pos, Image image) {
        super(pos);
        this.image = image;
    }

    @Override
    public void renderAtPos(@NotNull Graphics g, @NotNull Vec2 pos) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        g.drawImage(image, (int) pos.getX() - w/2, (int) pos.getY() - h/2, null);
    }
}
