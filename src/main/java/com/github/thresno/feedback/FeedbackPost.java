package com.github.thresno.feedback;

import com.github.thresno.monitor.Event;

@FunctionalInterface
public interface FeedbackPost {	
	
	public void post(Event msg);
	
}
