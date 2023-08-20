package engine.terrain;

import engine.entities.Player;
import engine.renderEngine.Loader;
import engine.textures.TerrainTexture;
import engine.textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;

public class TerrainManager {

    private List<Terrain> terrains;

    private Loader loader;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    public TerrainManager(Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap) {
        terrains = new ArrayList<>();
        this.loader = loader;
        this.texturePack = texturePack;
        this.blendMap = blendMap;
    }

    public void update(Player player) {
        Terrain terrain = player.getActiveTerrain();
        createSurroundingTerrains(terrain);
    }

    public void createTerrain(int gridX, int gridZ) {
        if(gridX < 0 || gridZ < 0) return;
        Terrain terrain = new Terrain(gridX, gridZ, loader, texturePack, blendMap);
        terrains.add(terrain);
    }

    private void createSurroundingTerrains(Terrain terrain) {
        int gridX = terrain.getGridX();
        int gridX1 = terrain.getGridX()+1;
        int gridX2 = terrain.getGridX()-1;
        int gridZ = terrain.getGridZ();
        int gridZ1 = terrain.getGridZ()+1;
        int gridZ2 = terrain.getGridZ()-1;

        //Right
        if(!terrainExists(gridX1, gridZ))
            createTerrain(gridX1, gridZ);

        //Left
        if(!terrainExists(gridX2, gridZ))
            createTerrain(gridX2, gridZ);

        //Top
        if(!terrainExists(gridX, gridZ1))
            createTerrain(gridX, gridZ1);

        //Bottom
        if(!terrainExists(gridX, gridZ2))
            createTerrain(gridX, gridZ2);

        //Top Right
        if(!terrainExists(gridX1, gridZ1))
            createTerrain(gridX1, gridZ1);

        //Top Left
        if(!terrainExists(gridX2, gridZ1))
            createTerrain(gridX2, gridZ1);

        //Bottom Right
        if(!terrainExists(gridX1, gridZ2))
            createTerrain(gridX1, gridZ2);

        //Bottom Left
        if(!terrainExists(gridX2, gridZ2))
            createTerrain(gridX2, gridZ2);


    }

    public void addTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void removeTerrain(Terrain terrain) {
        if(terrainExists(terrain))
            terrains.remove(terrain);
    }

    public boolean terrainExists(Terrain terrain) {
        for(Terrain t : terrains) {
            if(t.getGridZ() == terrain.getGridZ() && t.getGridX() == terrain.getGridX()) return true;
        }
        return false;
    }

    public boolean terrainExists(int gridX, int gridZ) {
        for(Terrain t : terrains) {
            if(t.getGridZ() == gridZ && t.getGridX() == gridX) return true;
        }
        return false;
    }

    public Terrain getTerrain(int gridX, int gridZ) {
        if(terrainExists(gridX, gridZ)) return null;
        for(Terrain t : terrains) {
            if(t.getGridZ() == gridZ && t.getGridX() == gridX) return t;
        }
        return null;
    }

    public List<Terrain> getTerrains() {
        return terrains;
    }
}
