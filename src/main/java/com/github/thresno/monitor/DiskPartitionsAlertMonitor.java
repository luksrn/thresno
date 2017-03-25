package com.github.thresno.monitor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import oshi.SystemInfo;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public class DiskPartitionsAlertMonitor implements AlertMonitor, EnvironmentAware , ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;
	
	private Environment env;

	@Override
	public void verify(SystemInfo si) {
		OperatingSystem os = si.getOperatingSystem();

		OSFileStore[] fsArray = os.getFileSystem().getFileStores();
		List<String> checkFileStoredVisited = new ArrayList<String>( fsArray.length );

		StringBuilder builder = new StringBuilder();

		for (OSFileStore fs : fsArray) {

			if( checkFileStoredVisited.contains(fs.getUUID()) ){
				// Skip Docker Mount
				continue;
			}
			checkFileStoredVisited.add(fs.getUUID());

			long usable = fs.getUsableSpace();
			long total = fs.getTotalSpace();

			double percentualFree = 100d * usable / total;

			AlertEvent.Type eventType = resolveTypeOfEvent(percentualFree);
			if ( eventType != null && isFileStoreMonitored(fs)) {

				logFileStoreStatus( fs , builder);
				
				
				AlertEvent e = new AlertEvent();
				e.setType(eventType);
				e.setTitle(String.format("File Store Alert: %.1f%% space free at %s mounted at %s", 100d * usable / total,fs.getVolume(), fs.getMount()));
				e.setDetails( builder.toString() );
				
				applicationEventPublisher.publishEvent( e );
				
				builder = new StringBuilder();
			}
		}	
 
	}
	
	private boolean isFileStoreMonitored(OSFileStore fs){
		return !fs.getType().equals("");
	}
	
	private AlertEvent.Type resolveTypeOfEvent(double percentualFree){
		if(env.containsProperty("thresno.monitor.check-partition.threshold")){

			Integer threshold = env.getProperty("thresno.monitor.check-partition.threshold", Integer.class);
			
			if( percentualFree < threshold.doubleValue() ){
				return AlertEvent.Type.WARNING;
			}
			return null;
		}
		
		return AlertEvent.Type.INFO;
	}
 
    
    private void logFileStoreStatus(OSFileStore fs, StringBuilder builder){
    	long usable = fs.getUsableSpace();
        long total = fs.getTotalSpace();
        builder.append(String.format("** %s (%s) [%s] %s of %s free (%.1f%%) is %s and is mounted at %s%n", fs.getName(),
                fs.getDescription().isEmpty() ? "file system" : fs.getDescription(), fs.getType(),
                FormatUtil.formatBytes(usable), FormatUtil.formatBytes(fs.getTotalSpace()), 100d * usable / total,
                fs.getVolume(), fs.getMount()));
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
