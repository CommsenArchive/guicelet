package com.commsen.guicelet.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commsen.guicelet.template.Constants;
import com.google.inject.Singleton;

@Singleton
public class TemplateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(TemplateServlet.class);

	private Map<String, String> templates = new HashMap<String, String>();
	private String jspFolder = "/WEB-INF/jsp";
	private String jspTemplate = "template.jsp";
	
	@Override
	public void init() throws ServletException {
		String jspDir = getServletConfig().getInitParameter(Constants.PARAM_JSP_FOLDER);
		if (jspDir != null) {
			jspDir = jspDir.trim();
			if (jspDir.isEmpty()) {
				if (log.isWarnEnabled()) {
					log.warn("Init parameter " + Constants.PARAM_JSP_FOLDER + " is empty! Using the default JSP folder: " + jspFolder);
				}
			} else { 
				if (folderExists(getServletContext().getRealPath(jspDir))) {
					this.jspFolder = jspDir;
				} else {
					if (log.isWarnEnabled()) {
						log.warn("Folder " + jspDir + " does not exists! Using the default JSP folder: " + jspFolder);
					}
				}
			}
		} else {
			if (log.isInfoEnabled()) {
				log.info("Init parameter " + Constants.PARAM_JSP_FOLDER + " not provided! Using the default JSP folder: " + jspFolder);
			}
			
		}
		
		String template = getServletConfig().getInitParameter(Constants.PARAM_TEMPLATE);
		if (template != null) {
			template = template.trim();
			if (template.isEmpty()) {
				template = this.jspTemplate;
				if (log.isWarnEnabled()) {
					log.warn("Init parameter " + Constants.PARAM_TEMPLATE + " is empty! Using the default template name: " + jspTemplate);
				}
			}
		} else {
			template = this.jspTemplate;
			if (log.isInfoEnabled()) {
				log.info("Init parameter " + Constants.PARAM_TEMPLATE + " not provided! Using the default template name: " + jspTemplate);
			}
			
		}

 		if (fileExists(getServletContext().getRealPath(template))) {
			this.jspTemplate = template;
 		} else {
			if (log.isErrorEnabled()) {
				log.error("File " + jspTemplate + " does not exists! " );
			}
		}

		
		@SuppressWarnings("unchecked")
		Enumeration<String> names = getServletConfig().getInitParameterNames();
		while (names.hasMoreElements()) {
			String param = names.nextElement();
			String paramName;
			String paramValue;
			if (param.startsWith(Constants.PARAM_TEMPLATE_PREFIX)) {
				paramName = param.substring(Constants.PARAM_TEMPLATE_PREFIX.length());
				paramValue = getServletConfig().getInitParameter(param);
				templates.put(paramName, paramValue);
				if (log.isDebugEnabled()) {
					log.debug("Param " + paramName + " set to " + paramValue + " !" );
				}
			}
		}

	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}
	

	protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestPath =  request.getRequestURI();
		String contextPath = request.getServletPath();
		if (!requestPath.endsWith("/")) requestPath += "/";
		if (!requestPath.startsWith(contextPath)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String path = requestPath.substring(contextPath.length());
		if (!folderExists(getServletContext().getRealPath(jspFolder + path))) {
 			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		for (Map.Entry<String, String> template : templates.entrySet()) {
			String templatePath = jspFolder + path + template.getValue(); 
			if (fileExists(getServletContext().getRealPath(templatePath))) {
	 			request.setAttribute(Constants.PARAM_TEMPLATE_PREFIX + template.getKey(), templatePath);
			}
		}
		
		
		request.getRequestDispatcher(jspTemplate).include(request, response);
		
	}
	
	
	private boolean folderExists(String name) throws ServletException {
		File f = new File(name);
		if (f.exists() && f.isDirectory()) {
			return true;
		}
		return false;
	}

	private boolean fileExists(String name) throws ServletException {
		File f = new File(name);
		if (f.exists() && f.isFile()) {
			return true;
		}
		return false;
	}

}
