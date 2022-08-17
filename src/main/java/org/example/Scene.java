package org.example;

public abstract class Scene {

    public Scene() {
        System.out.println("Scene is loading...");
    }


    public void init()  {
        System.out.println("Scene is initializing...");
    }

    public abstract void update(float deltaTime);
}
