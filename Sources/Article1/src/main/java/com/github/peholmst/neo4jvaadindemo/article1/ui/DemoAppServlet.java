package com.github.peholmst.neo4jvaadindemo.article1.ui;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Vaadin application servlet for {@link DemoApp}. Please note that this servlet
 * uses the new Servlet 3.0 annotation.
 * 
 * @author Petter Holmström
 */
@WebServlet(urlPatterns = "/*")
public class DemoAppServlet extends AbstractApplicationServlet {

    @Override
    protected Application getNewApplication(HttpServletRequest request) throws ServletException {
        return new DemoApp();
    }

    @Override
    protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
        return DemoApp.class;
    }
}
