package com.github.thresno.feedback;

import java.io.IOException;

import com.github.baloise.rocketchatrestclient.RocketChatClient;
import com.github.baloise.rocketchatrestclient.model.Message;
import com.github.baloise.rocketchatrestclient.model.Room;
import com.github.thresno.feedback.autoconfigure.RocketChatProperties;
import com.vdurmont.emoji.EmojiManager;

public class RocketChatFeedback implements FeedbackPost {

	private RocketChatProperties properties;
	
	public RocketChatFeedback(RocketChatProperties properties) {
		super();
		this.properties = properties;
	}

	@Override
	public void post(String feedback) {
		
		try{
	        RocketChatClient rc = new RocketChatClient( 
	        		properties.getHost(), properties.getUser(), properties.getPassword());
	
			Room room = new Room(properties.getRoom(), false);
	        Message msg = rc.getChatApi().postMessage(room, new Message(feedback));
	        msg.setEmojiAvatar(EmojiManager.getForAlias("smirk"));
	        
	        //rc.getChatApi().postMessage(room, msg);
		} catch (IOException e){
			System.out.println("Fail to connect with rocketchat.");
		}
	}

	
}
