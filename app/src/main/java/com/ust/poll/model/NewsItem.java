// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.model;

public class NewsItem {

	private String headline;
	private String reporterName;
	private String date;
	private String pollId;
	private String allOpt;

	public String getpollID() {
		return pollId;
	}

	public void setpollID(String pollId) {
		this.pollId = pollId;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getallOpt() {
		return allOpt;
	}

	public void setallOpt(String allOpt) {
		this.allOpt = allOpt;
	}

	@Override
	public String toString() {
		return "[ headline=" + headline + ", reporter Name=" + reporterName + " , date=" + date + " , Option=" + allOpt +"]";
	}
}