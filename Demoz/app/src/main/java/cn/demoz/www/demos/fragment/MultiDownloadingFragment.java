package cn.demoz.www.demos.fragment;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.jason.slog.SLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import cn.demoz.www.R;
import cn.demoz.www.base.BaseDemosFragment;
import cn.demoz.www.http.HttpHelper;

public class MultiDownloadingFragment extends BaseDemosFragment {

    private static final String TAG = "";
    static int ThreadCount = 3;
    static int finishedThread = 0;
    int currentProgress;

    private Button toDownload;
    private ProgressBar pbDownloading;

    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //把变量改成long，在long下运算
        }
    };
    private String path;

    @Override
    public View setDemoContentView(Context context, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.multi_downloading_layout, null);
        toDownload = (Button) view.findViewById(R.id.to_download);
        pbDownloading = (ProgressBar) view.findViewById(R.id.pb_downloading);

        toDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                            //发送get请求，请求这个地址的资源
                            try {
                                path = HttpHelper.URL + "multi_downloading";
                                URL url = new URL(path);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.setConnectTimeout(5000);
                                conn.setReadTimeout(5000);

                                if (conn.getResponseCode() == 200) {
                                    //拿到所请求资源文件的长度
                                    int length = conn.getContentLength();

                                    //设置进度条的最大值就是原文件的总长度
                                    pbDownloading.setMax(length);

                                    File file = new File(Environment.getExternalStorageDirectory(), "result.avi");
                                    //生成临时文件
                                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                                    //设置临时文件的大小
                                    raf.setLength(length);
                                    raf.close();
                                    //计算出每个线程应该下载多少字节
                                    int size = length / ThreadCount;

                                    for (int i = 0; i < ThreadCount; i++) {
                                        //计算线程下载的开始位置和结束位置
                                        int startIndex = i * size;
                                        int endIndex = (i + 1) * size - 1;
                                        //如果是最后一个线程，那么结束位置写死
                                        if (i == ThreadCount - 1) {
                                            endIndex = length - 1;
                                        }
                                        SLog.i(TAG, "线程" + i + "的下载区间是：" + startIndex + "---" + endIndex);
                                        new DownLoadThread(startIndex, endIndex, i).start();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                };
                t.start();
            }
        });

        return view;
    }

    class DownLoadThread extends Thread{
        int startIndex;
        int endIndex;
        int threadId;

        public DownLoadThread(int startIndex, int endIndex, int threadId) {
            super();
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            //再次发送http请求，下载原文件
            try {
                File progressFile = new File(Environment.getExternalStorageDirectory(), threadId + ".txt");
                //判断进度临时文件是否存在
                if(progressFile.exists()){
                    FileInputStream fis = new FileInputStream(progressFile);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    //从进度临时文件中读取出上一次下载的总进度，然后与原本的开始位置相加，得到新的开始位置
                    int lastProgress = Integer.parseInt(br.readLine());
                    if(lastProgress > endIndex - startIndex){
                        lastProgress = 0;
                    }else {
                        startIndex += lastProgress;
                    }

                    //把上次下载的进度显示至进度条
                    currentProgress += lastProgress;
                    pbDownloading.setProgress(currentProgress);

                    //发送消息，让主线程刷新文本进度
                    handler.sendEmptyMessage(1);
                    fis.close();
                }
                SLog.i(TAG, "线程" + threadId + "的下载区间是：" + startIndex + "---" + endIndex);
                HttpURLConnection conn;
                URL url = new URL(path);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                //设置本次http请求所请求的数据的区间
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);

                //请求部分数据，相应码是206
                if(conn.getResponseCode() == 206){
                    SLog.i(TAG, "请求到数据！");
                    //流里此时只有1/3原文件的数据
                    InputStream is = conn.getInputStream();
                    byte[] b = new byte[1024];
                    int len = 0;
                    int total = 0;
                    //拿到临时文件的输出流
                    File file = new File(Environment.getExternalStorageDirectory(), "result.avi");
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    //把文件的写入位置移动至startIndex
                    raf.seek(startIndex);
                    while((len = is.read(b)) != -1){
                        //每次读取流里数据之后，同步把数据写入临时文件
                        raf.write(b, 0, len);
                        total += len;
                        System.out.println("线程" + threadId + "下载了" + total);

                        //每次读取流里数据之后，把本次读取的数据的长度显示至进度条
                        currentProgress += len;
                        pbDownloading.setProgress(currentProgress);
                        //发送消息，让主线程刷新文本进度
                        handler.sendEmptyMessage(1);

                        //生成一个专门用来记录下载进度的临时文件
                        RandomAccessFile progressRaf = new RandomAccessFile(progressFile, "rwd");
                        //每次读取流里数据之后，同步把当前线程下载的总进度写入进度临时文件中
                        progressRaf.write((total + "").getBytes());
                        progressRaf.close();
                    }
                    SLog.i(TAG, "线程" + threadId + "下载完毕-------------------！");
                    raf.close();

                    finishedThread++;
                    synchronized (path) {
                        if(finishedThread == ThreadCount){
                            for (int i = 0; i < ThreadCount; i++) {
                                File f = new File(Environment.getExternalStorageDirectory(), i + ".txt");
                                f.delete();
                            }
                            finishedThread = 0;
                        }
                    }

                }
            } catch (SocketTimeoutException e) {
                SLog.e(TAG, "socket timeout exception");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
