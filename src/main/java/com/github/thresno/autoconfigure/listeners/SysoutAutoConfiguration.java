package com.github.thresno.autoconfigure.listeners;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.github.thresno.autoconfigure.listeners.SysoutAutoConfiguration.SysoutConditional;
import com.github.thresno.monitor.AlertEventListener;
import com.github.thresno.monitor.listeners.RocketChatListener;
import com.github.thresno.monitor.listeners.SysoutListener;

@Configuration
@Conditional(SysoutConditional.class)
public class SysoutAutoConfiguration {

	@Bean
	public AlertEventListener sysoutFeedback(){
		return new SysoutListener();
	}

	static class SysoutConditional extends AnyNestedCondition {
	
		public SysoutConditional() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}
	
		@ConditionalOnMissingBean(RocketChatListener.class)
		static class MissingRocketChatFeedbackBean {
		}
		
		
		@ConditionalOnProperty(prefix="feedback.console",value="enabled",havingValue="true")
		static class ConsoleEnabled {
		}
		
	}
}