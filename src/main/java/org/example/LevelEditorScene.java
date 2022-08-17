package org.example;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    float size = 200;
    private final float[] vertexArray = {
             size,   0f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     1,0,
               0f, size, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,     0,1,
             size, size, 0.0f,     1.0f, 0.0f, 1.0f, 1.0f,     1,1,
               0f,   0f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f,     0,0
    };
    private final int[] elementArray = {
            2, 1, 0,
            0, 1, 3
    };
    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;

    public LevelEditorScene() {
        System.out.println("LevelEditorScene is initializing...");
    }

    @Override
    public void init() {
        camera = new Camera(new Vector2f(-500,-30));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        testTexture = new Texture("assets/images/Untitled.png");


        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionsSize = 3;
        int colorsSize = 4;
        int uvSize = 2;
        int stride = (positionsSize + colorsSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, positionsSize, GL_FLOAT, false, stride, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, stride, (positionsSize + colorsSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float deltaTime) {
        defaultShader.use();

        defaultShader.uploadTexture("uSampler", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjectionMatrix", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uViewMatrix", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        defaultShader.detach();

    }
}
