package com.appspot.BioGrapher;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class PrototypeServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException, ServletException {
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("prototype.jsp");
		dispatcher.forward(req, resp);

        
    }
}