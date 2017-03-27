package com.github.thresno.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.github.thresno.autoconfigure.MonitorAutoConfiguration.DiskPartitionsMonitorConfiguration.DiskPartitionsCondition;
import com.github.thresno.autoconfigure.MonitorAutoConfiguration.MemoryMonitorConfiguration.MemoryConditions;
import com.github.thresno.monitor.DiskPartitionsAlertMonitor;
import com.github.thresno.monitor.MemoryAlertMonitor;

@Configuration
@ConditionalOnProperty(prefix = "thresno.monitor", name = "auto", havingValue = "true", matchIfMissing = true)
public class MonitorAutoConfiguration {
	
	@Configuration
	@Conditional(MemoryConditions.class)
	static class MemoryMonitorConfiguration {
		
		@Bean
		public MemoryAlertMonitor memoryAlertMonitor(){
			return new MemoryAlertMonitor();
		}
		
		static class MemoryConditions extends AnyNestedCondition {
			public MemoryConditions() {
				super(ConfigurationPhase.PARSE_CONFIGURATION);
			}
			
			@ConditionalOnProperty(prefix = "thresno.monitor.check-memory", name = "enabled")
			static class CheckMemoryEnabledProperty {
			}
			
			@ConditionalOnProperty(prefix = "thresno.monitor.check-memory", name = "threshold")
			static class ThresholdMemoryEnabledProperty {
			}
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
