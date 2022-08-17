package org.example;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener keyListener;
    private boolean[] keyPressed = new boolean[GLFW_KEY_LAST + 1];

    private KeyListener() {
        System.out.println("KeyListener is initializing...");
    }

    public static KeyListener get() {
        if (keyListener == null) {
            keyListener = new KeyListener();
        }
        return keyListener;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_UNKNOWN) {
            return;
        }
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public boolean isKeyPressed(int key) {
        return get().keyPressed[key];
    }
}
