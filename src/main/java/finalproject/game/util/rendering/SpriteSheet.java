package finalproject.game.util.rendering;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpriteSheet {
    ArrayList<BufferedImage> images = new ArrayList<>();

    public SpriteSheet(@NotNull BufferedImage image, int rows, int cols) {
        int subw = image.getWidth() / cols;
        int subh = image.getHeight() / rows;

        // row-major order
        for(int y = 0; y <= image.getHeight() - subh; y += subh) {
            for(int x = 0; x <= image.getWidth() - subw; x += subw) {
                BufferedImage frame = image.getSubimage(x, y, subw, subh);
                images.add(frame);
            }
        }
    }

    public ArrayList<BufferedImage> getImages() {
        return images;
    }
}
