package com.github.thresno.feedback;

import java.io.IOException;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.github.baloise.rocketchatrestclient.RocketChatClient;
import com.github.baloise.rocketchatrestclient.model.Message;
import com.github.baloise.rocketchatrestclient.model.Room;
import com.github.thresno.feedback.autoconfigure.RocketChatProperties;
import com.github.thresno.monitor.Event;
import com.vdurmont.emoji.EmojiManager;

public class RocketChatFeedback implements FeedbackPost , EnvironmentAware {

	private RocketChatProperties properties;
	
	private Environment env;
	
	public RocketChatFeedback(RocketChatProperties properties) {
		super();
		this.properties = properties;
	}

	@Override
	public void post(Event e) {
		
		try{
	        RocketChatClient rc = new RocketChatClient( 
	        		properties.getHost(), properties.getUser(), properties.getPassword());
	
			Room room = new Room(properties.getRoom(), false);
			String feedback = "*" + e.getTitle() + "*";
			feedback= feedback + "\n" + e.getDetails();
	        Message msg = new Message(feedback);
	        msg.setUsernameAlias( env.getProperty("thresno-bot-alias", String.class, "thresno"));
	        msg.setEmojiAvatar(EmojiManager.getForAlias("smirk"));
	        rc.getChatApi().postMessage(room, msg);
		} catch (IOException ex){
			System.out.println("Fail to connect with rocketchat.");
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

	
}
