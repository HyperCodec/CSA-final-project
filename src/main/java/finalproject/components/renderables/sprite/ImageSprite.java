package finalproject.components.renderables.sprite;

import finalproject.engine.util.Ref;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ImageSprite extends Sprite {
    Image image;

    public ImageSprite(Ref<Vec2> pos, Image image) {
        super(pos);
        this.image = image;
    }

    @Override
    public void render(@NotNull Graphics g) {
        g.drawImage(image, (int) pos.get().getX(), (int) pos.get().getY(), null);
    }
}
