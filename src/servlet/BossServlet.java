package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import config.TemplateEngineUtil;
import entities.User;
import service.WtrService;

@WebServlet("/boss")
public class BossServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private WtrService service;

	public BossServlet() {
		this.service = new WtrService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
		WebContext context = new WebContext(request, response, request.getServletContext());

		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute(LoginServlet.USER_PARAM);
		String username = user.getUsername();

		context.setVariable("users", service.findUserByBossId(user.getId()));
		context.setVariable("username", username);
		engine.process("boss.html", context, response.getWriter());
	}
}
