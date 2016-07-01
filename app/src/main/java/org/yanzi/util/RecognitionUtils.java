package org.yanzi.util;

import android.graphics.Bitmap;
import android.os.Environment;

import com.tianruiworkroomocr.Native;

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
        initTianRui();
    }

    public void initTianRui() {
        if (!bTianruiInited) {
            int rlt = Native.openOcrEngine(mstrFilePathForDat); // step 1: open OCR engine
            rlt = Native.setOcrLanguage(Native.TIANRUI_LANGUAGE_CHINESE_SIMPLIFIED); // step 2: set recognition language
            bTianruiInited = true;
        }
    }

    //这个方法比较耗时，需要新开线程使用。
    public String startDecodeThread(Bitmap mBmppp) {
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
            //识别所花时间
            long t2 = System.currentTimeMillis() - t1;
            String builder = new String();
            String[] mwholeTextLine = Native.getWholeTextLineResult();
            for (int i = 0; i < mwholeTextLine.length; i++) {
                //拼接字符串并去空格
                builder += mwholeTextLine[i].replace(" ", "") + "\r\n";
            }
//            return Long.toString(t2) + "mS:\n" + getNum(builder);11111111111111111X
            return getNum(builder);
        }
    }

    //取数字
    String getNum(String str) {
        String result = "";
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        Identity identity = new Identity();

        while (m.find()) {
            String tempStr = m.group();
            if (!"".equals(tempStr)) {
                if (tempStr.length() == 11 && isMobileNO(tempStr)) {
                    result += "手机号:" + tempStr + "\n";
                    System.out.println("手机号:" + tempStr);
                }
                if (tempStr.length() == 17 && identity.checkIDCard(tempStr + "X")) {
                    result += "身份证号码:" + tempStr + "X" + "\n";
                    System.out.println("身份证号码:" + tempStr);
                }
                if (tempStr.length() == 18 && identity.checkIDCard(tempStr)) {
                    result += "身份证号码:" + tempStr + "\n";
                    System.out.println("身份证号码:" + tempStr);
                }
            }
        }
        if (result.length() < 1) {
            return "-1";
        } else {
            return result;
        }
    }

    //判断是否手机号
    public boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");

        return m.matches();

    }
}
