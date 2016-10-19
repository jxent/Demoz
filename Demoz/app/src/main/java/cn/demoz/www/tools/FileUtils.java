package cn.demoz.www.tools;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtils {
    public static final String CACHE = "cache";
    public static final String ICON = "icon";
    public static final String ROOT = "demoz";

    /**
     * 获取图片的缓存的路径
     *
     * @return
     */
    public static File getIconDir() {
        return getDir(ICON);

    }

    /**
     * 获取缓存路径
     *
     * @return
     */
    public static File getCacheDir() {
        return getDir(CACHE);
    }

    /**
     * 获取目录
     * @param cache
     * @return
     */
    public static File getDir(String cache) {
        StringBuilder path = new StringBuilder();
        if (isSDAvailable()) {
            path.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            path.append(File.separator);// '/'
            path.append(ROOT);// /mnt/sdcard/demoz
            path.append(File.separator);
            path.append(cache);// /mnt/sdcard/demoz/cache

        } else {
            File filesDir = UiUtils.getContext().getCacheDir();    //  cache  getFileDir file
            path.append(filesDir.getAbsolutePath());// /data/data/com.itheima.googleplay/cache
            path.append(File.separator);///data/data/com.itheima.googleplay/cache/
            path.append(cache);///data/data/com.itheima.googleplay/cache/cache
        }
        File file = new File(path.toString());
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();// 创建文件夹
        }
        return file;

    }

    // todo 这里认为 通过分SD卡是否可以被移除两种情况考虑，可以出判断一下，不可移除的情况也算是sd开可用
    // todo 存疑 ？
    /**
     * 判断SD卡是否可用
     * @return
     */
    public static boolean isSDAvailable() {
        // Environment.MEDIA_MOUNTED ： 扩展存储设备状态为准备就绪
        // Environment.isExternalStorageRemovable() : true说明扩展存储设备是可移除的(比如sd卡)，
        // false说明扩展存数设备是内置的，不可移除掉的
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取缓存地址（扩展地址）
     * uniqueName 缓存文件唯一的名字
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        // 判断SD卡是否存在，存在将缓存放在SD卡中，没有放到内部存储中
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


}
