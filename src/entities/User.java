package entities;

import java.util.ArrayList;
import java.util.List;

public class User {

	private Integer id;
	private String name;
	private String username;
	private String password;
	Integer bossid;

	private List<Role> roles;

	public User() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Integer getBossid() {
		return bossid;
	}

	public void setBossid(Integer bossid) {
		this.bossid = bossid;
	}

	public void addRole(Role role) {
		if (this.roles == null) {
			this.roles = new ArrayList<>();
		}
		this.roles.add(role);
	}

	public boolean hasRole(String role) {
		if (this.roles == null) {
			return false;
		}
		return this.roles.stream().anyMatch(r -> role.equals(r.getName()));
	}

	public boolean hasAdminRole() {
		if (this.roles == null) {
			return false;
		}
		return this.roles.stream().anyMatch(role -> "ADMIN".equals(role.getName()));
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password + ", roles="
				+ roles + "]";
	}
}
