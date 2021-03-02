package service;

import java.util.List;

import entities.User;
import entities.Worktime;
import repository.WtrRepository;

public class WtrService {

	private WtrRepository repository;

	public WtrService() {
		this.repository = new WtrRepository();
	}

	public User login(String username, String password) {
		return repository.login(username, password);
	}

	public List<User> findUserByRole(String role) {
		return repository.findUserByRole(role);
	}

	public List<User> findUserByBossId(Integer bossid) {
		return repository.findUserByBossId(bossid);
	}

	public List<Worktime> findWorktimeByUserIdAndMonth(int year, int month, int userid) {
		return repository.findWorktimeByUserIdAndMonth(year, month, userid);
	}

	public void addNewWorkTime(Worktime worktime) {
		repository.addNewWorkTime(worktime);
	}

	public void updateWorkTime(Worktime worktime) {
		repository.updateWorkTime(worktime);
	}

	public void deleteWorkTime(int id) {
		repository.deleteWorkTime(id);
	}

	public Worktime findWorkTimeById(int id) {
		return repository.findWorkTimeById(id);
	}
}
