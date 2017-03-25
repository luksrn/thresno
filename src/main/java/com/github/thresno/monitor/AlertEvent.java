package com.github.thresno.monitor;

public class AlertEvent {
	
	enum Type {
		WARNING, INFO
	}

	private Type type;
	
	private String title;
	
	private String details;
	
	/** System time when the event happened */
	private final long timestamp = System.currentTimeMillis();

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
		return "[ " + getType() + " ] " + getTitle() + "\n" + getDetails();
	}
	
}
