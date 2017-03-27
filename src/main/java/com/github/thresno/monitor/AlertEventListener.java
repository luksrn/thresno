package com.github.thresno.monitor;

import org.springframework.context.ApplicationListener;

public interface AlertEventListener extends ApplicationListener<AlertEvent>{	
	
	void onApplicationEvent(AlertEvent event);
	
}
