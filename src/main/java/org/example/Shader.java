package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Shader {

    private int shaderProgramID;
    private String vertexSource, fragmentSource, filePath;

    public Shader(String filepath) {
        this.filePath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] split = source.split("(#type)( )+([a-zA-Z]+)");
            int index = source.indexOf("#type")+6;
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol)+6;
            eol = source.indexOf("#", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                this.vertexSource = split[1];
            } else if (firstPattern.equals("fragment")) {
                this.fragmentSource = split[1];
            } else {
                throw new Exception("Invalid shader type: " + firstPattern);
            }

            if (secondPattern.equals("vertex")) {
                this.vertexSource = split[2];
            } else if (secondPattern.equals("fragment")) {
                this.fragmentSource = split[2];
            } else {
                throw new Exception("Invalid shader type: " + secondPattern);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compile() {

    }

    public void use() {

    }

    public void detach() {

    }
}
