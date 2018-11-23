package com.ragavan.sample.mbeans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ragavan.sample.dao.UserDAO;
import com.ragavan.sample.dao.exception.DataAccessException;
import com.ragavan.sample.models.User;

@ManagedBean
@RequestScoped
public class LoginBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@ManagedProperty("#{userDAO}")
	private UserDAO userDAO;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String password;

	public String login() {
		User u;
		//String url = "index?faces-redirect=true";
		try {
			u = userDAO.retrieveUserByEmail(getEmail());
			if (u.getPassword().equals(getPassword())) {
				FacesContext context = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				session.setAttribute("userSession", u);
				return "dashboard?faces-redirect=true";
			}
			else{
				String errorMessage = "Invalid Login";
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage);
				FacesContext.getCurrentInstance().addMessage("form:msgId", message);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Invalid Email";
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage);
			FacesContext.getCurrentInstance().addMessage("form:email", message);
		}

		return null;
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		if (session.getAttribute("userSession") != null) {
			session.invalidate();
		}
		return "index?faces-redirect=true";
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}