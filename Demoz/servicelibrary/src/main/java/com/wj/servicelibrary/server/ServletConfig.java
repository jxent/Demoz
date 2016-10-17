package com.wj.servicelibrary.server;

import com.wj.servicelibrary.servlet.AppServlet;
import com.wj.servicelibrary.servlet.CategoryServlet;
import com.wj.servicelibrary.servlet.DetailServlet;
import com.wj.servicelibrary.servlet.DownloadServlet;
import com.wj.servicelibrary.servlet.FlowDemosShowingServlet;
import com.wj.servicelibrary.servlet.GameServlet;
import com.wj.servicelibrary.servlet.HomeServlet;
import com.wj.servicelibrary.servlet.HotServlet;
import com.wj.servicelibrary.servlet.ImageServlet;
import com.wj.servicelibrary.servlet.RecommendServlet;
import com.wj.servicelibrary.servlet.SubjectServlet;
import com.wj.servicelibrary.servlet.UserServlet;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServletConfig {
	public static void config(ServletContextHandler handler) {
		handler.addServlet(new ServletHolder(new CategoryServlet()), "/category");
		handler.addServlet(new ServletHolder(new ImageServlet()), "/image");
		handler.addServlet(new ServletHolder(new RecommendServlet()), "/recommend");
		handler.addServlet(new ServletHolder(new SubjectServlet()), "/subject");
		handler.addServlet(new ServletHolder(new DetailServlet()), "/detail");
		handler.addServlet(new ServletHolder(new HomeServlet()), "/home");
		handler.addServlet(new ServletHolder(new AppServlet()), "/app");
		handler.addServlet(new ServletHolder(new GameServlet()), "/game");
		handler.addServlet(new ServletHolder(new DownloadServlet()), "/download");
		handler.addServlet(new ServletHolder(new UserServlet()), "/user");
		handler.addServlet(new ServletHolder(new HotServlet()), "/hot");
		handler.addServlet(new ServletHolder(new FlowDemosShowingServlet()), "/demos");
	}
}
