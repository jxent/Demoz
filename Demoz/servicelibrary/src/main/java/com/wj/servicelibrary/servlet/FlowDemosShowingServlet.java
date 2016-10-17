package com.wj.servicelibrary.servlet;

import android.os.Environment;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public class FlowDemosShowingServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static String localJsonPath = "flow_demos.json";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);

		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + "WebInfos/";
		File jsonFile = new File(path, localJsonPath);

		// 使用okio API读取服务器json文件并返回给客户端json数据
		String strResponse = readLocalJsonFile(jsonFile);
		if(TextUtils.isEmpty(strResponse)){
			strResponse = "{\"code\":-100,\"items\": []}";
		}

		byte[] b = strResponse.getBytes();
		resp.setContentLength(b.length);
		OutputStream out = resp.getOutputStream();
		out.write(b);
		out.close();
	}

	private String readLocalJsonFile(File file){
		Source source;
		BufferedSource bufferedSource = null;
		String content = null;
		try {
			source = Okio.source(file);
			bufferedSource = Okio.buffer(source);
			content = bufferedSource.readUtf8();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(bufferedSource);
		}
		return content;
	}

	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (RuntimeException rethrown) {
				throw rethrown;
			} catch (Exception ignored) {

			}
		}
	}
}
