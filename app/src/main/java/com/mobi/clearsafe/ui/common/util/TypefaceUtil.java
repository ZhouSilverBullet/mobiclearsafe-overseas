package com.mobi.clearsafe.ui.common.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/9 19:08
 * @Dec 略
 */
public class TypefaceUtil {
    public static Typeface getTypeFace(Context context, String fontName) {
       return Typeface.createFromAsset(context.getAssets(),fontName);
    }
}
