package org.yanzi.camera;

import android.util.Log;

/**
 * 最终裁剪区域
 * Created by Administrator on 2016/6/30.
 */
public class RealityInfo {
    int realityX;
    int realityY;
    int realityWidth;
    int realityHeigh;

    /**
     * @param cropInfo
     * @param realWidth 被裁剪bitmap的宽
     */
    public RealityInfo(CropInfo cropInfo, int realWidth, int realHeight) {
        float scaleWidthSize = cropInfo.screenWidth / realWidth;
        float scaleHeightSize = cropInfo.screenHeight / realHeight;
        //按比例计算裁剪位置，并略微扩大一点范围。
        this.realityX = (int) (cropInfo.x / scaleWidthSize)-4;
        this.realityY = (int) (cropInfo.y / scaleHeightSize)-4;
        this.realityWidth = (int) (cropInfo.width / scaleWidthSize) + 8;
        this.realityHeigh = (int) (cropInfo.height / scaleHeightSize) + 8;

        if (realityX < 0) {
            realityX = 0;
        }
        if (realityY < 0) {
            realityY = 0;
        }
        while ((realityX + realityWidth) > realWidth) {
            if (realityX > 0) {
                realityX -= 1;
            }
            realityWidth -= 1;
        }

        while ((realityY + realityHeigh) > realHeight) {
            if (realityY > 0) {
                realityY -= 1;
            }
            realityHeigh -= 1;
        }
        Log.i("RealityInfo", "x:" + realityX + ",y:" + realityY + ",realityWidth:" + realityWidth + ",realityHeigh:" + realityHeigh);
    }
}
