package com.jason.slog;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 功能：分级别打印日志，按级别过滤级别，日志开关、保存到本地文件，日志格式化
 * 支持：普通log、json数据、xml数据、文本文件、Bundle
 * todo: 支持打印Bundle
 * 使用方法：
 *     SLog.getBuilder(getApplicationContext())
 *         .setDefTag("jason")
 *         .setLogSwitch(true)
 *         .setLog2FileSwitch(false)
 *         .setLogLevel(SLog.V)
 *         .create();
 *     SLog.v("我是log");
 *     SLog.a("tag", "我是log");
 *
 * */
public class SLog {

    private SLog() {
        throw new UnsupportedOperationException("对象不能被实例化！");
    }

    public static final int V = 0x00;
    public static final int D = 0x01;
    public static final int I = 0x02;
    public static final int W = 0x03;
    public static final int E = 0x04;
    public static final int A = 0x05;
    public static final int J = 0x06;    // json
    public static final int X = 0x07;    // xml
    public static final int F = 0x08;    // file

    public static final String[] types = new String[]{"Verbose", "Debug", "Info",
                                            "Warning", "Error", "Assert"};

    private static boolean logSwitch = true;        // 是否打印log, 不打印同时也不会输出到文件
    private static boolean log2FileSwitch = false;  // 是否输出到文件
    // 打印或输出log的级别，依次是(V D I W E A)，V全部输出，E 只输出E及A级别log
    private static int logLevel = D;
    private static String defaultTag = "SLog";
    private static String outputFileDir = null;

    private static final int BASE_STACK_TRACE_INDEX = 4;
    private static int stackTraceIndex = BASE_STACK_TRACE_INDEX;
    public static final int JSON_INDENT = 4;
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");   // 系统的换行符
    public static final String NULL_TIPS = "Log with null object";
    private static final String PARAM = "Param";
    private static final String NULL = "null";
    private static final String JAVA_FILE_SUFFIX = ".java";

    /**
     * 初始化SLog，与getBuilder作用一致
     * @param context
     * @param logSwitch
     * @param log2FileSwitch
     * @param logLevel
     * @param tag
     */
    public static void init(Context context, boolean logSwitch, boolean log2FileSwitch, int logLevel, String tag) {
        setOutputFileDir(context);
        SLog.logSwitch = logSwitch;
        SLog.log2FileSwitch = log2FileSwitch;
        SLog.logLevel = logLevel;
        SLog.defaultTag = tag;
    }

    /**
     * 获取初始化Builder对象，调用create()初始化SLog
     * @param context
     * @return
     */
    public static Builder getBuilder(Context context) {
        setOutputFileDir(context);
        return new Builder();
    }

    /**
     * Builder对象
     */
    public static class Builder {
        // 默认设置，默认输出log，不输出到文件，只输出debug以上的log
        private boolean logSwitch = true;
        private boolean log2FileSwitch = false;
        private int logLevel = D;
        private String defTag = "SLog";

        public Builder setLogSwitch(boolean logSwitch) {
            this.logSwitch = logSwitch;
            return this;
        }

        public Builder setLog2FileSwitch(boolean log2FileSwitch) {
            this.log2FileSwitch = log2FileSwitch;
            return this;
        }

        public Builder setLogLevel(int logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder setDefTag(String defTag) {
            this.defTag = defTag;
            return this;
        }

        public void create() {
            SLog.logSwitch = logSwitch;
            SLog.log2FileSwitch = log2FileSwitch;
            SLog.logLevel = logLevel;
            SLog.defaultTag = defTag;
        }
    }

    /**
     * 设置log输出本地路径
     * @param context
     */
    public static void setOutputFileDir(Context context){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            outputFileDir = context.getExternalCacheDir().getPath() + File.separator;
        } else {
            outputFileDir = context.getCacheDir().getPath() + File.separator;
        }
    }

    public static void v(String msg) {
        stackTraceIndex++;
        v(defaultTag, msg);
    }

    public static void v(String tag, Object... msg) {
        stackTraceIndex++;
        v(tag, null, msg);
    }

    /**
     * verbose
     * @param tag
     * @param tr
     * @param msg
     */
    public static void v(String tag, Throwable tr, Object... msg) {
        stackTraceIndex++;
        log(V, tag, tr, msg);
    }

    public static void d(String msg) {
        stackTraceIndex++;
        d(defaultTag, msg);
    }

    public static void d(String tag, Object... msg) {// 调试信息
        stackTraceIndex++;
        d(tag, null, msg);
    }

    /**
     * debug
     * @param tag
     * @param tr
     * @param msg
     */
    public static void d(String tag, Throwable tr, Object... msg) {
        stackTraceIndex++;
        log(D, tag, tr, msg);
    }

    public static void i(String msg) {
        stackTraceIndex++;
        i(defaultTag, msg);
    }

