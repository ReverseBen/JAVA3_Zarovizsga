package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import config.TemplateEngineUtil;
import entities.User;
import entities.Worktime;
import service.WtrService;

@WebServlet(urlPatterns = { "/view", "/delete_worktime", "/edit_worktime" })
public class ViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private WtrService service;

	public ViewServlet() {
		this.service = new WtrService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
		WebContext context = new WebContext(request, response, request.getServletContext());

		LocalDate today = LocalDate.now();
		int year = today.getYear();
		int month = today.getMonthValue();
		int userId = Integer.parseInt(request.getParameter("userid"));

		User user = ((User) request.getSession().getAttribute(LoginServlet.USER_PARAM));
		String userName = user.getUsername();

		Worktime wt = new Worktime();
		wt.setUserid(userId);

		Worktime editableWorktime = null;

		if (request.getServletPath().equals("/delete_worktime")) {
			int id = Integer.parseInt(request.getParameter("worktimeid"));
			service.deleteWorkTime(id);

		} else if (request.getServletPath().equals("/edit_worktime")) {
			int id = Integer.parseInt(request.getParameter("worktimeid"));
			editableWorktime = service.findWorkTimeById(id);
		}

		if (editableWorktime == null) {
			editableWorktime = new Worktime();
			editableWorktime.setUserid(userId);
		}

		List<Worktime> worktimes = service.findWorktimeByUserIdAndMonth(year, month, userId);
		int monthlyWorkedMinutes = 0;
		for (Worktime workt : worktimes) {
			monthlyWorkedMinutes += workt.getWorkedMinutes();
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		List<String> times = new ArrayList<>();
		LocalTime count = LocalTime.of(0, 0);
		times.add(count.format(dtf));
		count = count.plusMinutes(15);
		while (!count.equals(LocalTime.of(0, 0))) {
			times.add(count.format(dtf));
			count = count.plusMinutes(15);
		}

		context.setVariable("worker_edit", wt);
		context.setVariable("worktimes", worktimes);
		context.setVariable("userid", userId);
		context.setVariable("monthlyWorkedMinutes", monthlyWorkedMinutes);
		context.setVariable("view_edit", editableWorktime);
		context.setVariable("times", times);
		context.setVariable("username", userName);
		engine.process("view.html", context, response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String wstart = request.getParameter("wstart");
		String lstart = request.getParameter("lstart");
		String lend = request.getParameter("lend");
		String wend = request.getParameter("wend");
		String useridString = request.getParameter("userid");
		Integer userid = null;

		Worktime worktime = new Worktime();
		String parameter = request.getParameter("id");
		if (StringUtils.isNotBlank(parameter)) {
			worktime.setId(Integer.parseInt(parameter));
		}
		if (StringUtils.isNotBlank(useridString)) {
			userid = Integer.parseInt(useridString);
			worktime.setUserid(userid);
		}
		worktime.setDate(LocalDate.now());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
		worktime.setWstart(LocalTime.parse(wstart, dtf));
		worktime.setLstart(LocalTime.parse(lstart, dtf));
		worktime.setLend(LocalTime.parse(lend, dtf));
		worktime.setWend(LocalTime.parse(wend, dtf));

		if (Objects.isNull(worktime.getId())) {
			service.addNewWorkTime(worktime);

		} else {
			service.updateWorkTime(worktime);
		}

		response.sendRedirect("/wtr/view?userid=" + userid);

	}

}
