package finalproject.engine.ecs;

import finalproject.engine.Camera;
import finalproject.engine.Engine;
import finalproject.engine.input.KeysManager;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;

/**
 * Safe wrapper around the engine that tracks created
 * children and components of an entity.
 */
public class EntityComponentRegistry {
    private final Engine engine;
    private final Entity target;

    HashSet<Renderable> renderables = new HashSet<>();
    HashSet<Tickable> tickables = new HashSet<>();
    HashSet<Entity> children = new HashSet<>();
    HashSet<Object> markers = new HashSet<>();
    HashSet<Runnable> keySubscribers = new HashSet<>();
    HashSet<MouseListener> mouseListeners = new HashSet<>();
    HashSet<MouseMotionListener> mouseMotionListeners = new HashSet<>();

    public EntityComponentRegistry(Engine engine, Entity target) {
        this.engine = engine;
        this.target = target;
    }

    public void addTickable(Tickable tickable) {
        tickables.add(tickable);
        engine.addStaticTickable(tickable);
    }

    public boolean removeTickable(Tickable tickable) {
        return tickables.remove(tickable);
    }

    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
        engine.addStaticRenderable(renderable);
    }

    public boolean removeRenderable(Renderable renderable) {
        return renderables.remove(renderable);
    }

    public void addMarker(Object marker) {
        markers.add(marker);
    }

    public boolean removeMarker(Object marker) {
        return markers.remove(marker);
    }

    /**
     * Adds an entity that will be destroyed
     * if this entity is destroyed
     * @param entity The entity to add
     */
    public void addChildEntity(Entity entity) {
        children.add(entity);
        engine.addEntity(entity);
    }

    public boolean destroySelf() {
        return engine.destroyEntity(target);
    }

    public HashSet<Renderable> getRenderables() {
        return renderables;
    }

    public HashSet<Tickable> getTickables() {
        return tickables;
    }

    public HashSet<Entity> getChildren() {
        return children;
    }

    public HashSet<Object> getMarkers() {
        return markers;
    }

    public WorldAccessor getWorldAccessor() {
        return engine.getWorldAccessor();
    }

    public void setMainCamera(Camera camera) {
        engine.setMainCamera(camera);
    }

    public void addMouseListener(MouseListener sub) {
        mouseListeners.add(sub);
        engine.getMouseManager().addMouseListener(sub);
    }

    public void addMouseMotionListener(MouseMotionListener sub) {
        mouseMotionListeners.add(sub);
        engine.getMouseManager().addMouseMotionListener(sub);
    }

    public HashSet<MouseListener> getMouseListeners() {
        return mouseListeners;
    }

    public HashSet<MouseMotionListener> getMouseMotionListeners() {
        return mouseMotionListeners;
    }

    public HashSet<Runnable> getKeySubscribers() {
        return keySubscribers;
    }

    public Entity getTarget() {
        return target;
    }
}
