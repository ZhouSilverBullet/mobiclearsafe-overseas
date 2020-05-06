package com.mobi.overseas.clearsafe.ui.clear.util;

import android.text.TextUtils;
import android.util.Log;

import com.mobi.overseas.clearsafe.ui.clear.data.LenFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static final String TAG = "FileUtil";

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(java.io.File file) {
        if (!file.exists()) {
            return 0L;
        }
        long size = 0L;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static List<File> getCacheFolder(File file, IFileLengthListener listener) {
        List<File> files = new ArrayList<>();
        findCacheFile(file, files, listener);
        return files;
    }

    private static void findCacheFile(File file, List<File> files, IFileLengthListener listener) {
        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    if ("cache".equals(fileList[i].getName())) {
                        files.add(fileList[i]);
                        listener.onFileLen(getFolderSize(fileList[i]));
                        return;
                    }
                    findCacheFile(fileList[i], files, listener);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {//判断是否待删除目录是否存在
            return false;
        }
        if (file.isFile()) {
            file.delete();
        }
        return true;
    }

    public static boolean deleteFile(File file) {
        if (!file.exists()) {//判断是否待删除目录是否存在
            return false;
        }
        if (file.isFile()) {
            file.delete();
        }
        return true;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param filePath
     * @param deleteThisPath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        boolean delete = file.delete();
                        Log.e(TAG, " delete1 : " + delete);
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            boolean delete = file.delete();
                            Log.e(TAG, " delete2 : " + delete);
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param filePath
     * @param deleteThisPath
     * @return
     */
    public static void deleteFolderFile(String filePath, IFileLengthListener listener, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        long length = file.length();
                        if (listener != null) {
                            listener.onFileLen(length);
                        }
                        boolean delete = file.delete();
                        Log.e(TAG, " delete1 : " + delete);
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            boolean delete = file.delete();
                            Log.e(TAG, " delete2 : " + delete);
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param file
     * @param deleteThisPath
     * @return
     */
    public static void deleteFolderFile(File file, IFileLengthListener listener, boolean deleteThisPath) {
        if (file == null) {
            return;
        }
        try {
            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    long length = file.length();
//                    if (listener != null) {
//                        listener.onFileLen(length);
//                    }
                    boolean delete = file.delete();
                    Log.e(TAG, " delete1 : " + delete);
                } else {// 目录
                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        boolean delete = file.delete();
                        Log.e(TAG, " delete2 : " + delete);
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取文件大小
     *
     * @param size
     * @return
     */
    public static String getFileSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * 获取文件大小
     *
     * @param size
     * @return
     */
    public static String[] getFileSize0(long size) {
        String[] index = new String[2];
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            index[0] = String.valueOf(size);
            index[1] = "B";
            return index;
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            index[0] = String.valueOf(size);
            index[1] = "KB";
            return index;
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            index[0] = String.valueOf((size / 100)) + "." + String.valueOf((size % 100));
            index[1] = "MB";
            return index;
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            index[0] = String.valueOf((size / 100)) + "." + String.valueOf((size % 100));
            index[1] = "GB";
            return index;
        }
    }


    /**
     * 指定文件
     *
     * @param file
     * @param files
     * @param fileName 指定文件名 如 cache
     * @param listener
     */
    public static void findPositionFile(File file, List<File> files, String fileName, IFileListener listener) {
        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    if (fileName.equals(fileList[i].getName())) {
                        files.add(fileList[i]);
                        listener.onFile(fileList[i], getFolderSize(fileList[i]));
                        return;
                    }
                    findPositionFile(fileList[i], files, fileName, listener);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 后缀名
     *
     * @param file
     * @param files
     * @param suffix   后缀名 .apk 什么的
     * @param listener
     */
    public static void findSuffixFile(File file, List<File> files, String suffix, IFileListener listener) {
        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    findSuffixFile(fileList[i], files, suffix, listener);
                } else {
                    if (fileList[i].getName().endsWith(suffix)) {
                        files.add(fileList[i]);
                        listener.onFile(fileList[i], fileList[i].length());
                        return;
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static long deleteFileOrDirectory(LenFile file) {
        long length = 0L;
        boolean b = file.canWrite();
        Log.e(TAG, file.getPath() + " canWrite = " + b + " canRead = " + file.canRead());
//        if (file.isFile()) {
//            //文件长度
//            length = file.length();
//
//
//            boolean isDelete = FileUtils.deleteQuietly(file);
//            //如果删除失败就强制删除
//            if (!isDelete) {
//                try {
//                    FileUtils.forceDeleteOnExit(file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        } else {
//            //原本文件的长度
//            length = file.getLen();
//
//            try {
//                FileUtils.forceDeleteOnExit(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
        length = file.getLen();
        deleteToFile(file);
        return length;
    }

    /**
     *
     * @param file
     * @param currentSize 如果是0 默认使用的是file.length()
     * @return
     */
    public static LenFile getLenFile(File file, long currentSize) {
        LenFile lenFile = new LenFile(file.getPath());
        lenFile.setLen(currentSize);
        return lenFile;
    }


    public static void deleteToFile(File file) {
        if (file.isFile()) {
            deleteFileSafely(file);
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                deleteFileSafely(file);
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteToFile(childFiles[i]);
            }
            deleteFileSafely(file);
        }
    }


    /**
     * 安全删除文件.
     *
     * @param file
     * @return
     */
    public static boolean deleteFileSafely(File file) {
        if (file != null) {
            String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
            File tmp = new File(tmpPath);
            boolean b = file.renameTo(tmp);
            Log.e(TAG, " file.renameTo =  " + b);
            return tmp.delete();
        }
        return false;
    }

}
