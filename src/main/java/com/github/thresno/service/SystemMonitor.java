package com.github.thresno.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.thresno.feedback.FeedbackPost;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

@Component
public class SystemMonitor {
	
    private static final Logger log = LoggerFactory.getLogger(SystemMonitor.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	Environment env;
	
	@Autowired
	FeedbackPost feedbackPost;
	
	@Scheduled(fixedDelay = 15000)
    public void checkSystemHeath() {
        log.info("Checking system status now {}", dateFormat.format(new Date()));
        
		SystemInfo si = new SystemInfo();

	    HardwareAbstractionLayer hal = si.getHardware();
	    OperatingSystem os = si.getOperatingSystem();
	    
	    StringBuilder builder = new StringBuilder();
	    builder.append("Hostname: ").append( os.getNetworkParams().getHostName()  ).append("\n");
	    
	    boolean warnOnDiskUsage = checkDiskUsage(hal, os, builder);

	    if( warnOnDiskUsage ){
	    	feedbackPost.post(builder.toString());
	    }
    }

	private boolean checkDiskUsage(HardwareAbstractionLayer hal, OperatingSystem os, StringBuilder builder) {
		if(env.containsProperty("check-partition-threshold")){
	    	Integer threshold = env.getProperty("check-partition-threshold", Integer.class);
	    	
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
              
	    		if ( percentualFree < threshold.doubleValue() ){
	    			builder.append("Alert: ")
	    						.append(String.format(" %.1f%% space free at %s mounted at %s%n", 100d * usable / total,fs.getVolume(), fs.getMount()));
	    			
	    			SystemInformationFormater.printDisks(hal.getDiskStores(), builder);
	    			SystemInformationFormater.printFileSystem(os.getFileSystem(), builder);
	    			return true;
	    		}
             
          }
	    }
		return false;
	}
	

}