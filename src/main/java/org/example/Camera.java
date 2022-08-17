package org.example;


import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;


public class Camera {

    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;


    public Camera(Vector2f position) {
        this.position = position;
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        adjustProjectionMatrix();
    }

    public void adjustProjectionMatrix() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0,32*40,0,32*21,0,100);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0f,0f,-1f);
        Vector3f cameraUp = new Vector3f(0f,1f,0f);
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x,position.y,20), cameraFront.add(position.x,position.y,0), cameraUp);

        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
