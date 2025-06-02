package finalproject.engine.input;

import finalproject.engine.Engine;
import finalproject.game.util.physics.CardinalDirection;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class KeysManager implements KeyListener {
    HashSet<String> heldKeys = new HashSet<>();
    HashMap<String, HashSet<Runnable>> keyDownSubscribers = new HashMap<>();
    HashMap<String, HashSet<Runnable>> keyUpSubscribers = new HashMap<>();
    HashSet<KeyListener> keyListeners = new HashSet<>();
    Engine engine;

    public KeysManager(@NotNull Engine engine) {
        this.engine = engine;
        engine.addKeyListener(this);
    }

    public synchronized void subscribeKeyDown(String ident, Runnable onKeyDown) {
        HashSet<Runnable> subs = keyDownSubscribers.get(ident);
        if(subs == null)
            throw new IllegalArgumentException("key not found");
        subs.add(onKeyDown);
        keyDownSubscribers.put(ident, subs);
    }

    public synchronized void subscribeKeyUp(String ident, Runnable onKeyUp) {
        HashSet<Runnable> subs = keyUpSubscribers.get(ident);
        if(subs == null)
            throw new IllegalArgumentException("key not found");
        subs.add(onKeyUp);
        keyUpSubscribers.put(ident, subs);
    }

    public synchronized boolean isPressed(String ident) {
        return heldKeys.contains(ident);
    }

    public void addKeybind(String ident, String key) {
        addKeybindRaw(KeyStroke.getKeyStroke(key), ident, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerKeyDown(ident);
            }
        });
        addKeybindRaw(KeyStroke.getKeyStroke("released " + key), "released_" + ident, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerKeyUp(ident);
            }
        });

        // using putIfAbsent in case multiple
        // keys are bound to the same ident
        keyDownSubscribers.putIfAbsent(ident, new HashSet<>());
        keyUpSubscribers.putIfAbsent(ident, new HashSet<>());
    }

    private void addKeybindRaw(KeyStroke keyStroke, Object ident, Action action) {
        engine.getInputMap().put(keyStroke, ident);
        engine.getActionMap().put(ident, action);
    }

    public synchronized void rebindKey(KeyStroke oldKey, KeyStroke newKey) {
        InputMap inputMap = engine.getInputMap();

        Object ident = inputMap.get(oldKey);
        inputMap.remove(oldKey);
        engine.getInputMap().put(newKey, ident);
    }

    public synchronized Object removeKeybind(KeyStroke keyStroke) {
        InputMap inputMap = engine.getInputMap();

        Object ident = inputMap.get(keyStroke);
        inputMap.remove(keyStroke);

        // maybe remove subscribers

        return ident;
    }

    public synchronized void removeSubscriber(Runnable sub) {
        for(HashSet<Runnable> subs : keyDownSubscribers.values())
            subs.remove(sub);

        for(HashSet<Runnable> subs : keyUpSubscribers.values())
            subs.remove(sub);
    }

    public synchronized void removeAllSubscribers(@NotNull Collection<Runnable> subs) {
        for(Runnable sub : subs)
            removeSubscriber(sub);
    }

    private synchronized void triggerKeyDown(String ident) {
        heldKeys.add(ident);
        HashSet<Runnable> subs = keyDownSubscribers.get(ident);

        for(Runnable sub : subs)
            sub.run();
    }

    private synchronized void triggerKeyUp(String ident) {
        heldKeys.remove(ident);

        HashSet<Runnable> subs = keyUpSubscribers.get(ident);

        for(Runnable sub : subs)
            sub.run();
    }

    public void registerDefaultKeybinds() {
        // movement keys
        addKeybind("right", "D");
        addKeybind("right", "RIGHT");

        addKeybind("left", "A");
        addKeybind("left", "LEFT");

        addKeybind("up", "W");
        addKeybind("up", "UP");
        addKeybind("up", "SPACE");

        addKeybind("down", "S");
        addKeybind("down", "DOWN");

        addKeybind("toggle_debug", "F3");
        addKeybind("debug_colliders", "control F3");
    }

    public final static Map<String, CardinalDirection> DIRECTION_MAP = Map.of(
            "right", CardinalDirection.RIGHT,
            "left", CardinalDirection.LEFT,
            "up", CardinalDirection.UP,
            "down", CardinalDirection.DOWN
    );

    public synchronized HashSet<CardinalDirection> getHeldDirections() {
        HashSet<CardinalDirection> output = new HashSet<>();

        // could probably use CardinalDirection.values() and toString
        // instead of a map, but whatever. efficiency is about the same
        // either way.
        for(Map.Entry<String, CardinalDirection> kv : DIRECTION_MAP.entrySet())
            if(heldKeys.contains(kv.getKey()))
                output.add(kv.getValue());

        return output;
    }

    public synchronized void addKeyListener(KeyListener sub) {
        keyListeners.add(sub);
    }

    public synchronized void removeKeyListener(KeyListener sub) {
        keyListeners.remove(sub);
    }

    public synchronized void removeAllKeyListeners(Collection<KeyListener> subs) {
        keyListeners.removeAll(subs);
    }

    @Override
    public synchronized void keyTyped(KeyEvent e) {
        for(KeyListener listener : keyListeners)
            listener.keyTyped(e);
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        for(KeyListener listener : keyListeners)
            listener.keyPressed(e);
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        for(KeyListener listener : keyListeners)
            listener.keyReleased(e);
    }
}
