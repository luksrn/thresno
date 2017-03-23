package com.github.thresno.feedback;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(value={RockchatFeedback.class})
@ConditionalOnProperty(prefix="feedback.console",value="enabled",havingValue="true")
public class SysoutFeedback implements FeedbackPost {

	@Override
	public void post(String msg) {
		System.out.println(msg);
	}
}
