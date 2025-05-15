package finalproject.engine;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.ecs.Tickable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Safe wrapper for Engine with things like queries.
 * Mostly used to hide `addStaticTickable` and `addStaticRenderable`
 * from components to eliminate any memory leaks caused by it.
 */
public class WorldAccessor {
    private final Engine engine;

    public WorldAccessor(Engine engine) {
        this.engine = engine;
    }

    public void addEntity(Entity entity) {
        engine.addEntity(entity);
    }

    public boolean destroyEntity(Entity entity) {
        return engine.destroyEntity(entity);
    }

    public Set<Entity> getEntities() {
        return engine.getEntities();
    }

    /**
     * O(N) check for all entities with a given tickable type
     * @param targetTickable The class of the tickable component
     * @return HashMap mapping entities to the tickable type.
     * @param <T> The type of the tickable component
     */
    public <T extends Tickable> HashMap<Entity, T> findEntitiesWithTickable(Class<T> targetTickable) {
        Set<Entity> entities = getEntities();
        HashMap<Entity, T> found = new HashMap<>();

        for(Entity entity : entities) {
            HashSet<Tickable> tickables = engine.getTickablesForEntity(entity);

            for(Tickable tickable : tickables) {
                if(targetTickable.isAssignableFrom(tickable.getClass())) {
                    // there should logically only ever
                    // be one of each component type per entity.
                    // however idk if i should collect all cases
                    // with inheritance factored in.
                    found.put(entity, targetTickable.cast(tickable));
                    break;
                }
            }
        }

        return found;
    }

    /**
     * O(N) search for all tickables of that type
     * @param targetTickable The class of the tickable component
     * @return ArrayList of all components of the tickable type.
     * @param <T> The type of the tickable component.
     */
    public <T extends Tickable> ArrayList<T> findTickables(Class<T> targetTickable) {
        ArrayList<T> found = new ArrayList<>();

        for(Tickable t : engine.tickables) {
            if(targetTickable.isAssignableFrom(t.getClass())) {
                found.add(targetTickable.cast(t));
            }
        }

        return found;
    }

    /**
     * O(N) check for all entities with a given renderable type
     * @param targetRenderable The class of the renderable component
     * @return HashMap mapping entities to the renderable type.
     * @param <T> The type of the renderable component
     */
    public <T extends Renderable> HashMap<Entity, T> findEntitiesWithRenderable(Class<T> targetRenderable) {
        Set<Entity> entities = getEntities();
        HashMap<Entity, T> found = new HashMap<>();

        for (Entity entity : entities) {
            HashSet<Renderable> renderables = engine.getRenderablesForEntity(entity);
            for (Renderable renderable : renderables) {
                if (targetRenderable.isAssignableFrom(renderable.getClass())) {
                    found.put(entity, targetRenderable.cast(renderable));
                    break;
                }
            }
        }

        return found;
    }

    /**
     * O(N) search for all renderables of that type
     * @param targetRenderable The class of the renderable component
     * @return ArrayList of all components of the renderable type.
     * @param <T> The type of the renderable component.
     */
    public <T extends Renderable> ArrayList<T> findRenderables(Class<T> targetRenderable) {
        ArrayList<T> found = new ArrayList<>();

        for(Renderable renderable : engine.renderables) {
            if(targetRenderable.isAssignableFrom(renderable.getClass())) {
                found.add(targetRenderable.cast(renderable));
            }
        }

        return found;
    }

    public void endGame() {
        engine.stopGameLoop();
    }
}
