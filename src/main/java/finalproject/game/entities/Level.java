package finalproject.game.entities;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.game.util.ParseUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Level implements Entity {
    ArrayList<Entity> entities;

    public Level(String json) {
        entities = parseEntities(json);
    }

    public Level(File file) throws FileNotFoundException {
        this(readFile(file));
    }

    private static @NotNull String readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        StringBuilder text = new StringBuilder();
        while (scanner.hasNextLine()) {
            text.append(scanner.nextLine());
        }

        scanner.close();

        return text.toString();
    }

    public static @NotNull ArrayList<Entity> parseEntities(String json) {
        return ParseUtils.parseJSONArray(new JSONArray(json), Entity.class);
    }

    @Override
    public void spawn(EntityComponentRegistry r) {
        for(Entity entity : entities)
            r.addChildEntity(entity);
    }
}
