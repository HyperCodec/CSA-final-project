package finalproject.game.util;

import finalproject.game.entities.character.Player;
import finalproject.game.entities.environment.Platform;
import finalproject.engine.util.Vec2;
import finalproject.game.entities.environment.Wall;
import finalproject.game.util.rendering.TextureManager;
import finalproject.game.util.rendering.TileMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Functions for parsing and
 * constructing classes out of JSON
 */
public class ParseUtils {
    // i have no idea how i managed to make
    // this all work in like my second try
    // after writing everything without testing once

    // i could probably find a way to make
    // this work with classpaths in general,
    // but i dont feel like typing all of that
    // out in JSON anyway
    private final static List<Class<?>> VALID_CLASSES = List.of(
            Player.class,
            Platform.class,
            Wall.class,
            Vec2.class,
            TextureManager.class,
            TextureManager.Environment.class,
            TileMap.class
    );

    /**
     * Maps parseable class names to classes
     * so that they can be identified in the json format.
     * Generated from VALID_CLASSES.
     */
    public final static HashMap<String, Class<?>> CLASS_MAP;

    static {
        CLASS_MAP = new HashMap<>();

        for(Class<?> classData : VALID_CLASSES) {
            CLASS_MAP.put(classData.getSimpleName(), classData);
        }
    }

    public static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAPPINGS = Map.of(
            Boolean.class, boolean.class,
            Byte.class, byte.class,
            Character.class, char.class,
            Double.class, double.class,
            Float.class, double.class,
            Integer.class, int.class,
            Long.class, long.class,
            Short.class, short.class
    );

    public static @NotNull ArrayList<Object> parseJSONArray(@NotNull JSONArray jsonArray) {
        ArrayList<Object> result = new ArrayList<>(jsonArray.length());

        for(Object obj : jsonArray)
            result.add(parseJSONValue(obj));

        return result;
    }

    public static <T> @NotNull ArrayList<T> parseJSONArray(@NotNull JSONArray jsonArray, @NotNull Class<T> clazz) {
        ArrayList<T> result = new ArrayList<>(jsonArray.length());

        for(Object obj : jsonArray)
            result.add(clazz.cast(parseJSONValue(obj)));

        return result;
    }

    // `val` can be of type Object, ArrayList, String, boolean, int, double, float, long, or null.
    // turns it from JSON result to initialized Java type.
    public static @Nullable Object parseJSONValue(Object val) {
        if(val instanceof HashMap<?, ?> map)
            return parseObject(new JSONObject(map));
        if(val instanceof JSONObject obj)
            return parseObject(obj);
        if(val instanceof JSONArray arr)
            return parseJSONArray(arr);
        if(val instanceof String str)
            return str;
        if(val instanceof Boolean bool)
            return bool;
        if(val instanceof Number num)
            return num;
        if(val.equals(JSONObject.NULL))
            return null;


        throw new IllegalArgumentException("Unparseable type: " + val.getClass().getName());
    }

    public static @NotNull Object parseObject(@NotNull JSONObject obj) {
        String type = obj.getString("type");

        if (obj.has("args")) {
            JSONArray args = obj.getJSONArray("args");

            return parseObject(type, args.toList());
        }

        String parent = obj.getString("parent");
        String staticName = obj.getString("static");

        try {
            return parseStatic(type, parent, staticName);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Invalid static value: " + obj, e);
        }
    }

    public static @NotNull Object parseObject(String type, @NotNull List<Object> args) {
        Class<?> clazz = CLASS_MAP.get(type);

        // recursively initialize args
        Object[] parsedArgs = args
                .stream()
                .map(ParseUtils::parseJSONValue)
                .toArray();

        try {
            return constructFromArgs(clazz, parsedArgs);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            // lazily make everything crash bc I don't
            // want to handle exceptions everywhere
            throw new RuntimeException(e);
        }
    }

    @Contract(pure = true)
    public static <T> @NotNull T constructFromArgs(@NotNull Class<T> tClass, Object @NotNull ... args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?>[] paramTypes = new Class<?>[args.length];

        for(int i = 0; i < args.length; i++) {
            Object arg = args[i];

            // for some reason JSON randomly
            // decides to use BigDecimal even
            // though I didn't change anything
            // in the file.
            if(arg instanceof BigDecimal big) {
                paramTypes[i] = double.class;
                args[i] = big.doubleValue();
                continue;
            }

            Class<?> clazz = arg.getClass();
            Class<?> mappedClazz = PRIMITIVE_TYPE_MAPPINGS.get(clazz);

            if(mappedClazz == null) {
                paramTypes[i] = clazz;
                continue;
            }

            paramTypes[i] = mappedClazz;
            args[i] = mappedClazz.cast(arg);
        }

        Constructor<T> constructor = tClass.getConstructor(paramTypes);
        return constructor.newInstance(args);
    }

    public static Object parseStatic(String typeName, String parentName, String staticName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> type = CLASS_MAP.get(typeName);
        Class<?> parent = CLASS_MAP.get(parentName);

        if(type == null)
            throw new IllegalArgumentException("No such class: " + typeName);
        if(parent == null)
            throw new IllegalArgumentException("No such class: " + parentName);

        return fetchStatic(type, parent, staticName);
    }

    public static <T, P> T fetchStatic(@NotNull Class<T> type, @NotNull Class<P> parent, String staticName) throws NoSuchFieldException, IllegalAccessException {
        return type.cast(parent.getField(staticName).get(null));
    }
}
