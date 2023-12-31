package engine.toolbox;

import engine.entities.Camera;
import engine.input.Mouse;
import engine.renderEngine.WindowManager;
import engine.terrain.Terrain;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MousePicker {

    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600;


    private Vector3f currentRay;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    private Terrain terrain;
    private Vector3f currentTerrainPoint;

    public MousePicker(Camera camera, Matrix4f projectionMatrix, Terrain terrain) {
        this.projectionMatrix = projectionMatrix;
        this.camera = camera;
        this.viewMatrix = MathTools.createViewMatrix(camera);
        this.terrain = terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public Vector3f getCurrentRay() {
        return this.currentRay;
    }

    public void update(Terrain terrain) {
        this.terrain = terrain;
        viewMatrix = MathTools.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }
    }

    private Vector3f calculateMouseRay() {
        float mouseX = Mouse.getMousePositionX();
        float mouseY = Mouse.getMousePositionY();
        Vector2f normaizedCoords = getNormalizedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normaizedCoords.x, normaizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldCoords = toWorldCoords(eyeCoords);
        return worldCoords;
    }

    private Vector2f getNormalizedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2.0f * mouseX) / WindowManager.getCurrentWidth() - 1f;
        float y = (2.0f * mouseY) / WindowManager.getCurrentHeight() - 1f;
        y = -y;
        return new Vector2f(x, y);
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f newMatrix = new Matrix4f();
        try {
            newMatrix = (Matrix4f) projectionMatrix.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Matrix4f invertedProjection = newMatrix.invert();
        Vector4f eyeCoords = invertedProjection.transform(clipCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = viewMatrix.invert();
        Vector4f rayWorld = invertedView.transform(eyeCoords);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getTransform().getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return start.add(scaledRay);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.x, endPoint.z);
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getTerrain(testPoint.x, testPoint.z);
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.x, testPoint.z);
        }
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }

    private Terrain getTerrain(float worldX, float worldZ) {
        return terrain;
    }



}
