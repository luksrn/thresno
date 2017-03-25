package com.github.thresno.monitor.listeners;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.github.baloise.rocketchatrestclient.RocketChatClient;
import com.github.baloise.rocketchatrestclient.model.Message;
import com.github.baloise.rocketchatrestclient.model.Room;
import com.github.thresno.autoconfigure.listeners.RocketChatProperties;
import com.github.thresno.monitor.AlertEvent;
import com.github.thresno.monitor.AlertEventListener;
import com.vdurmont.emoji.EmojiManager;

public class RocketChatListener implements AlertEventListener , EnvironmentAware {

	private static final Logger log = LoggerFactory.getLogger(RocketChatListener.class);
	
	private RocketChatProperties properties;
	
	private Environment env;
	
	public RocketChatListener(RocketChatProperties properties) {
		super();
		this.properties = properties;
	}

	@Override
	public void onAlertEvent(AlertEvent e) {
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
			log.error("Fail to connect with rocketchat.");
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}
	
}
