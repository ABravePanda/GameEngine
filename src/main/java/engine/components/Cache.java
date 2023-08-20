package engine.components;

import engine.models.RawModel;
import engine.objConverter.ModelData;
import engine.objConverter.OBJFileLoader;
import engine.renderEngine.Loader;
import engine.textures.ModelTexture;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {

    private Map<String, ModelTexture> texturesMap;
    private Map<String, RawModel> modelMap;


    private Loader loader;

    public Cache(Loader loader) {
        this.loader = loader;
        texturesMap = new HashMap<>();
        modelMap = new HashMap<>();
    }

    public void init() {
        populateTextureMap();
        populateModelMap();
    }

    public ModelTexture getTextureByName(String name) {
        return texturesMap.get(name);
    }

    public RawModel getRawModelByName(String name) {
        return modelMap.get(name);
    }

    private List<String> loadAllTextureFiles() {
        List<String> list = new ArrayList<>();
        list.add("white");
        return list;
    }

    private List<String> loadAllObjectFiles() {
        List<String> list = new ArrayList<>();
        list.add("bunny");
        list.add("dragon");
        return list;
    }

    private void populateTextureMap() {
        for(String s : loadAllTextureFiles()) {
            texturesMap.put(s, new ModelTexture(loader.loadTexture(s)));
        }
    }
    private void populateModelMap() {
        for(String s : loadAllObjectFiles()) {
            ModelData data = OBJFileLoader.loadOBJ(s);
            modelMap.put(s, loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices()));
        }
    }
}
