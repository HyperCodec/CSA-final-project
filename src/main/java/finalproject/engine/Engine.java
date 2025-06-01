package finalproject.engine;

import finalproject.engine.ecs.*;
import finalproject.game.util.Box;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Engine extends JPanel {
    public final static int WIDTH = 800;
    public final static int HEIGHT = 600;
    public final static Vec2 SCREEN_DIMENSIONS = new Vec2(WIDTH, HEIGHT);

    final static Color BACKGROUND_COLOR = Color.WHITE;
    final static long FPS = 128;

    final static long FRAME_DELAY = 1000 / FPS;

    final HashMap<Entity, EntityComponentRegistry> components = new HashMap<>();
    final HashSet<Tickable> tickables = new HashSet<>();
    final HashSet<Renderable> renderables = new HashSet<>();
    boolean running = false;
    Camera mainCamera = new Camera(new Box<>(Vec2.ZERO));

    TimeManager time = new TimeManager();
    WorldAccessor access = new WorldAccessor(this);
    KeybindManager keybinds = new KeybindManager(this);

    public Engine() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(BACKGROUND_COLOR);
        requestFocusInWindow();
    }

    @Override
    public synchronized void paint(@NotNull Graphics g) {
        // clear previous frame
        super.paint(g);

        for(Renderable renderable : renderables)
            renderable.render(g, mainCamera);
    }

    public synchronized void step() {
        double dt = time.deltaSecs();

        for(Tickable t : tickables)
            t.tick(access, dt);

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

    public KeybindManager getKeybindManager() {
        return keybinds;
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
}
