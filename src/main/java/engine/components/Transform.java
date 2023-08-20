package engine.components;

import org.joml.Vector3f;

public class Transform {

    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public Transform(Vector3f position, Vector3f rotation, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Transform() {
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);;
        this.scale = 1;
    }

    //transform
    public void setTransform(Transform transform) {
        this.position = transform.position;
        this.rotation = transform.rotation;
        this.scale = transform.scale;
    }

    //Position
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setPositionX(float x) {
        position.x = x;
    }

    public void setPositionY(float y) {
        position.y = y;
    }

    public void setPositionZ(float z) {
        position.z = z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPositionX() {
        return position.x;
    }

    public float getPositionY() {
        return position.y;
    }

    public float getPositionZ() {
        return position.z;
    }


    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }


    //Rotation
    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void setRotationX(float x) {
        rotation.x = x;
    }

    public void setRotationY(float y) {
        rotation.y = y;
    }

    public void setRotationZ(float z) {
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotation.x += dx;
        this.rotation.y += dy;
        this.rotation.z += dz;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getRotationX() {
        return rotation.x;
    }

    public float getRotationY() {
        return rotation.y;
    }

    public float getRotationZ() {
        return rotation.z;
    }


    //Scale
    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }
}
