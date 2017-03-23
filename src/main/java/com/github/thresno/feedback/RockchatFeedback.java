package com.github.thresno.feedback;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.github.baloise.rocketchatrestclient.RocketChatClient;
import com.github.baloise.rocketchatrestclient.model.Message;
import com.github.baloise.rocketchatrestclient.model.Room;
import com.github.thresno.feedback.autoconfigure.RocketchatCondition;
import com.vdurmont.emoji.EmojiManager;

@Component
@Conditional(RocketchatCondition.class)
public class RockchatFeedback implements FeedbackPost {

	@Autowired
	Environment env;
	
	@Override
	public void post(String feedback) {
		
		try{
	        String serverUrl = env.getProperty(RockchatProperties.HOST);
	        String user = env.getProperty(RockchatProperties.USER);
	        String password = env.getProperty(RockchatProperties.PASSWORD);
	        String roomPublish = env.getProperty(RockchatProperties.CHAT_ROOM);
	        
	        RocketChatClient rc = new RocketChatClient(serverUrl, user, password);
	
			Room room = new Room(roomPublish, false);
	        Message msg = rc.getChatApi().postMessage(room, new Message(feedback));
	        msg.setEmojiAvatar(EmojiManager.getForAlias("smirk"));
	        
	        rc.getChatApi().postMessage(room, msg);
		} catch (IOException e){
			System.out.println("Fail to connect with rocketchat.");
		}
	}

	
}
