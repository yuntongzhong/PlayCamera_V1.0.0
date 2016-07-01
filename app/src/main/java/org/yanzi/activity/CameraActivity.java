package org.yanzi.activity;

import org.yanzi.camera.CameraInterface;
import org.yanzi.camera.CameraInterface.CamOpenOverCallback;
import org.yanzi.camera.CropInfo;
import org.yanzi.camera.preview.CameraSurfaceView;
import org.yanzi.playcamera.R;
import org.yanzi.util.DisplayUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;

public class CameraActivity extends Activity implements CamOpenOverCallback {
    private static final String TAG = "yanzi";
    CameraSurfaceView surfaceView = null;
    ImageButton shutterBtn;
    Button button;
    float previewRate = -1f;
    Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CameraInterface.getInstance().setResultCallBack(new CameraInterface.ResultCallback() {
            @Override
            public void call(String result) {
                recognitionSuccees(result);
            }
        });
    }


    @Override
    protected void onResume() {
        Log.e("tag", "onResume");
        initUI();
        initViewParams();
        shutterBtn.post(openCamera);
        super.onResume();
    }
    Runnable openCamera = new Runnable() {
        @Override
        public void run() {
            Log.e("CropInfo", button.getLeft() + "," + button.getTop() + "," + button.getWidth() + "," + button.getHeight());
            CropInfo cropInfo = new CropInfo(button.getLeft(), button.getTop(), button.getWidth(), button.getHeight(), p.x, p.y);
            CameraInterface.getInstance().doOpenCamera(cropInfo, CameraActivity.this);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    private void initUI() {
        surfaceView = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
        shutterBtn = (ImageButton) findViewById(R.id.btn_shutter);
        button = (Button) findViewById(R.id.btn_edt);
        shutterBtn.setOnClickListener(new BtnListeners());

    }

    private void initViewParams() {
        LayoutParams params = surfaceView.getLayoutParams();
        p = DisplayUtil.getScreenMetrics(this);
        Log.e("screen info", "screenWidth:" + p.x + ",screenHeigh:" + p.y);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        surfaceView.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        LayoutParams p2 = shutterBtn.getLayoutParams();
        p2.width = DisplayUtil.dip2px(this, 80);
        p2.height = DisplayUtil.dip2px(this, 80);
        ;
        shutterBtn.setLayoutParams(p2);

    }

    @Override
    public void cameraHasOpened() {
        // TODO Auto-generated method stub
        SurfaceHolder holder = surfaceView.getSurfaceHolder();
        CameraInterface.getInstance().doStartPreview(holder, previewRate);
    }
    private class BtnListeners implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_shutter:
                    CameraInterface.getInstance().doTakePicture();
                    break;
                default:
                    break;
            }
        }
    }

    void recognitionSuccees(String result){
        Intent intent = new Intent();
        intent.putExtra("info",result);
        setResult(RESULT_OK, intent);

        CameraActivity.this.finish();
    }
}
