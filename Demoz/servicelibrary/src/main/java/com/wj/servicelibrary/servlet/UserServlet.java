package com.wj.servicelibrary.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UserServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		String strResponse = "{name:'传智黄盖',email:'huanggai@itcast.cn',url:'image/user.png'}";
		byte[] b = strResponse.getBytes();
		resp.setContentLength(b.length);
		OutputStream out = resp.getOutputStream();
		out.write(b);
		out.close();
	}
}
