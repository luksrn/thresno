package com.github.thresno.monitor;

import org.springframework.context.ApplicationEvent;

public class AlertEvent extends ApplicationEvent {
	
	enum Type {
		WARNING, INFO
	}

	private Type type;
	
	private String title;
	
	private String details;
	
	public AlertEvent(Object source) {
		super(source);
	}

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
	
	
	@Override
	public String toString() {
		return "[ " + getType() + " ] " + getTitle() + "\n" + getDetails();
	}
	
}
