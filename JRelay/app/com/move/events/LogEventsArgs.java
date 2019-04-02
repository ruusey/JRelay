package com.move.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEventsArgs {

	private String message;
	private Date timestamp;
	private static final DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	public LogEventsArgs(String message) {
		super();
		this.message = message;
		this.timestamp = new Date();
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "[" + sdf.format(this.timestamp) + "]";
	}
	public String messageWithTimestamp() {
		return this.toString()+ " "+this.message;
	}
	

}
