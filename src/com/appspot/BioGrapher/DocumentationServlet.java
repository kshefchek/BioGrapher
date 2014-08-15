package com.appspot.BioGrapher;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class DocumentationServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException, ServletException {
        
        String about = "Documentation coming soon!&nbsp;&nbsp;Please see the homepage for general instructions";
		req.setAttribute("dcontent", about);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("template.jsp");
		dispatcher.forward(req, resp);

        
    }
}