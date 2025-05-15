package finalproject.engine.ecs;

import finalproject.engine.Engine;
import finalproject.engine.WorldAccessor;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;

/**
 * Safe wrapper around the engine that tracks created
 * children and components of an entity.
 */
public class EntityComponentRegistry {
    private final Engine engine;

    HashSet<Renderable> renderables = new HashSet<>();
    HashSet<Tickable> tickables = new HashSet<>();
    HashSet<Entity> children = new HashSet<>();
    Entity target;

    public EntityComponentRegistry(Engine engine, Entity target) {
        this.engine = engine;
        this.target = target;
    }

    public void addTickable(Tickable tickable) {
        tickables.add(tickable);
        engine.addStaticTickable(tickable);
    }

    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
        engine.addStaticRenderable(renderable);
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

    public void addKeyListener(KeyListener keyListener) {
        engine.addKeyListener(keyListener);
    }

    public void addMouseListener(MouseListener mouseListener) {
        engine.addMouseListener(mouseListener);
    }

    public void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        engine.addMouseMotionListener(mouseMotionListener);
    }

    public WorldAccessor getWorldAccessor() {
        return engine.getWorldAccessor();
    }
}
