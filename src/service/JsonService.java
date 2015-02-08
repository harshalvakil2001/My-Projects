package service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.JsonUtil;

/**
 * Servlet implementation class JsonService
 */
@WebServlet("/JsonService")
public class JsonService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public JsonService() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			JsonUtil util = JsonUtil.getInstance();
			PrintWriter out = response.getWriter();
			response.setHeader("content-type", "text/html");
			String db = "database.mdb";
			String str = util.getJson(db);
			out.print(str);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
