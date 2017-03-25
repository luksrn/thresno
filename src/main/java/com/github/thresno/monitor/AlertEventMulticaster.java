package com.github.thresno.monitor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AlertEventMulticaster {
	
	private static final Logger log = LoggerFactory.getLogger(AlertEventMulticaster.class);
	
	@Autowired
	List<AlertEventListener> listeners;
	
	@EventListener
	public void processEvent(AlertEvent e){
		
		for ( AlertEventListener listener : listeners ){		
			try {
				listener.onAlertEvent(e);
			} catch (Exception ex){
				log.error("Error calling AlertEventListener {}", listener.getClass().getName() );
				ex.printStackTrace();
			}
		}
	}
}
