package finalproject.game.entities;

import finalproject.engine.Camera;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.game.entities.character.Player;
import finalproject.game.util.ParseUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Scene implements Entity {
    ArrayList<Entity> entities;
    String json;

    public Scene(String json) {
        entities = parseEntities(json);
        this.json = json;
    }

    public Scene(Path path) throws IOException {
        this(Files.readString(path));
    }

    public static @NotNull ArrayList<Entity> parseEntities(String json) {
        return ParseUtils.parseJSONArray(new JSONArray(json), Entity.class);
    }

    @Override
    public void spawn(EntityComponentRegistry r) {
        Player player = null;
        for(Entity entity : entities) {
            if(entity instanceof Player p)
                player = p;
            r.addChildEntity(entity);
        }

        // track player with camera
        if(player != null) {
            r.setMainCamera(r.getWorldAccessor().makeCamera(player.pos));
        }
    }

    public void reload(@NotNull WorldAccessor world) {
        world.destroyEntity(this);
        entities = parseEntities(json);
        world.addEntity(this);
    }
}
