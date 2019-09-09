package com.lp.BOBService.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.tc.logging.Location;

/**
 * Servlet implementation class AppInit
 */
public class AppInit extends HttpServlet {
	private static final long serialVersionUID = 8638158991117898889L;
	private static final Location loc = Location.getLocation(AppInit.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AppInit() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// System.out.println("Portal Sevice configuration Data Initialise.......");
		// SimpleLogger.log(Severity.INFO,Category.SYS_SERVER,loc,"","Portal Sevice
		// configuration Data Initialise.......");
		String prefix = getServletContext().getRealPath("\\");
		String file = null;
		String filePath = null;
		try {
			file = getInitParameter("configurationLocation");
			filePath = prefix + file;

			// Properties p =
			// PortalServiceUtils.readProperties("C:\\TomcatServer7\\webapps\\BOBService\\WEB-INF\\app.properties");
			// SimpleLogger.log(Severity.INFO,Category.SYS_SERVER,loc,"","KMSETLOCATION ::
			// "+ p.getProperty("KMSETLOCATION"));

			// Properties p1 =
			// PortalServiceUtils.readProperties(p.getProperty("KMSETLOCATION"));

		} catch (Exception e) {
			// SimpleLogger.traceThrowable(Severity.ERROR, loc, "", e);

		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