    public static void i(String tag, Object... msg) {
        stackTraceIndex++;
        i(tag, null, msg);
    }

    /**
     * info
     * @param tag
     * @param tr
     * @param msg
     */
    public static void i(String tag, Throwable tr, Object... msg) {
        stackTraceIndex++;
        log(I, tag, tr, msg);
    }

    public static void w(String msg) {
        stackTraceIndex++;
        w(defaultTag, msg);
    }

    public static void w(String tag, Object... msg) {
        stackTraceIndex++;
        w(tag, null, msg);
    }

    /**
     * warning
     * @param tag
     * @param tr
     * @param msg
     */
    public static void w(String tag, Throwable tr, Object... msg) {
        stackTraceIndex++;
        log(W, tag, tr, msg);
    }

    public static void e(String msg) {
        stackTraceIndex++;
        e(defaultTag, msg);
    }

    public static void e(String tag, Object... msg) {
        stackTraceIndex++;
        e(tag, null, msg);
    }

    /**
     * error
     * @param tag
     * @param tr
     * @param msg
     */
    public static void e(String tag, Throwable tr, Object... msg) {
        stackTraceIndex++;
        log(E, tag, tr, msg);
    }

    public static void a(String msg) {
        stackTraceIndex++;
        a(defaultTag, msg);
    }

    public static void a(String tag, Object... msg) {
        stackTraceIndex++;
        a(tag, null, msg);
    }

    /**
     * assert
     * @param tag
     * @param tr
     * @param msg
     */
    public static void a(String tag, Throwable tr, Object... msg) {
        stackTraceIndex++;
        log(A, tag, tr, msg);
    }

    /**
     * 打印json数据
     * @param tag
     * @param jsonStr
     */
    public static void json(String tag, String jsonStr){
        stackTraceIndex++;
        log(J, tag, null, jsonStr);
    }

    /**
     * 打印xml数据
     * @param tag
     * @param xmlStr
     */
    public static void xml(String tag, String xmlStr){
        stackTraceIndex++;
        log(X, tag, null, xmlStr);
    }

    /**
     * 打印文件(目前只是保存到设备中，并输出信息)
     * todo: 将文件按需输出指定的行数据
     * @param tag
     * @param filePath
     */
    public static void file(String tag, String filePath){
        stackTraceIndex++;
        log(F, tag, null, filePath);
    }

    private static void log(int type, String tag, Throwable tr, Object... objects) {
        if (logSwitch) {
            String[] contents = wrapperContent(tag, objects);
            stackTraceIndex = BASE_STACK_TRACE_INDEX; // 复原
            String wrappedTag = contents[0];
            String wrappedMsg = contents[1];
            String wrappedHeaderInfo = contents[2];
            String wrappedHeaderWithoutMethod = contents[3];

            if(type <= A && type >= logLevel){
                Util.printDefault(type, wrappedTag, wrappedMsg, wrappedHeaderWithoutMethod, tr);
            }

            if(J == type){
                Util.printJson(wrappedTag, wrappedMsg, wrappedHeaderInfo);
            }

            if(X == type){
                Util.printXml(wrappedTag, wrappedMsg, wrappedHeaderInfo);
            }

            if(F == type){
                Util.printFile(wrappedTag, Util.getFileByPath(outputFileDir), "", wrappedMsg, wrappedHeaderInfo);
            }

            if (log2FileSwitch) {
                log2File(type, wrappedTag, wrappedMsg + '\n' + Log.getStackTraceString(tr));
            }
        }
    }

    private static String[] wrapperContent(String tagStr, Object... objects) {
        // 返回当前线程的栈跟踪元素信息数组
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[stackTraceIndex];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + JAVA_FILE_SUFFIX;
        }

        if (className.contains("$")) {
            className = className.split("\\$")[0] + JAVA_FILE_SUFFIX;
        }

        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }

//        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        String tag = (tagStr == null ? className : tagStr);

        if(TextUtils.isEmpty(tag)){
            tag = defaultTag;
        }

        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
        String headString = "[(" + className + ":" + lineNumber + ")#" + methodName+"]";
//        String headStringWithoutMethod = "(" + className + ":" + lineNumber + ")";

        return new String[]{tag, msg, headString, headString};
    }

    private static String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else if(objects.length == 1){
            Object object = objects[0];
            return object == null ? NULL : object.toString();
        }
        return NULL;
    }

    private synchronized static void log2File(int type, String tag, String content) {
        if (content == null) {
            return;
        }
        Date now = new Date();
        String date = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(now);
        String fullPath = outputFileDir + date + ".txt";
        if (!Util.createOrExistsFile(fullPath)) {
            return;
        }
        String time = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(now);
        String dateLogContent = time + ":" + types[type] + ":" + tag + ":" + content + '\n';
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(fullPath, true));
            bw.write(dateLogContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Util.closeIO(bw);
        }
    }
}