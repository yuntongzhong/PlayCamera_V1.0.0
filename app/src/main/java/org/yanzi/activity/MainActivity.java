package org.yanzi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.yanzi.playcamera.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/30.
 */
public class MainActivity extends Activity {
    private  final int RESULT_CAMERA=0X123;
    public static final String mstrFilePathForDatSave = Environment.getExternalStorageDirectory().toString() + "/tianrui/TianruiWorkroomOCR.dat";
    public static final String mstrFilePathForDat = Environment.getExternalStorageDirectory().toString() + "/tianrui";
    private boolean mFileExist = false;

    @Bind(R.id.recognition)
    Button recognition;
    @Bind(R.id.tv_result)
    TextView tvResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        copyTianRuiRes();
    }

    @OnClick(R.id.recognition)
    public void onClick() {
        Intent intent=new Intent(this,CameraActivity.class);
        startActivityForResult(intent,RESULT_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK||requestCode!=RESULT_CAMERA) {
            return;
        }
        String info=data.getStringExtra("info");
        if(info!=null){
            Log.e("info",info);
            tvResult.setText(info);
        }


    }


    private void copyTianRuiRes() {
        mFileExist = fileIsExists(mstrFilePathForDatSave);
        if (!mFileExist) {
            Thread trimmingThread = new Thread(new Runnable() {
                public void run() {
                    CopyAssets("", mstrFilePathForDat);
                }
            });
            trimmingThread.start();
        }
    }



    public boolean fileIsExists(String filePath) {
        long fLength = 0;
        try {
            File f = new File(filePath);

            if (!f.exists()) {
                return false;
            }

            fLength = f.length();
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

        if (fLength != 11140123) {
            return false;
        }

        return true;
    }

    private void CopyAssets(String assetDir, String dir) {
        String[] files;
        try {
            files = getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        // if this directory does not exists, make one.
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {

            }
        }
        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                // we make sure file name not contains '.' to be a folder.
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(fileName, dir + fileName + "/");
                    } else {
                        CopyAssets(assetDir + "/" + fileName, dir + fileName
                                + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();
                InputStream in = null;
                if (0 != assetDir.length()) {
                    in = getAssets().open(assetDir + "/" + fileName);
                } else {
                    in = getAssets().open(fileName);
                }
                OutputStream out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
