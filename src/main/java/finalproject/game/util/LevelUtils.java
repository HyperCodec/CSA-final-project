package finalproject.game.util;

import finalproject.engine.ecs.WorldAccessor;
import finalproject.game.entities.Level;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LevelUtils {
    private final static ClassLoader CLASS_LOADER = LevelUtils.class.getClassLoader();

    /**
     * Removes old levels from the world and spawns the new one.
     * @param world The world accessor.
     * @param level The new level to spawn in.
     */
    public static void loadLevel(@NotNull WorldAccessor world, Level level) {
        world.destroyEntitiesOfType(Level.class);
        world.addEntity(level);
    }

    public static @NotNull Level parseFromResources(String path) throws IOException, URISyntaxException {
        URL resource = CLASS_LOADER.getResource(path);
        if (resource == null)
            throw new FileNotFoundException(path);

        Path fullPath = Paths.get(resource.toURI());

        return new Level(fullPath);
    }

    public static void loadFromResources(WorldAccessor world, String path) throws IOException, URISyntaxException {
        Level level = parseFromResources(path);
        loadLevel(world, level);
    }
}
