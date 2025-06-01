package finalproject.game.util.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileMap {
    BufferedImage image;

    public TileMap(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage tileRect(int width, int height) {
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = output.getGraphics();
        for(int x = 0; x < width; x += image.getWidth()) {
            for(int y = 0; y < width; y += image.getHeight()) {
                g.drawImage(image, x, y, null);
            }
        }

        return output;
    }
}
