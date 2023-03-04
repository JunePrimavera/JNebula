package org.NayaEngine.Compenents.DifferentCompenents;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import org.NayaEngine.Compenents.iComponent;
import org.NayaEngine.Tooling.loadShader;
import org.NayaEngine.math.Vector3;

/**
 * @author Theo willis
 * @version 1.0.0
 * ~ project outline here ~
 * @Javadoc
 */
public class PhysicsComponent extends iComponent {
    public float startingV;
    public Vector3 vectorPosition;
    private Vector3 position;

    public PhysicsComponent(GL2 gl, float startingV, Vector3 position) {
        this.gl = gl;
        this.position = position;
        this.startingV = startingV;
        vectorPosition = position;
        System.out.println("constructor value: "+vectorPosition.y);
    }

    @Override
    public String toString() {
        return "PhysicsComponent";
    }

    @Override
    public void update(float dt) {
        startingV += 0.05f;
        vectorPosition.y -= startingV;
        System.out.println("pos: "+vectorPosition.y);
        position = vectorPosition;

    }

    @Override
    public void sendtoGPU(int shaderProgram, loadShader sh) {
    }

}
