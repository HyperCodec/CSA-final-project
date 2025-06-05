package finalproject.game.util;

import finalproject.engine.ecs.WorldAccessor;
import finalproject.game.entities.Scene;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;

public class SceneUtils {

    /**
     * Removes old levels from the world and spawns the new one.
     * @param world The world accessor.
     * @param scene The new level to spawn in.
     */
    public static void loadScene(@NotNull WorldAccessor world, Scene scene) {
        world.destroyEntitiesOfType(Scene.class);
        world.addGlobalEntity(scene);
    }

    public static @NotNull Scene parseFromResources(String path) throws IOException, URISyntaxException {
        String json = ResourceUtils.readString(SceneUtils.class, path);
        return new Scene(json);
    }

    public static void loadFromResources(WorldAccessor world, String path) throws IOException, URISyntaxException {
        Scene scene = parseFromResources(path);
        loadScene(world, scene);
    }

    public static void resetScene(@NotNull WorldAccessor world) {
        world.findEntitiesOfType(Scene.class).getFirst().reload(world);
    }
}
