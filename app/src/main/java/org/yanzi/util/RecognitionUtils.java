package org.yanzi.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;

import com.tianruiworkroomocr.Native;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/6/30.
 */
public class RecognitionUtils {
    public static final String mstrFilePathForDat = Environment.getExternalStorageDirectory().toString() + "/tianrui";
    private boolean bTianruiInited = false;
    private static RecognitionUtils recognitionUtils;

    public static synchronized RecognitionUtils getInstance() {
        if (recognitionUtils == null) {
            recognitionUtils = new RecognitionUtils();
        }
        return recognitionUtils;
    }

    public RecognitionUtils() {
       // initTianRui();
    }

    public void initTianRui() {
        if (!bTianruiInited) {
            int rlt = Native.openOcrEngine(mstrFilePathForDat); // step 1: open OCR engine
            rlt = Native.setOcrLanguage(Native.TIANRUI_LANGUAGE_CHINESE_SIMPLIFIED); // step 2: set recognition language
            bTianruiInited = true;
        }
    }

    public String startDecodeThread(Bitmap mBmppp) {
        initTianRui();
        final long t1 = System.currentTimeMillis();
        int picw = mBmppp.getWidth();
        int pich = mBmppp.getHeight();
        int[] pix = new int[picw * pich];

        mBmppp.getPixels(pix, 0, picw, 0, 0, picw, pich);
        int rlt = 0;
        rlt = Native.recognizeImage(pix, picw, pich); // step 3: recognize one image
        if (rlt != 1) {
            return "-1";
        } else {
            long t2 = System.currentTimeMillis() - t1;
            String builder = new String();
            String[] mwholeTextLine = Native.getWholeTextLineResult();
            for (int i = 0; i < mwholeTextLine.length; i++) {
                builder += mwholeTextLine[i].replace(" ", "") + "\r\n";
            }
//            return Long.toString(t2) + "mS:\n" + getNum(builder);11111111111111111X
            return getNum(builder);
        }
    }

    //取数字
    String getNum(String str) {
        String result="";
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);

        while (m.find()) {
            String tempStr = m.group();
            if (!"".equals(tempStr)) {
                if (tempStr.length() == 11&&isMobileNO(tempStr)) {
                    result+="手机号:" +tempStr+"\n";
                    System.out.println("手机号:" + tempStr);
                }
                if (tempStr.length() == 17) {
                    result+="身份证号码:" +tempStr+"X"+"\n";
                    System.out.println("身份证号码:" + tempStr);
                }
                if (tempStr.length() == 18) {
                    result+="身份证号码:" +tempStr+"\n";
                    System.out.println("身份证号码:" + tempStr);
                }
            }
        }
        if(result.length()<1){
            return "-1";
        }else {
            return result;
        }
    }

    public boolean isMobileNO(String mobiles){

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        System.out.println(m.matches()+"---");

        return m.matches();

    }
}
