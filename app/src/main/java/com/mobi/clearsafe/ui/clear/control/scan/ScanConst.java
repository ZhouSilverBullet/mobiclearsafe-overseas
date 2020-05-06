package com.mobi.clearsafe.ui.clear.control.scan;

import android.os.Environment;
import android.os.Handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/27 09:48
 * @Dec 略
 */
public class ScanConst {

    public static final String[] FILE_CATEGORY_ICON = {"ic_video", "ic_document", "ic_picture", "ic_music",
            "ic_apk", "ic_zip"};

    //每种类型的文件包含的后缀
    public static final Map<String, Set<String>> CATEGORY_SUFFIX;

    static {
        //初始化赋值
        CATEGORY_SUFFIX = new HashMap<>(FILE_CATEGORY_ICON.length);
        Set<String> set = new HashSet<>();
//        set.add("mp4");
//        set.add("avi");
//        set.add("wmv");
//        set.add("flv");
//        CATEGORY_SUFFIX.put("video", set);
//
//        set.add("txt");
//        set.add("pdf");
//        set.add("doc");
//        set.add("docx");
//        set.add("xls");
//        set.add("xlsx");
//        CATEGORY_SUFFIX.put("document", set);
//
//        set = new HashSet<>();
//        set.add("jpg");
//        set.add("jpeg");
//        set.add("png");
//        set.add("bmp");
//        set.add("gif");
//        CATEGORY_SUFFIX.put("picture", set);
//
//        set = new HashSet<>();
//        set.add("mp3");
//        set.add("ogg");
//        CATEGORY_SUFFIX.put("music", set);

        set = new HashSet<>();
        set.add("apk");
        CATEGORY_SUFFIX.put("apk", set);

//        set = new HashSet<>();
//        set.add("zip");
//        set.add("rar");
//        set.add("7z");
//        CATEGORY_SUFFIX.put("zip", set);
    }

    /**
     * 扫描文件
     */
    private void scanFile(Handler handler) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        //单一线程线程池
        ExecutorService singleExecutorService = Executors.newSingleThreadExecutor();
        singleExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                ScanFileCountUtil scanFileCountUtil = new ScanFileCountUtil
                        .Builder(handler)
                        .setFilePath(path)
                        .setCategorySuffix(CATEGORY_SUFFIX)
                        .create();
                scanFileCountUtil.scanCountFile();
            }
        });
    }
}
