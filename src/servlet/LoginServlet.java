package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import config.TemplateEngineUtil;
import entities.User;
import service.WtrService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String LOGIN_ERROR_PARAM = "auth_error";
	public static final String USER_PARAM = "user";
	public static final String USERNAME_PARAM = "username";
	public static final String PASSWORD_PARAM = "password";
	private static final String EMPTY_FIELDS = "empty_field";
	private static final String TOO_MANY_CHAR = "too_many_char";

	private WtrService service;

	public LoginServlet() {
		this.service = new WtrService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
		WebContext context = new WebContext(request, response, request.getServletContext());

		context.setVariable(TOO_MANY_CHAR, request.getParameter(TOO_MANY_CHAR));
		context.setVariable(EMPTY_FIELDS, request.getParameter(EMPTY_FIELDS));
		context.setVariable(LOGIN_ERROR_PARAM, request.getParameter(LOGIN_ERROR_PARAM));
		engine.process("login.html", context, response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter(USERNAME_PARAM);
		String password = request.getParameter(PASSWORD_PARAM);
		User user = service.login(username, password);

		System.out.println("user: " + user);

		if (StringUtils.isAnyBlank(username) || StringUtils.isAnyBlank(password)) {
			response.sendRedirect("/wtr/login?" + EMPTY_FIELDS + "=true");
			return;

		} else if (username.length() >= 10 || password.length() >= 10) {
			response.sendRedirect("/wtr/login?" + TOO_MANY_CHAR + "=true");
			return;
		}

		String redirectUrl = "";
		if (user != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute(USER_PARAM, user);
			session.setMaxInactiveInterval(600);

			if (user.hasAdminRole()) {
				redirectUrl = "/wtr/boss";
			} else {
				redirectUrl = "/wtr/worker";
			}
		} else {
			redirectUrl = "/wtr/login?" + LOGIN_ERROR_PARAM + "=true";
		}
		response.sendRedirect(redirectUrl);
	}

}
