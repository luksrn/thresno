package com.github.thresno.monitor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.github.thresno.service.SystemInformationFormater;

import oshi.SystemInfo;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

public class DiskPartitionsMonitor implements Monitor, EnvironmentAware , ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;
	
	private Environment env;

	@Override
	public void verify(SystemInfo si) {
		OperatingSystem os = si.getOperatingSystem();

		StringBuilder builder = new StringBuilder();

		OSFileStore[] fsArray = os.getFileSystem().getFileStores();
		List<String> checkFileStoredVisited = new ArrayList<String>( fsArray.length );
		for (OSFileStore fs : fsArray) {

			if( checkFileStoredVisited.contains(fs.getUUID()) ){
				// Skip Docker Mount
				continue;
			}
			checkFileStoredVisited.add(fs.getUUID());

			long usable = fs.getUsableSpace();
			long total = fs.getTotalSpace();

			double percentualFree = 100d * usable / total;

			Event.Type eventType = typeOfEvent(percentualFree);
			if ( eventType != null ) {

				SystemInformationFormater.printDisks(si.getHardware().getDiskStores(), builder);
				SystemInformationFormater.printFileSystem(os.getFileSystem(), builder);
				
				Event e = new Event();
				e.setType(eventType);
				e.setTitle(String.format(" %.1f%% space free at %s mounted at %s%n", 100d * usable / total,fs.getVolume(), fs.getMount()));
				e.setDetails( builder.toString() );
				applicationEventPublisher.publishEvent( e );
				
				builder = new StringBuilder();
			}
		}	
 
	}
	
	private Event.Type typeOfEvent(double percentualFree){
		if(env.containsProperty("thresno.monitor.check-partition.threshold")){

			Integer threshold = env.getProperty("thresno.monitor.check-partition.threshold", Integer.class);
			
			if( percentualFree < threshold.doubleValue() ){
				return Event.Type.WARNING;
			}
			return null;
		}
		
		return Event.Type.INFO;
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
