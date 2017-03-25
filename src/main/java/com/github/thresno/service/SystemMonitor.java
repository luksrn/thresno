package com.github.thresno.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.thresno.monitor.DiskPartitionsMonitor;

import oshi.SystemInfo;

@Component
public class SystemMonitor {
	
    private static final Logger log = LoggerFactory.getLogger(SystemMonitor.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	DiskPartitionsMonitor monitor;
	
	
	@Scheduled(fixedDelay = 15000)
    public void checkSystemHeath() {
        log.info("Checking system status now {}", dateFormat.format(new Date()));
        
		SystemInfo si = new SystemInfo();
		// Initialize
	    si.getHardware();
	    si.getOperatingSystem();
	    
	    monitor.verify(si);

	    
    }

}