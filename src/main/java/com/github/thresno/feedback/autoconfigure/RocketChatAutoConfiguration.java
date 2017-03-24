package com.github.thresno.feedback.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.github.thresno.feedback.FeedbackPost;
import com.github.thresno.feedback.RocketChatFeedback;
import com.github.thresno.feedback.autoconfigure.RocketChatAutoConfiguration.RocketChatCondition;

@Configuration
@Conditional(RocketChatCondition.class)
@EnableConfigurationProperties(RocketChatProperties.class)
public class RocketChatAutoConfiguration {

	private RocketChatProperties properties;
	
	public RocketChatAutoConfiguration(RocketChatProperties properties) {
		super();
		this.properties = properties;
	}

	@Bean
	public FeedbackPost rocketChatFeedbackPost(){
		return new RocketChatFeedback(properties);
	}
	
	static class RocketChatCondition extends AllNestedConditions {

		public RocketChatCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty(prefix = "feedback.rocketchat", name = "host")
		static class HostProperty {
		}
		
		@ConditionalOnProperty(prefix = "feedback.rocketchat", name = "user")
		static class UserProperty {
		}
		
		@ConditionalOnProperty(prefix = "feedback.rocketchat", name = "password")
		static class PasswordProperty {
		}
		
		@ConditionalOnProperty(prefix = "feedback.rocketchat", name = "room")
		static class RoomProperty {
		}
	}
}
