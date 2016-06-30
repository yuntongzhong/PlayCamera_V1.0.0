package org.yanzi.camera;

/**
 * Created by Administrator on 2016/6/29.
 * Created by zyt on 2016/6/29.
 */
public class CropInfo {
    float x;
    float y;
    float width;
    float height;
    float screenWidth;
    float screenHeigh;

    public CropInfo(int x, int y, int width, int height, int screenWidth, int screenHeigh) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.screenHeigh = screenHeigh;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public float getScreenHeigh() {
        return screenHeigh;
    }

    public void setScreenHeigh(float screenHeigh) {
        this.screenHeigh = screenHeigh;
    }
}
