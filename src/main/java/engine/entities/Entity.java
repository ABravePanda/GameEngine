package engine.entities;

import engine.components.Transform;
import engine.models.TexturedModel;
import org.joml.Vector3f;

public class Entity {

    private TexturedModel model;
    private Transform transform;
    private int textureIndex = 0;

    public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.transform = new Transform(position, rotation, scale);
    }

    public Entity(TexturedModel model) {
        this.model = model;
        transform = new Transform();
    }

    public Entity(Entity entity) {
        this.model = entity.getModel();
        transform = entity.getTransform();
    }

    public float getTextureXOffset(){
        int column = textureIndex%model.getTexture().getNumberOfRows();
        return (float)column/(float)model.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset(){
        int row = textureIndex/model.getTexture().getNumberOfRows();
        return (float)row/(float)model.getTexture().getNumberOfRows();
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
