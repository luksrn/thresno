package com.github.thresno.feedback.autoconfigure;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix="feedback.rocketchat",ignoreUnknownFields=true)
public class RocketChatProperties {
	
	@NotEmpty
	private String host;
	
	@NotEmpty
	private String user;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	private String room;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
}
