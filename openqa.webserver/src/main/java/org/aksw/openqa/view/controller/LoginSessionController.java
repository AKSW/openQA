package org.aksw.openqa.view.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name="loginSessionController")
@SessionScoped
public class LoginSessionController {
	
	private String password;
	private String login;
	private boolean logged;
	
	public String login() {
		if(login != null && login.equals("admin") && password != null  && password.equals("nimda")) {
			HttpSession session = (HttpSession) FacesContext.
	        	getCurrentInstance().
	        	getExternalContext().
	        	getSession(false);
			 
			session.setAttribute("logged", true);
			 
			return "/admin/manager.xhtml?faces-redirect=true";
		}
		
		FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Invalid Login!",
                    "Please Try Again!"));
		
		return "login";
	}
	
	public String logout() {
		 FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	     return "/index.xhtml?faces-redirect=true";
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public boolean isLogged() {
		return logged;
	}
}
