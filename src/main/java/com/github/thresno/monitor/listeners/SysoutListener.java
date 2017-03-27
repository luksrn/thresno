package com.github.thresno.monitor.listeners;

import com.github.thresno.monitor.AlertEvent;
import com.github.thresno.monitor.AlertEventListener;

public class SysoutListener implements AlertEventListener {

	@Override
	public void onApplicationEvent(AlertEvent e) {
		System.out.println( e );
	}
}
