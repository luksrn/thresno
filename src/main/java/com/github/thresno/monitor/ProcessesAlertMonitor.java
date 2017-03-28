package com.github.thresno.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessSort;
import oshi.util.FormatUtil;

public class ProcessesAlertMonitor implements AlertMonitor , EnvironmentAware , ApplicationEventPublisherAware {

	private static final Logger log = LoggerFactory.getLogger(ProcessesAlertMonitor.class);

	private ApplicationEventPublisher applicationEventPublisher;

	private Environment env;
	
	private Map<Integer,OSProcess> lastAccessInformation = new HashMap<>();
	
	@Override
	public void verify(SystemInfo si) {
		GlobalMemory globalMemory = si.getHardware().getMemory();
		
		printProcesses(si.getOperatingSystem(), globalMemory);		
		
	}
	
	public void printProcesses(OperatingSystem os, GlobalMemory memory) {
		
		List<OSProcess> procs = Arrays.asList(os.getProcesses(0, ProcessSort.CPU));
		
		
		List<Integer> pids = Arrays.asList(env.getProperty("thresno.monitor.check-process.by-pid", Integer[].class));
		
		if( log.isDebugEnabled() ){
			log.debug("Checking for PIDs = " + pids );
		}
		
		List<Integer> pidsFound = new ArrayList<Integer>( pids.size() );
		
		for (int i = 0; i < procs.size(); i++) {
			OSProcess p = procs.get(i);
			if( pids.contains(p.getProcessID())){
				pidsFound.add(p.getProcessID());
				lastAccessInformation.put(p.getProcessID(), p);
			}
		}
		
		
		if ( pids.size() != pidsFound.size() ){
			
			int diff = pids.size() - pidsFound.size();
			
			StringBuilder builder = new StringBuilder();
			
			boolean hasMessage = false;
			for ( Integer pid : pids ){
				if( pidsFound.contains(pid)){
					continue;
				}
				OSProcess processDead = lastAccessInformation.remove(pid);
				if( processDead == null ){
					continue;
				}
				hasMessage = true;
				logProcessesAlert(processDead,builder);
			}
			if(hasMessage){
				AlertEvent e = new AlertEvent(pids);
				e.setType(AlertEvent.Type.WARNING);
				e.setTitle(diff +" process isn't running. check out last checking state");
				e.setDetails(builder.toString());
				
				applicationEventPublisher.publishEvent(e);
				
			}
			
		}
		
    }
	
	private void logProcessesAlert(OSProcess process, StringBuilder builder){
		
		builder.append("PID: ").append(process.getProcessID()).append("\n");
		builder.append("Name: ").append(process.getName()).append("\n");
		builder.append("Command line: ").append(process.getCommandLine()).append("\n");		
		builder.append("User: ").append(process.getUser()).append("\n");
		builder.append("%CPU: ").append( String.format("%5.1f", 100d * (process.getKernelTime() + process.getUserTime()) / process.getUpTime() )).append("\n");		
		builder.append("Resident Set Size (RSS): ").append( FormatUtil.formatBytes(process.getResidentSetSize()) ).append("\n");
		builder.append("Virtual Memory Size (VSZ): ").append( FormatUtil.formatBytes(process.getVirtualSize()) ).append("\n");
		builder.append("Uptime: " ).append(FormatUtil.formatElapsedSecs( process.getUpTime() )).append("\n");
		
	}

	@Override
	public void setEnvironment(Environment environment) {
		env = environment;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

}
