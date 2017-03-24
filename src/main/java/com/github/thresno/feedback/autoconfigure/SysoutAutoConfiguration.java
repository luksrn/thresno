package com.github.thresno.feedback.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.github.thresno.feedback.FeedbackPost;
import com.github.thresno.feedback.RocketChatFeedback;
import com.github.thresno.feedback.autoconfigure.SysoutAutoConfiguration.SysoutConditional;

@Configuration
@Conditional(SysoutConditional.class)
public class SysoutAutoConfiguration {

	@Bean
	public FeedbackPost sysoutFeedback(){
		return System.out::println;
	}

	static class SysoutConditional extends AnyNestedCondition {
	
		public SysoutConditional() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}
	
		@ConditionalOnMissingBean(RocketChatFeedback.class)
		static class MissingRocketChatFeedbackBean {
		}
		
		
		@ConditionalOnProperty(prefix="feedback.console",value="enabled",havingValue="true")
		static class ConsoleEnabled {
		}
		
	}
}