package engine.components;

import engine.entities.Entity;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.objConverter.ModelData;
import engine.objConverter.OBJFileLoader;
import engine.renderEngine.Loader;
import engine.textures.ModelTexture;

public class GameObject {

    public static Entity instantiate(Cache cache, String objectName, String textureName, Transform transform) {
        TexturedModel playerModel = new TexturedModel(cache.getRawModelByName("bunny"), cache.getTextureByName("white"));
        ModelTexture texture = playerModel.getTexture();
        texture.setReflectivity(10f);
        texture.setShineDamper(1f);
        Entity entity = new Entity(playerModel);
        entity.getTransform().setTransform(transform);
        return entity;
    }
}
