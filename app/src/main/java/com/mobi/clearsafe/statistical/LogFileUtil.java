package com.mobi.clearsafe.statistical;

import android.content.Context;
import android.os.Environment;

import com.mobi.clearsafe.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * author:zhaijinlu
 * date: 2019/11/18
 * desc: 日志文件读写
 */
public class LogFileUtil {

    //保存文件的路径
    private static final String CACHE_IMAGE_DIR = "aray/cache";

    /**
     * 读取固定的文件中的内容
     *
     * @param context
     * @return
     */
    public static String readFile(Context context,String fileName) {
        File file = getFile(context,fileName);
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            Reader in = new BufferedReader(isr);
            int i;
            while ((i = in.read()) > -1) {
                buffer.append((char) i);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 保存 内容到 SD卡中
     * @param str
     * @param context
     */
    public static void saveFile(String str, Context context,String fileName) {
        File file = getFile(context,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 保存的文件的地址
     * @param context
     * @return
     */
    private static File getFile(Context context,String fileName) {
        File mCropFile = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cropdir = new File(Environment.getExternalStorageDirectory(), CACHE_IMAGE_DIR);
            if (!cropdir.exists()) {
                cropdir.mkdirs();
            }
            mCropFile = new File(cropdir, fileName); // 用当前时间给取得的图片命名
        } else {
            File cropdir = new File(context.getFilesDir(), CACHE_IMAGE_DIR);
            if (!cropdir.exists()) {
                cropdir.mkdirs();
            }
            mCropFile = new File(cropdir, fileName);
        }
        return mCropFile;
    }

    /** 删除文件，可以是文件或文件夹
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(Context context,String fileName) {
        File file = getFile(context,fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(file.getPath());
            else
                return deleteDirectory(file.getPath());
        }
    }

    /** 删除单个文件
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                LogUtils.e( "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功");
                return true;
            } else {
                LogUtils.e( "Copy_Delete.deleteDirectory: 删除目录" + filePath$Name + "失败");
                return false;
            }
        } else {
            return false;
        }
    }

    /** 删除目录及目录下的文件
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            LogUtils.e( "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功");
            return true;
        } else {
            LogUtils.e( "Copy_Delete.deleteDirectory: 删除目录" + filePath + "失败");
            return false;
        }
    }


}
