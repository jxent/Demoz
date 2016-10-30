package com.jason.adrlog;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Jason on 2016/10/24.
 */
public class Util {

    /**
     * 判断一行文字是否为空字符串、null、换行符、tab、空格
     * @param line
     * @return 如果是上述罗列，返回true，如果有其他字符返回false
     */
    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

//    public static void printJsonFormatLine(String tag, boolean isTop) {
//        if (isTop) {
//            Log.d(tag, "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//        } else {
//            Log.dag, "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
//     }
//    }

    /**
     * 打印json的格式框
     * @param tag
     * @param isTop 头部还是底部
     */
    public static void printJsonFormatLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    /**
     * 打印xml的格式框
     * @param tag
     * @param isTop 头部还是底部
     */
    public static void printXmlFormatLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "┌───────────────────────────────────────────────────────────────────────────────────────");
        } else {
            Log.d(tag, "└───────────────────────────────────────────────────────────────────────────────────────");
        }
    }
    
/// default message ----------------------------------------------------------------

    /**
     * 打印一般日志
     * @param type  日志级别
     * @param tag   tag
     * @param msg   具体日志信息
     * @param tr    异常Throwable对象
     */
    public static void printDefault(int type, String tag, String msg, Throwable tr) {

        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub, tr);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index, msg.length()), tr);
        } else {
            printSub(type, tag, msg, tr);
        }
    }

    private static void printSub(int type, String tag, String sub, Throwable tr) {
        switch (type) {
            case AdrLog.V:
                Log.v(tag, sub, tr);
                break;
            case AdrLog.D:
                Log.d(tag, sub, tr);
                break;
            case AdrLog.I:
                Log.i(tag, sub, tr);
                break;
            case AdrLog.W:
                Log.w(tag, sub, tr);
                break;
            case AdrLog.E:
                Log.e(tag, sub, tr);
                break;
            case AdrLog.A:
                Log.wtf(tag, sub, new RuntimeException("你回来啦"));
                break;
        }
    }

/// json ----------------------------------------------------------------

    static void printJson(String tag, String msg, String headString) {
        String message;
        boolean isValid = false;
        try {
            if (msg.startsWith("{")) {
                isValid = true;
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(AdrLog.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                isValid = true;
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(AdrLog.JSON_INDENT);
            } else {
                isValid = false;
                message = "[※JSON语法错误] " + msg;
            }
        } catch (JSONException e) {
            isValid = false;
            message = "[※JSON语法错误] " + msg;
        }

        printJsonFormatLine(tag, true);
        message = headString + AdrLog.LINE_SEPARATOR + message;
        String[] lines = message.split(AdrLog.LINE_SEPARATOR);
        for (String line : lines) {
            if(isValid) {
                Log.d(tag, "║ " + line);
            }else {
                Log.e(tag, "║ " + line);
            }
        }
        printJsonFormatLine(tag, false);
    }

/// xml ----------------------------------------------------------------

    static void printXml(String tag, String xml, String headString) {

        if (xml != null) {
            xml = formatXML(xml);
            xml = headString + "\n" + xml;
        } else {
            xml = headString + AdrLog.NULL_TIPS;
        }

        printXmlFormatLine(tag, true);
        String[] lines = xml.split(AdrLog.LINE_SEPARATOR);
        for (String line : lines) {
            if (!isEmpty(line)) {
                Log.d(tag, "│ " + line);
            }
        }
        printXmlFormatLine(tag, false);
    }

    static String formatXML(String inputXML) {
        try {
            Source xmlInput = new StreamSource(new StringReader(inputXML));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception e) {
            e.printStackTrace();
            return inputXML;
        }
    }
    
/// log output to file ----------------------------------------------------------------

    static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    static boolean createOrExistsFile(File file) {
        if (file == null)
            return false;
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists())
            return file.isFile();
        if (!createOrExistsDir(file.getParentFile()))
            return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param dirPath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    static File getFileByPath(String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

/// file ----------------------------------------------------------------

    static void printFile(String tag, File targetDirectory, String fileName, String headString, String msg) {

        fileName = (fileName == null) ? getFileName() : fileName;
        if (save(targetDirectory, fileName, msg)) {
            Log.d(tag, headString + " save log success ! location is >>>" + targetDirectory.getAbsolutePath() + "/" + fileName);
        } else {
            Log.e(tag, headString + "save log fails !");
        }
    }

    private static boolean save(File dic, String fileName, String msg) {

        File file = new File(dic, fileName);

        try {
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            outputStreamWriter.write(msg);
            outputStreamWriter.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static String getFileName() {
        Random random = new Random();
        return "JLog_" + Long.toString(System.currentTimeMillis() + random.nextInt(10000)).substring(4) + ".txt";
    }
}
