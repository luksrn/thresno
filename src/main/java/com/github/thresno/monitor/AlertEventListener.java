package com.github.thresno.monitor;

@FunctionalInterface
public interface AlertEventListener {	
	
	public void onAlertEvent(AlertEvent msg);
	
}
