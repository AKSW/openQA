package org.aksw.openqa.view.controller;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/admin/*"})
public class AuthFilter implements Filter {
     
    public AuthFilter() {
    }
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
         try { 
			// check whether session variable is set
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession ses = req.getSession(false);
			
			String reqURI = req.getRequestURI();
			if (!reqURI.contains("admin"))
			    chain.doFilter(request, response);
			else if(ses != null && ses.getAttribute("logged") != null)
				chain.doFilter(request, response);
			else
				res.sendRedirect(req.getContextPath() + "/login.xhtml");
         }
         catch(Throwable t) {
        	 System.out.println( t.getMessage());
         }
    } //doFilter
 
    @Override
    public void destroy() {
         
    }
}