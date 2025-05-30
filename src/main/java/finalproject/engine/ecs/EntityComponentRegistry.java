package finalproject.engine.ecs;

import finalproject.engine.Engine;

import javax.swing.*;
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
    HashSet<Object> markers = new HashSet<>();
    Entity target;

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

    // probably a bad idea to register these
    // to the actual panel, should use custom event
    // registry instead with only one listener on that API.
    // however for this project i'm only ever really using it
    // for persistent objects anyway.
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

    public void addKeybind(KeyStroke keyStroke, Object ident, Action action) {
        engine.getInputMap().put(keyStroke, ident);
        engine.getActionMap().put(ident, action);
    }

    public void rebindKey(KeyStroke oldKey, KeyStroke newKey) {
        InputMap inputMap = engine.getInputMap();

        Object ident = inputMap.get(oldKey);
        inputMap.remove(oldKey);
        engine.getInputMap().put(newKey, ident);
    }

    public Object removeKeybind(KeyStroke keyStroke) {
        InputMap inputMap = engine.getInputMap();

        Object ident = inputMap.get(keyStroke);
        inputMap.remove(keyStroke);

        return ident;
    }
}
