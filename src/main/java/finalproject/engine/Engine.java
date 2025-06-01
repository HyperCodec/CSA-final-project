package finalproject.engine;

import finalproject.engine.ecs.*;
import finalproject.engine.input.KeysManager;
import finalproject.engine.input.MouseManager;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Engine extends JPanel {
    public final static int INITIAL_WIDTH = 800;
    public final static int INTIIAL_HEIGHT = 600;

    final static Color BACKGROUND_COLOR = Color.WHITE;
    final static long FPS = 500;

    final static long FRAME_DELAY = 1000 / FPS;

    final HashMap<Entity, EntityComponentRegistry> components = new HashMap<>();
    final HashSet<Tickable> tickables = new HashSet<>();
    final HashSet<Renderable> renderables = new HashSet<>();
    boolean running = false;
    Camera mainCamera = new Camera(this, new BasicBox<>(Vec2.ZERO));

    TimeManager time = new TimeManager();
    WorldAccessor access = new WorldAccessor(this);
    KeysManager keys = new KeysManager(this);
    MouseManager mouse = new MouseManager(this);

    public Engine() {
        setPreferredSize(new Dimension(INITIAL_WIDTH, INTIIAL_HEIGHT));
        setBackground(BACKGROUND_COLOR);
        requestFocusInWindow();
    }

    @Override
    public synchronized void paint(@NotNull Graphics g) {
        // clear previous frame
        super.paint(g);

        ArrayList<Renderable> renderablesSnapshot = new ArrayList<>(renderables);

        // put lower-layered things on top
        renderablesSnapshot.sort((a, b) -> b.getLayer() - a.getLayer());

        for(Renderable renderable : renderablesSnapshot)
            renderable.render(g, mainCamera);
    }

    public synchronized void step() {
        double dt = time.deltaSecs();

        // shallow clone tickables
        // to prevent ConcurrentModificationException
        // when `t.tick()` modifies spawns/despawns entity.
        ArrayList<Tickable> tickablesSnapshot = new ArrayList<>(tickables);

        for(Tickable t : tickablesSnapshot)
            t.tick(access, dt);

        mouse.endTick();
        time.endTick();
    }

    public Thread startGameLoop() {
        if(running) throw new IllegalStateException("Game loop is already running");

        running = true;

        Thread t = new Thread(() -> {
            while (running) {
                step();
                repaint();

                try {
                    Thread.sleep(FRAME_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

        return t;
    }

    public synchronized void addEntity(Entity entity) {
        EntityComponentRegistry r = new EntityComponentRegistry(this, entity);
        components.put(entity, r);
        entity.spawn(r);
    }

    // things are "static" (i.e. persistent) if they are registered
    // without an `EntityComponentRegistry` because they will never
    // be removed.
    public synchronized void addStaticTickable(@NotNull Tickable tickable) {
        tickables.add(tickable);
    }

    public synchronized void addStaticRenderable(@NotNull Renderable renderable) {
        renderables.add(renderable);
    }

    public synchronized boolean destroyEntity(@NotNull Entity entity) {
        EntityComponentRegistry r = components.remove(entity);
        if(r == null) return false;

        tickables.removeAll(r.getTickables());
        renderables.removeAll(r.getRenderables());

        mouse.removeAllMouseListeners(r.getMouseListeners());
        mouse.removeAllMouseMotionListeners(r.getMouseMotionListeners());

        keys.removeAllSubscribers(r.getKeySubscribers());
        keys.removeAllKeyListeners(r.getKeyListeners());

        for(Entity child : r.getChildren())
            destroyEntity(child);

        return true;
    }

    public HashSet<Tickable> getTickables() {
        return tickables;
    }

    public HashSet<Renderable> getRenderables() {
        return renderables;
    }

    public Set<Entity> getEntities() {
        return components.keySet();
    }

    public synchronized HashSet<Tickable> getTickablesForEntity(@NotNull Entity entity) {
        EntityComponentRegistry r = components.get(entity);
        if(r == null) throw new IllegalArgumentException("entity not found");

        return r.getTickables();
    }

    public synchronized HashSet<Renderable> getRenderablesForEntity(@NotNull Entity entity) {
        EntityComponentRegistry r = components.get(entity);
        if(r == null) throw new IllegalArgumentException("entity not found");

        return r.getRenderables();
    }

    public synchronized HashSet<Object> getMarkersForEntity(@NotNull Entity entity) {
        EntityComponentRegistry r = components.get(entity);
        if(r == null) throw new IllegalArgumentException("entity not found");

        return r.getMarkers();
    }

    public WorldAccessor getWorldAccessor() {
        return access;
    }

    public KeysManager getKeysManager() {
        return keys;
    }

    public MouseManager getMouseManager() {
        return mouse;
    }

    public void stopGameLoop() {
        running = false;
    }

    public Camera getMainCamera() {
        return mainCamera;
    }

    public synchronized void setMainCamera(Camera camera) {
        mainCamera = camera;
    }

    public synchronized void addChildEntity(Entity parent, Entity child) {
        EntityComponentRegistry r = components.get(parent);
        r.addChildEntity(child);
    }

    public Vec2 getScreenDimensions() {
        return new Vec2(getWidth(), getHeight());
    }
}
