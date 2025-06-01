package finalproject.engine;

import finalproject.game.util.CardinalDirection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class KeybindManager {
    HashSet<String> heldKeys = new HashSet<>();
    HashMap<String, ArrayList<Runnable>> keyDownSubscribers = new HashMap<>();
    HashMap<String, ArrayList<Runnable>> keyUpSubscribers = new HashMap<>();
    Engine engine;

    public KeybindManager(Engine engine) {
        this.engine = engine;
    }

    public synchronized void subscribeKeyDown(String ident, Runnable onKeyDown) {
        ArrayList<Runnable> subs = keyDownSubscribers.get(ident);
        if(subs == null)
            throw new IllegalArgumentException("key not found");
        subs.add(onKeyDown);
        keyDownSubscribers.put(ident, subs);
    }

    public synchronized void subscribeKeyUp(String ident, Runnable onKeyUp) {
        ArrayList<Runnable> subs = keyUpSubscribers.get(ident);
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
        keyDownSubscribers.put(ident, new ArrayList<>());
        keyUpSubscribers.put(ident, new ArrayList<>());
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
        for(ArrayList<Runnable> subs : keyDownSubscribers.values())
            subs.remove(sub);

        for(ArrayList<Runnable> subs : keyUpSubscribers.values())
            subs.remove(sub);
    }

    private synchronized void triggerKeyDown(String ident) {
        heldKeys.add(ident);
        ArrayList<Runnable> subs = keyDownSubscribers.get(ident);

        for(Runnable sub : subs)
            sub.run();
    }

    private synchronized void triggerKeyUp(String ident) {
        heldKeys.remove(ident);

        ArrayList<Runnable> subs = keyUpSubscribers.get(ident);

        for(Runnable sub : subs)
            sub.run();
    }

    public void registerDefaultKeybinds() {
        addKeybind("right", "D");
        addKeybind("left", "A");
        addKeybind("up", "W");
        addKeybind("down", "S");
    }

    public final static Map<String, CardinalDirection> DIRECTION_MAP = Map.of(
            "right", CardinalDirection.RIGHT,
            "left", CardinalDirection.LEFT,
            "up", CardinalDirection.UP,
            "down", CardinalDirection.DOWN
    );

    public synchronized HashSet<CardinalDirection> getHeldDirections() {
        HashSet<CardinalDirection> output = new HashSet<>();

        for(Map.Entry<String, CardinalDirection> kv : DIRECTION_MAP.entrySet())
            if(heldKeys.contains(kv.getKey()))
                output.add(kv.getValue());

        return output;
    }
}
