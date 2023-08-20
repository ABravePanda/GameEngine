package engine.renderEngine;

import engine.models.RawModel;
import engine.toolbox.StringTools;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static RawModel loadOBJModel(String fileName, Loader loader) {
        List<String> lines = StringTools.readAllLines(fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        for(String line : lines) {
            String[] tokens = line.split("\\s+");
            switch(tokens[0]) {
                case "v":
                    Vector3f verticesVec = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                    vertices.add(verticesVec);
                    break;
                case "vt":
                    Vector2f texturesVec = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                    textures.add(texturesVec);
                    break;
                case "vn":
                    Vector3f normalsVec = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                    normals.add(normalsVec);
                    break;
                case "f":
                    processFace(tokens[1], faces);
                    processFace(tokens[2], faces);
                    processFace(tokens[3], faces);
                    break;
                default:
                    break;
            }
        }

        List<Integer> indices = new ArrayList<>();

        float[] verticesArray = new float[vertices.size() * 3];
        int i = 0;

        for(Vector3f pos : vertices) {
            verticesArray[i * 3] = pos.x;
            verticesArray[i * 3 + 1] = pos.y;
            verticesArray[i * 3 + 2] = pos.z;
            i++;
        }

        float[] textureArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];

        for(Vector3i face : faces) {
            processVertex(face.x, face.y, face.z, textures, normals, indices, textureArray, normalsArray);
        }

        int[] indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();

        return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
    }

    private static void processVertex(int pos, int texCoord, int normal, List<Vector2f> texCoordList, List<Vector3f> normalList, List<Integer> indicesList, float[] texCoordArray, float[] normalArray) {
        indicesList.add(pos);

        if(texCoord >= 0) {
            Vector2f texCoordVec = texCoordList.get(texCoord);
            texCoordArray[pos * 2] = texCoordVec.x;
            texCoordArray[pos * 2 + 1] = 1 - texCoordVec.y;
        }

        if(normal >= 0) {
            Vector3f normalVec = normalList.get(normal);
            normalArray[pos * 3] = normalVec.x;
            normalArray[pos * 3 + 1] = normalVec.y;
            normalArray[pos * 3 + 2] = normalVec.z;
        }
    }

    private static void processFace(String token, List<Vector3i> faces) {
        String[] lineToken = token.split("/");
        int length = lineToken.length;
        int pos = -1, coords = -1, normal = -1;
        pos = Integer.parseInt(lineToken[0]) - 1;

        if(length > 1) {
            String textureCoord = lineToken[1];
            coords = textureCoord.length() > 0 ? Integer.parseInt(textureCoord) - 1 : -1;
            if(length > 2)
                normal = Integer.parseInt(lineToken[2]) - 1;
        }
        Vector3i facesVec = new Vector3i(pos, coords, normal);
        faces.add(facesVec);
    }
//    public static RawModel loadObjModel(String fileName, Loader loader) {
//        FileReader fr = null;
//        try {
//            fr = new FileReader(new File("src/main/resources/objs/" + fileName +".obj"));
//        } catch (FileNotFoundException e) {
//            System.err.println("Couldn't load file!");
//            e.printStackTrace();
//        }
//        BufferedReader reader = new BufferedReader(fr);
//        String line;
//        List<Vector3f> vertices = new ArrayList<Vector3f>();
//        List<Vector2f> textures = new ArrayList<Vector2f>();
//        List<Vector3f> normals = new ArrayList<Vector3f>();
//        List<Integer> indices = new ArrayList<Integer>();
//        float[] verticesArray = null;
//        float[] normalsArray = null;
//        float[] textureArray = null;
//        int[] indicesArray = null;
//        try {
//
//            while(true) {
//                line = reader.readLine();
//                String[] currentLine = line.split(" ");
//                if (line.startsWith("v ")) {
//                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
//                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
//                    vertices.add(vertex);
//                } else if (line.startsWith("vt ")) {
//                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
//                            Float.parseFloat(currentLine[2]));
//                    textures.add(texture);
//                } else if (line.startsWith("vn ")) {
//                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
//                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
//                    normals.add(normal);
//                } else if (line.startsWith("f ")) {
//                    textureArray = new float[vertices.size()*2];
//                    normalsArray = new float[vertices.size()*3];
//                    break;
//                }
//            }
//
//            while(line!=null) {
//                if(!line.startsWith("f ")) {
//                    line = reader.readLine();
//                    continue;
//                }
//                String[] currentLine = line.split(" ");
//                String[] vertex1 = currentLine[1].split("/");
//                String[] vertex2 = currentLine[2].split("/");
//                String[] vertex3 = currentLine[3].split("/");
//
//                processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
//                processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
//                processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
//                line = reader.readLine();
//            }
//            reader.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        verticesArray = new float[vertices.size()*3];
//        indicesArray = new int[indices.size()];
//
//        int vertexPointer = 0;
//        for (Vector3f vertex:vertices) {
//            verticesArray[vertexPointer++] = vertex.x;
//            verticesArray[vertexPointer++] = vertex.y;
//            verticesArray[vertexPointer++] = vertex.z;
//        }
//
//        for(int i=0;i<indices.size();i++) {
//            indicesArray[i] = indices.get(i);
//        }
//        return loader.loadToVAO(verticesArray, textureArray, indicesArray);
//
//    }
//    private static void processVertex(String[] vertexData, List<Integer> indices,
//                                      List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
//                                      float[] normalsArray) {
//        int currentVertexPointer = Integer.parseInt(vertexData[0])-1;
//        indices.add(currentVertexPointer);
//        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
//        textureArray[currentVertexPointer*2] = currentTex.x;
//        textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
//        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
//        normalsArray[currentVertexPointer*3] = currentNorm.x;
//        normalsArray[currentVertexPointer*3+1] = currentNorm.y;
//        normalsArray[currentVertexPointer*3+2] = currentNorm.z;
//    }
}
