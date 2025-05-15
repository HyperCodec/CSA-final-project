package finalproject.engine.ecs;

import finalproject.engine.Engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WorldAccessor {
    private Engine engine;

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
    public <T extends Tickable> HashMap<Entity, T> queryTickable(Class<T> targetTickable) {
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
     * O(N) check for all entities with a given renderable type
     * @param targetRenderable The class of the renderable component
     * @return HashMap mapping entities to the renderable type.
     * @param <T> The type of the renderable component
     */
    public <T extends Renderable> HashMap<Entity, T> queryRenderable(Class<T> targetRenderable) {
        Set<Entity> entities = getEntities();
        HashMap<Entity, T> found = new HashMap<>();

        for(Entity entity : entities) {
            HashSet<Renderable> renderables = engine.getRenderablesForEntity(entity);
            for(Renderable renderable : renderables) {
                if(targetRenderable.isAssignableFrom(renderable.getClass())) {
                    found.put(entity, targetRenderable.cast(renderable));
                    break;
                }
            }
        }

        return found;
    }
}
