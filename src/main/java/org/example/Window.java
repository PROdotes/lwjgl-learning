package org.example;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window {
    private final int width ,height;
    private final String title;
    public float r,g,b,a;
    private long glWindow;
    private static Window window = null;
    private static Scene currentScene = null;
    private boolean vSync = true;

    private Window() {
        width = 800;
        height = 600;
        title = "Window";
        r = g = b = a = 0.1f;
    }

    public static void changeScene(int scene) {
        switch (scene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                //currentScene.init();
                break;
            default:
                assert false : "Invalid scene " + scene;
                break;
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void start() {
        System.out.println("Window is starting...");

        init();
        loop();

        glfwFreeCallbacks(glWindow);
        glfwDestroyWindow(glWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        System.out.println("Window is initializing...");

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        glWindow = glfwCreateWindow(width, height, title, 0, 0);
        if (glWindow == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetCursorPosCallback(glWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glWindow);

        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop() {
        System.out.println("Window is looping...");
        float beginTime = Time.getTime(),
                endTime = beginTime,
                totalTime = 0f,
                fpsUpdate = 0.5f,
                deltaTime = 0f;
        int frames = 0;
        while (!glfwWindowShouldClose(glWindow)) {
            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            currentScene.update(deltaTime);

            if (KeyListener.get().isKeyPressed(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(glWindow, true);
            }
            if (KeyListener.get().isKeyPressed(GLFW_KEY_V)) {
                if (vSync) {
                    glfwSwapInterval(0);
                    vSync = false;
                } else {
                    glfwSwapInterval(1);
                    vSync = true;
                }
                System.out.println("vSync is " + vSync);
            }

            glfwSwapBuffers(glWindow);

            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;


            //show fps
            frames++;
            totalTime += deltaTime;
            if (totalTime >= fpsUpdate) {
                totalTime -= fpsUpdate;
                System.out.println("FPS: " + frames/fpsUpdate);
                frames = 0;
            }
        }
    }
}
