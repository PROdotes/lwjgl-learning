package org.example;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}\n";
    private int vertexID, fragmentID, shaderProgramID;
    private float[] vertexArray = {
             0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,
            -0.5f,  0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,
             0.5f,  0.5f, 0.0f,     1.0f, 0.0f, 1.0f, 1.0f,
            -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f
    };
    private int[] elementArray = {
            2, 1, 0,
            0, 1, 3
    };
    private int vaoID, vboID, eboID;

    public LevelEditorScene() {
        System.out.println("LevelEditorScene is initializing...");
    }

    @Override
    public void init() {
        Shader testShader = new Shader("assets/shaders/default.glsl");

        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Vertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "Vertex shader compilation failed";
        }


        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Fragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "Fragment shader compilation failed";
        }


        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Shader program linking failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "Shader program linking failed";
        }

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
        int floatSize = 4;
        int stride = (positionsSize + colorsSize) * floatSize;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, positionsSize, GL_FLOAT, false, stride, positionsSize * floatSize);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float deltaTime) {
        glUseProgram(shaderProgramID);
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);

    }
}
