package org.example;


import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;


public class Shader {

    private int shaderProgramID;
    private String vertexSource, fragmentSource, filePath;
    private boolean inUse;

    public Shader(String filepath) {
        this.filePath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] split = source.split("(#type)( )+([a-zA-Z]+)");
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("#", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                this.vertexSource = split[1];
            } else if (firstPattern.equals("fragment")) {
                this.fragmentSource = split[1];
            } else {
                throw new Exception("Invalid shader type: " + firstPattern + " in " + filepath);
            }

            if (secondPattern.equals("vertex")) {
                this.vertexSource = split[2];
            } else if (secondPattern.equals("fragment")) {
                this.fragmentSource = split[2];
            } else {
                throw new Exception("Invalid shader type: " + secondPattern + " in " + filepath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compile() {
        int vertexID, fragmentID;
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Vertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "Vertex shader compilation failed";
        }


        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
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
    }

    public void use() {
        if (!inUse) {
            glUseProgram(shaderProgramID);
            inUse = true;
        }

    }

    public void detach() {
        glUseProgram(0);
        inUse = false;
    }

    public void uploadMat4f(String name, Matrix4f matrix) {
        int location = glGetUniformLocation(shaderProgramID, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        glUniformMatrix4fv(location, false, buffer);
    }

    public void uploadMat3f(String name, Matrix4f matrix) {
        int location = glGetUniformLocation(shaderProgramID, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
        matrix.get(buffer);
        glUniformMatrix3fv(location, false, buffer);
    }

    public void uploadVec4f(String name, Vector4f vector) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    public void uploadVec3f(String name, Vector4f vector) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    public void uploadVec2f(String name, Vector4f vector) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform2f(location, vector.x, vector.y);
    }

    public void uploadFloat(String name, float value) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1f(location, value);
    }

    public void uploadInt(String name, int value) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1i(location, value);
    }

    public void uploadTexture(String name, int textureID) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1i(location, textureID);
    }

}