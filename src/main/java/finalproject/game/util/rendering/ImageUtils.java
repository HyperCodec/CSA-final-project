package finalproject.game.util.rendering;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

public class ImageUtils {
    public static BufferedImage flipHorizontal(@NotNull BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    public static List<Image> flatCastImages(@NotNull Collection<BufferedImage> images) {
        return images.stream().map((image) -> (Image) image).toList();
    }

    public static BufferedImage rotateImage(@NotNull BufferedImage image, double angle) {
        AffineTransform tx = AffineTransform.getRotateInstance(angle, (double) image.getWidth() / 2, (double) image.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    public static BufferedImage scaleImage(@NotNull BufferedImage image, double scale) {
        AffineTransform tx = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }
}
