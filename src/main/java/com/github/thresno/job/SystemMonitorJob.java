package com.github.thresno.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.thresno.monitor.AlertMonitor;

import oshi.SystemInfo;

@Component
public class SystemMonitorJob {
	
    private static final Logger log = LoggerFactory.getLogger(SystemMonitorJob.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	List<AlertMonitor> monitors;
	
	@Scheduled(fixedDelay = 15000)
    public void checkSystemHeath() {
 
        log.debug("Checking system status now {}", dateFormat.format(new Date()));
        
		SystemInfo si = new SystemInfo();
		// Initialize
	    si.getHardware();
	    si.getOperatingSystem();
	    
	    for(AlertMonitor monitor : monitors ){
	    	monitor.verify(si);
	    }
    }

}