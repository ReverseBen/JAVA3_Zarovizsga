package entities;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Worktime {

	Integer id;
	Integer userid;
	LocalDate date;
	LocalTime wstart;
	LocalTime lstart;
	LocalTime lend;
	LocalTime wend;

	public Worktime() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getWstart() {
		return wstart;
	}

	public void setWstart(LocalTime wstart) {
		this.wstart = wstart;
	}

	public LocalTime getLstart() {
		return lstart;
	}

	public void setLstart(LocalTime lstart) {
		this.lstart = lstart;
	}

	public LocalTime getLend() {
		return lend;
	}

	public void setLend(LocalTime lend) {
		this.lend = lend;
	}

	public LocalTime getWend() {
		return wend;
	}

	public void setWend(LocalTime wend) {
		this.wend = wend;
	}

	public int getWorkedMinutes() {
		Duration am = Duration.between(wstart, lstart);
		long amSeconds = am.getSeconds();

		Duration pm = Duration.between(lend, wend);
		long pmSeconds = pm.getSeconds();

		return (int) ((amSeconds + pmSeconds) / 60);
	}

	@Override
	public String toString() {
		return "Worktime [id=" + id + ", userid=" + userid + ", date=" + date + ", wstart=" + wstart + ", lstart="
				+ lstart + ", lend=" + lend + ", wend=" + wend + "]";
	}
}
