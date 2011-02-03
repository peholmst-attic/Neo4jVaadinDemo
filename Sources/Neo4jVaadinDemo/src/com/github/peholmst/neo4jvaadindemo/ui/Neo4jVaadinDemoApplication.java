package com.github.peholmst.neo4jvaadindemo.ui;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

public class Neo4jVaadinDemoApplication extends Application {

	private static final long serialVersionUID = -8865502065318150190L;

//	private transient Logger logger = Logger.getLogger(getClass().getName());

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
//		logger = Logger.getLogger(getClass().getName());
	}

	@SuppressWarnings("serial")
	@WebServlet(urlPatterns = "/*", name = "Neo4jVaadinDemoApplicationServlet")
	public static class Servlet extends AbstractApplicationServlet {

		@Override
		protected Application getNewApplication(HttpServletRequest request)
				throws ServletException {
			return new Neo4jVaadinDemoApplication();
		}

		@Override
		protected Class<? extends Application> getApplicationClass()
				throws ClassNotFoundException {
			return Neo4jVaadinDemoApplication.class;
		}
	}

	@Override
	public void init() {
		setTheme("neo4jvaadindemo");
		setMainWindow(new MainWindow());
	}

}
