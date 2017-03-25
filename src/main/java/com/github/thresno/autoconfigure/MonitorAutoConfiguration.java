package com.github.thresno.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.github.thresno.autoconfigure.MonitorAutoConfiguration.DiskPartitionsMonitorConfiguration.DiskPartitionsCondition;
import com.github.thresno.monitor.DiskPartitionsAlertMonitor;
import com.github.thresno.monitor.MemoryAlertMonitor;

@Configuration
@ConditionalOnProperty(prefix = "thresno.monitor", name = "auto", havingValue = "true", matchIfMissing = true)
public class MonitorAutoConfiguration {
	
	@Configuration
	static class MemoryMonitorConfiguration {
		
		@Bean
		public MemoryAlertMonitor memoryAlertMonitor(){
			return new MemoryAlertMonitor();
		}
	}
	
	@Configuration
	@Conditional(DiskPartitionsCondition.class)
	static class DiskPartitionsMonitorConfiguration {
		
		@Bean
		public DiskPartitionsAlertMonitor diskPartitionsMonitor(){
			return new DiskPartitionsAlertMonitor();
		}
		
		
		static class DiskPartitionsCondition extends AnyNestedCondition {

			public DiskPartitionsCondition() {
				super(ConfigurationPhase.PARSE_CONFIGURATION);
			}
			
			@ConditionalOnProperty(prefix = "thresno.monitor.check-partition", name = "enabled")
			static class CheckPartitionEnabledProperty {
			}
			
			@ConditionalOnProperty(prefix = "thresno.monitor.check-partition", name = "threshold")
			static class ThresholdPartitionEnabledProperty {
			}
		}
	}

}
