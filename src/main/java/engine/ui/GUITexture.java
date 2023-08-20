package engine.ui;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class GUITexture {

    private int texture;
    private Vector2f position;
    private Vector2f scale;
    private Vector3f rotation;

    public GUITexture(int texture, Vector2f position, Vector3f rotation, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector2f getScale() {
        return scale;
    }
}
