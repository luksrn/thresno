package com.github.thresno.monitor;

import oshi.SystemInfo;

public interface AlertMonitor {
	void verify(SystemInfo si);
}
