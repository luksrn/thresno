package com.github.thresno.monitor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.github.thresno.feedback.FeedbackPost;

@Component
public class EventConsumer {

	@Autowired
	List<FeedbackPost> feedbackPost;
	
	@EventListener
	public void processEvent(Event e){
		feedbackPost.forEach( i -> i.post(e) );
	}

}
