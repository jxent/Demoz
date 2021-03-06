package com.wj.servicelibrary.server;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.Closeable;
import java.io.File;

/**
 */
public class WebService extends Service {

	private Server server;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		startForeground(9999, new Notification());
		startServer();
	}

	@Override
	public void onDestroy() {
		stopServer();
		super.onDestroy();
	}

	private void startServer() {
		if (server != null) {
			Toast.makeText(this, "服务器已经开启", Toast.LENGTH_SHORT).show();
			return;
		}
		new Thread(new StartRunnable()).start();
	}

	private void stopServer() {
		if (server != null) {
			new Thread(new StopRunnable()).start();
		}
	}

	class StartRunnable implements Runnable {
		@Override
		public void run() {
			try {
				File JETTY_DIR = new File(Environment.getExternalStorageDirectory(), "jetty");
				// Set jetty.home
				System.setProperty("jetty.home", JETTY_DIR.getAbsolutePath());

				// ipv6 workaround for froyo
				System.setProperty("java.net.preferIPv6Addresses", "false");

				server = new Server(8090);
				// server.setHandler(new DefaultHandler());
				ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
				contextHandler.setContextPath("/DemozWeb");// 指定上下文路径，项目目录 /DemozWeb/flows...?index...
				server.setHandler(contextHandler);
				ServletConfig.config(contextHandler);

				server.start();
				server.join();
				Looper.prepare();		// looper 线程
				Looper.loop();
				Toast.makeText(WebService.this, "服务器启动", Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				server = null;
				e.printStackTrace();
				Toast.makeText(WebService.this, "服务器启动失败", Toast.LENGTH_SHORT).show();
			}
		}
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

	class StopRunnable implements Runnable {
		@Override
		public void run() {
			try {
				server.stop();
				server = null;
				Looper.prepare();
				Looper.loop();
				Toast.makeText(WebService.this, "服务器关闭", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(WebService.this, "服务器关闭失败", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
