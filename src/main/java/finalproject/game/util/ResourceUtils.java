package finalproject.game.util;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceUtils {
    public static @NotNull Path pathToResource(@NotNull Class<?> requester, String resourcePath) throws URISyntaxException {
        ClassLoader classLoader = requester.getClassLoader();
        URL url = classLoader.getResource(resourcePath);

        if(url == null)
            throw new IllegalArgumentException("resource not found: " + resourcePath);

        return Paths.get(url.toURI());
    }

    public static BufferedImage readImage(@NotNull Class<?> requester, String resourcePath) throws URISyntaxException, IOException {
        Path path = pathToResource(requester, resourcePath);
        return ImageIO.read(path.toFile());
    }

    public static @NotNull String readString(@NotNull Class<?> requester, String resourcePath) throws URISyntaxException, IOException {
        Path path = pathToResource(requester, resourcePath);
        return Files.readString(path);
    }
}
