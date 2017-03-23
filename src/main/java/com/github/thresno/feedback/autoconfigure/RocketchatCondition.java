package com.github.thresno.feedback.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import com.github.thresno.feedback.RockchatProperties;

public class RocketchatCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		
		ConditionMessage.Builder message = ConditionMessage.forCondition("Rocketchat properties Condition");

		try {
			Environment env = context.getEnvironment();
			
			String [] properties = new String []{ 
					RockchatProperties.HOST,
					RockchatProperties.USER,
					RockchatProperties.PASSWORD,
					RockchatProperties.CHAT_ROOM };
			
			for ( String p : properties ){
				if(StringUtils.isEmpty(env.getRequiredProperty(p))){
					throw new IllegalStateException();
				}
			}
			return ConditionOutcome.match(
					message.found("Rocketchat properties")
					.items((Object[])properties));
			
		} catch (IllegalStateException e){
			return ConditionOutcome.noMatch(
					message.didNotFind(	
							"Rocketchat properties"
					).atAll());
		}
	}
}
