package com.github.thresno.monitor;

import oshi.SystemInfo;

public interface Monitor {

	void verify(SystemInfo si);
}
