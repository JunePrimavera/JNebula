# JNebula 2D v1.0.0


the naya engine.

2D game engine coded by me TheoW03

written in java

# Installation

1. <p>download these 2 dependecies
---
*    version: 2.3.2 JOGL (openGL)
* https://jar-download.com/artifacts/org.jogamp.gluegen/gluegen-rt
---
 *   version 1.10.5 JOML (Matrix math)
 * https://jar-download.com/artifacts/org.joml

---
*    requires java 17
* https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
---
# Getting Started

main

```JAVA
import org.JNebula.GameObjects.GameRenderer;

public class Main {
    public static void main(String[] args) throws IOException {
        GameRenderer starterCode = new StarterCode(); //for step 3 if you get a not defined error then
        Window.InitWindow(640, 480, "example window", starterCode);
    }
}

```
---

your renderer class
```JAVA
public class StarterCode extends GameRenderer {
    public InitObjects initObject;
    //runs 1st frame
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        super.init(glAutoDrawable); 
        GameObject obj = new GameObject("starterObj"); // you can name it what you want
        obj.AddComponent(new CameraComponent(new Vector3(0,0,0))); //not required if you dont add it will default to 0,0
        obj.AddComponent(new SpriteComponents("sprite.png","png",null)); //the null is a color
        obj.AddComponent(new TransformComponent(new Vector3(0,0,0)));
        gameObjectArrayList.add(obj);
    }
    //runs every frame. 
    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        Init.InstantiateObjects(gameObjectArrayList); //inits list
    }
}

```


