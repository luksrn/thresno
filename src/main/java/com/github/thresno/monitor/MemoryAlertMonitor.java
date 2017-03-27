package com.github.thresno.monitor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.util.FormatUtil;

public class MemoryAlertMonitor implements AlertMonitor, EnvironmentAware , ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;
	
	private Environment env;

	@Override
	public void verify(SystemInfo si) {

		GlobalMemory globalMemory = si.getHardware().getMemory();
		
		AlertEvent.Type eventType = resolveTypeOfEvent(globalMemory.getAvailable());
		if ( eventType != null ) {
			StringBuilder builder = new StringBuilder();
			
			logMemory(globalMemory, builder);
			
			AlertEvent e = new AlertEvent(globalMemory);
			e.setType(eventType);
			e.setTitle(String.format("Memory alert."));
			e.setDetails( builder.toString() );
			
			applicationEventPublisher.publishEvent( e );
			
		}
	}

	private void logMemory(GlobalMemory memory, StringBuilder builder) {
        builder.append("Memory RAM: " + FormatUtil.formatBytes(memory.getAvailable()) + "/" + FormatUtil.formatBytes(memory.getTotal())).append("\n");
        builder.append("Swap used: " + FormatUtil.formatBytes(memory.getSwapUsed()) + "/" + FormatUtil.formatBytes(memory.getSwapTotal()));
        // Java HEAP?
    }
	
	private AlertEvent.Type resolveTypeOfEvent(double percentualFree){
		if(env.containsProperty("thresno.monitor.check-memory.threshold")){

			Long threshold = env.getProperty("thresno.monitor.check-memory.threshold", Long.class);
			
			if( percentualFree < threshold.doubleValue() ){
				return AlertEvent.Type.WARNING;
			}
			return null;
		}
		
		return AlertEvent.Type.INFO;
	}
	
	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

}
