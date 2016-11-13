package com.wj.servicelibrary.servlet;

import android.os.Environment;

import com.jason.adrlog.SLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MultiDownloadingServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final java.lang.String TAG = "MultiDownloadingServlet";
	private static final int ONCE_READ = 1024 * 6;
	private static String downloadingFileName = "yyb.apk";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		boolean isRange = req.getHeader("Range") != null ? true : false;
		if(isRange){
			resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
		}else {
			resp.setStatus(HttpServletResponse.SC_OK);
		}
		SLog.i("Multi", isRange);

		long start = 0;
		long end = 0;

		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/";
		File file = new File(path, downloadingFileName);

//		String filePath = this.getServletContext().getRealPath("/");// 获取真实路径
		String filename = downloadingFileName;	// 在下载框默认显示的文件名
		filename = URLEncoder.encode(filename, "UTF-8");

		if(file.exists()) {
			long fileSize = file.length();
			resp.setContentLength((int) fileSize);
			resp.setHeader("Content-Disposition", "attachment;filename=" + filename);	// 设置在下载框默认显示的文件名
			resp.setContentType("application/octet-stream");	// 指明response的返回对象是文件

			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] b = new byte[ONCE_READ];
//			int len = 0;
			OutputStream out = resp.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(out);

			if(isRange){
				String[] header = req.getHeader("Range").replace("bytes=", "").split("-");
//				pos = Long.parseLong(req.getHeader("Range").replaceAll("bytes=", "").replaceAll("-", ""));
				start = Long.parseLong(header[0]);
				end = Long.parseLong(header[1]);

				String contentRange = new StringBuffer("bytes ")
						.append(String.valueOf(start)).append("-")
						.append(String.valueOf(fileSize - 1)).append("/")
						.append(String.valueOf(fileSize)).toString();
				resp.setHeader("Content-Range", contentRange);
				bis.skip(start);
			}

			long need = end - start;
			int len = 0;
			if(isRange) {
				while (need > 0) {
					len = bis.read(b);
					if (len == -1) {
						break;
					}
					bos.write(b,0,need > ONCE_READ?ONCE_READ:(int)need);
					need -= ONCE_READ;
				}
			}else {
				while ((len = bis.read(b)) != -1) {
					bos.write(b);
				}
			}

			fis.close();
			bis.close();
			out.close();
			bos.close();
		}
	}
}
