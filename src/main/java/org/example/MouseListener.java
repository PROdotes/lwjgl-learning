package org.example;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener mouseListener = null;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean leftButton, rightButton, middleButton;
    private boolean dragging;

    private MouseListener() {
        System.out.println("MouseListener is initializing...");
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    public static MouseListener get() {
        if (mouseListener == null) {
            mouseListener = new MouseListener();
        }
        return mouseListener;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().dragging = get().leftButton || get().rightButton || get().middleButton;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_LEFT) {
            if (action == GLFW_PRESS) {
                get().leftButton = true;
            } else if (action == GLFW_RELEASE) {
                get().leftButton = false;
                get().dragging = false;
            }
        } else if (button == GLFW_MOUSE_BUTTON_RIGHT) {
            if (action == GLFW_PRESS) {
                get().rightButton = true;
            } else if (action == GLFW_RELEASE) {
                get().rightButton = false;
                get().dragging = false;
            }
        } else if (button == GLFW_MOUSE_BUTTON_MIDDLE) {
            if (action == GLFW_PRESS) {
                get().middleButton = true;
            } else if (action == GLFW_RELEASE) {
                get().middleButton = false;
                get().dragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public float getX() {
        return (float) xPos;
    }

    public float getY() {
        return (float) yPos;
    }

    public float getDX() {
        return (float) (lastX - xPos);
    }

    public float getDY() {
        return (float) (lastY - yPos);
    }

    public float getScrollX() {
        return (float) scrollX;
    }

    public float getScrollY() {
        return (float) scrollY;
    }

    public boolean isLeftButtonPressed() {
        return leftButton;
    }

    public boolean isRightButtonPressed() {
        return rightButton;
    }

    public boolean isMiddleButtonPressed() {
        return middleButton;
    }

    public boolean isDragging() {
        return dragging;
    }
}
