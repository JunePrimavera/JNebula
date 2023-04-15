package org.NayaEngine.Tooling;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL4;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.io.*;


/**
 * @author Theo willis
 * @version 1.0.0
 * ~ project outline here ~
 * @Javadoc
 */
public class loadShader {

    /**
     * @return shader content
     */
    public String processShader(String fileName) throws IOException {
        String file = "src/shaders/" + fileName;
        StringBuilder t = new StringBuilder();
        try {
            ArrayList<String> a = (ArrayList<String>) Files.readAllLines(Path.of(file), StandardCharsets.UTF_8);
            a.forEach(contents -> {
                t.append(contents);
                t.append("\n");
            });
        } catch (IOException e) {
            System.err.println("didnt load");
            return "error getting file";
        }

        return t.toString();
    }

    public int shaderCOmpile(GL4 gl) {
        String vertexSource = "",
                fragSource = "";
        try {
            vertexSource = processShader("VertexSprite.glsl");
            fragSource = processShader("SpriteFrag.glsl");
        } catch (IOException ignored) {

            return 0;
        }
        return loadShaders(vertexSource, fragSource, gl);
    }

    public int loadShaders(String vertexShaderS, String fragShader, GL4 gl2) {

        //duke mwean UwU
        if (vertexShaderS.equals("error getting file") ||
                fragShader.equals("error getting file")) {
            System.out.println("didnt compile");
            return 0;
        }

        // OHIO boss :kekw:
//        if (vertexShaderS.length() == "error getting file".length() ||
//                fragShader.length() == "error getting file".length()) {
//            System.out.println("didnt compile");
//            return 0;
//        }
        int shaderProgram = gl2.glCreateProgram();
        int vertexShader = gl2.glCreateShader(GL2.GL_VERTEX_SHADER);
        gl2.glShaderSource(vertexShader, 1, new String[]{vertexShaderS}, null);
        System.out.println(vertexShader);
        gl2.glCompileShader(vertexShader);
        int[] sucess = new int[1];
//        int sucess = 0;
        gl2.glGetShaderiv(vertexShader, GL2.GL_COMPILE_STATUS, sucess, 0);
        if (sucess[0] == GL2.GL_FALSE) {
            int[] logLength = new int[1];
            gl2.glGetShaderiv(vertexShader, GL2.GL_INFO_LOG_LENGTH, logLength, 0);

            ByteBuffer infoLog = ByteBuffer.allocate(logLength[0]);
            gl2.glGetShaderInfoLog(vertexShader, logLength[0], null, infoLog);
            String logString = new String(infoLog.array(), 0, logLength[0]);
            System.err.println(logString);
        }
        gl2.glAttachShader(shaderProgram, vertexShader);

// Compile and attach the fragment shader
        int fragmentShader = gl2.glCreateShader(GL2.GL_FRAGMENT_SHADER);
        gl2.glShaderSource(fragmentShader, 1, new String[]{fragShader}, null);
        gl2.glCompileShader(fragmentShader);
        gl2.glAttachShader(shaderProgram, fragmentShader);
        sucess = new int[1];
        gl2.glGetShaderiv(fragmentShader, GL2.GL_COMPILE_STATUS, sucess, 0);

        if (sucess[0] == GL2.GL_FALSE) {
            int[] logLength = new int[1];
            gl2.glGetShaderiv(fragmentShader, GL2.GL_INFO_LOG_LENGTH, logLength, 0);

            ByteBuffer infoLog = ByteBuffer.allocate(logLength[0]);
            gl2.glGetShaderInfoLog(fragmentShader, logLength[0], null, infoLog);
            String logString = new String(infoLog.array(), 0, logLength[0]);
            System.err.println(logString);
        }
        gl2.glLinkProgram(shaderProgram);

        gl2.glUseProgram(shaderProgram);

        return shaderProgram;

    }

    public void genBuffer(int[] buffer, GL4 gl) {

        gl.glGenBuffers(buffer.length, buffer, 0);
    }

    public void bindBuffer(int[] buffer, int GL_CONSTANT, GL4 gl) {
        gl.glBindBuffer(GL_CONSTANT, buffer[0]);
    }

    public void freeBuffer(int[] buffer, int GL_CONSTANT, GL4 gl) {
        gl.glBindBuffer(GL_CONSTANT, 0);

    }

    public void sendMartices(Matrix4f matrice, GL4 gl, int matricLocation) {
        FloatBuffer matBufferM = Buffers.newDirectFloatBuffer(1024);
        matrice.get(matBufferM);
//        c.initModel(new Vector3(0,0,0)).get(matBufferM);
        gl.glUniformMatrix4fv(matricLocation, 1, false, matBufferM);
        matBufferM.clear();
    }
}
