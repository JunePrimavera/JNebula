package org.JNebula.ObjectEditor;

import com.google.gson.Gson;
import org.JNebula.Components.DifferentComponents.SpriteComponent;
import org.JNebula.Components.iComponent;
import org.JNebula.GameObjects.GameObject;
import org.JNebula.Tooling.SceneObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * @author Theo willis
 * @version 1.0.0
 * ~ project outline here ~
 * @Javadoc
 */
public class ObjectEditorJSON {

    private String JSONString;
    public SceneObject objects;

    public ObjectEditorJSON(String file) {
        StringBuilder t = new StringBuilder();
        try {
            System.out.println("hi");

            ArrayList<String> a = (ArrayList<String>) Files.readAllLines(Path.of(file), StandardCharsets.UTF_8);
            a.forEach(contents -> {
                t.append(contents);
                t.append("\n");
            });
        } catch (IOException e) {
            System.err.println("didnt load");
        }

        this.JSONString = t.toString();
        this.objects = JSONEditor();


        //field.setAccessible(true);
//    Object value = field.get(obj);
    }

    private SceneObject JSONEditor() {
        Gson gson = new Gson();
        ArrayList<GameObject> objectList = new ArrayList<>();
        SceneObject scene = new SceneObject();
        JSONArray jsonArray = new JSONArray(JSONString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject a = jsonArray.getJSONObject(i);

            GameObject gameObject = null;

            try {
                // Dynamically create an instance of the class
                Class<?> clazz = Class.forName("org.JNebula.GameObjects.GameObject");
                Object object = gson.fromJson(String.valueOf(a), clazz);
                gameObject = (GameObject) clazz.cast(object);
                // Access object properties
                System.out.println("object value: " + gameObject.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("the class is not found");
            }
            JSONArray components = a.getJSONArray("components");
            for (int c = 0; c < components.length(); c++) {
                String obj = components.getJSONObject(c).get("component_name").toString();
                try {
                    Class<? extends iComponent> clazz = (Class<? extends iComponent>) Class.forName(obj);
                    Object object = gson.fromJson(String.valueOf(components.getJSONObject(c)), clazz);
                    assert gameObject != null;
                    gameObject.AddComponent(clazz.cast(object));
                } catch (ClassNotFoundException e) {
                    System.out.println("class not found");
                }
            }
            scene.initObject(gameObject);

        }
        return scene;
    }
}
