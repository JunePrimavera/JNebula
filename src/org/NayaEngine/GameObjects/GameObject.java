package org.NayaEngine.GameObjects;

import com.jogamp.opencl.llb.CL;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL4;
import org.NayaEngine.Compenents.DifferentCompenents.PhysicsComponent;
import org.NayaEngine.Compenents.DifferentCompenents.SpriteComponents;
import org.NayaEngine.Compenents.ManageCmponent;
import org.NayaEngine.Compenents.DifferentCompenents.TransformComponent;
import org.NayaEngine.Compenents.iComponent;
import org.NayaEngine.Tooling.loadShader;
import org.NayaEngine.math.Vector3;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;


/**
 * @author Theo willis
 * @version 1.0.0
 * ~ project outline here ~
 * @Javadoc
 */
public class GameObject {
    public int[] indices;

    public ArrayList<iComponent> compenets;
    public String name;
    public TransformComponent transform;

    public boolean isActive;

    public static GL4 gl;

    public GameObject(String name) {
        isActive = true;
        compenets = new ArrayList<>();

        this.name = name;
    }

    public <T extends iComponent> T GetCompenent(Class<T> compenet) {
        for (iComponent c : compenets) {
            if (compenet.isAssignableFrom(c.getClass())) {
                return compenet.cast(c);
            }
        }
        return null;
    }

    public <T extends iComponent> T RemoveCompenent(Class<T> compenet) {
        for (int i = 0; i < compenets.size(); i++) {
            if (compenet.isAssignableFrom(compenets.get(i).getClass())) {
                return compenet.cast(compenets.remove(i));
            }

        }
        return null;
    }


    public void AddCompenent(iComponent compenet) {
        if(compenet instanceof TransformComponent){
            transform = (TransformComponent) compenet;
        }
        compenet.gameObject = this;
        compenets.add(compenet);

    }

    public void update(float dt, GL4 gl) {
        if (isActive) {
            loadShader sh = new loadShader();
//        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // Clear the color buffer to the clear color
            indices = new int[3];
            int shP = sh.shaderCOmpile(gl);
            for (int i = 0; i < compenets.size(); i++) {
                compenets.get(i).update(dt);
                compenets.get(i).sendtoGPU(shP, sh);
                if(GetCompenent(SpriteComponents.class) != null){
                    System.out.println("update: "+this.name);
                    indices = GetCompenent(SpriteComponents.class).indices;
                    int[] buffers = new int[1];
                    gl.glGenBuffers(1, buffers, 0);
//                    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL_LINES);
                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[0]);
                    gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 6L, IntBuffer.wrap(indices), GL_STATIC_DRAW);
                    gl.glDrawElements(GL_TRIANGLE_STRIP, 12, GL_UNSIGNED_INT, 0); //learn to make dynamic
//                    if(i == 2){
//                        gl.glDrawElements(GL_TRIANGLE_STRIP, 12, GL_UNSIGNED_INT, 0); //learn to make dynamic
//
//                    }else {
//                        gl.glDrawElements(GL_LINE_LOOP, 12, GL_UNSIGNED_INT, 0); //learn to make dynamic
//
//                    }
//                    gl.glDrawElements(GL_TRIANGLE_STRIP, 12, GL_UNSIGNED_INT, 0); //learn to make dynamic
//                    gl.glDrawElements(GL_LINE_LOOP, 3, GL_UNSIGNED_INT, 0);
                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
//                    gl.glPolygonMode(GL_BACK, GL_LINES);
                }
//                if (compenets.get(i) instanceof SpriteComponents) {
//                    indices = ((SpriteComponents) compenets.get(i)).indices;
//                    int[] buffers = new int[1];
//                    gl.glGenBuffers(1, buffers, 0);
//                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[0]);
//                    gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 4L, IntBuffer.wrap(indices), GL_STATIC_DRAW);
//                }
                if (compenets.get(i) instanceof PhysicsComponent) {
                    GetCompenent(TransformComponent.class).location = ((PhysicsComponent) compenets.get(i)).vectorPosition;
                }


            }
            if(GetCompenent(SpriteComponents.class) != null){
                System.out.println("render");


            }
//            gl.glDrawElements(GL_TRIANGLE_STRIP, 6, GL_UNSIGNED_INT, 0);
//
//            gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
//            gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }

    }

    public void start(float dt, GL4 gl) {
        if (isActive) {
            loadShader sh = new loadShader();
            int shP = sh.shaderCOmpile(gl);
            indices = new int[3];
            for (int i = 0; i < compenets.size(); i++) {
                compenets.get(i).init(dt);
                compenets.get(i).sendtoGPU(shP, sh);
                if(GetCompenent(SpriteComponents.class) != null){
                    indices = GetCompenent(SpriteComponents.class).indices;
                    int[] buffers = new int[1];
                    gl.glGenBuffers(1, buffers, 0);
                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[0]);
                    gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 4L, IntBuffer.wrap(indices), GL_STATIC_DRAW);
                }
//                if (compenets.get(i) instanceof SpriteComponents) {
//                    indices = ((SpriteComponents) compenets.get(i)).indices;
//                    int[] buffers = new int[1];
//                    gl.glGenBuffers(1, buffers, 0);
//                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[0]);
//                    gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * 4L, IntBuffer.wrap(indices), GL_STATIC_DRAW);
//                }
            if (compenets.get(i) instanceof PhysicsComponent) {
                GetCompenent(TransformComponent.class).location = ((PhysicsComponent) compenets.get(i)).vectorPosition;
            }


            }
//            if(GetCompenent(SpriteComponents.class) != null){
//                gl.glDrawElements(GL_TRIANGLE_STRIP, 6, GL_UNSIGNED_INT, 0);
//                gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
//                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
//            }


        }

    }


    public void GetCompenentList(String name, iComponent compenet) {

    }


    public ArrayList<String> getList() {
        return null;
    }
}
