package netty.io.spring.server.domain;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import netty.io.spring.server.exception.UserNotFoundException;
import netty.io.spring.server.repository.ChannelRepository;

@Slf4j
public class User {

	public static final AttributeKey<User> USER_ATTRIBUTE_KEY = AttributeKey.newInstance("USER");

	@Getter
	private final String username;
	private final Channel channel;

	private User(String username, Channel channel) {
		this.username = username;
		this.channel = channel;
	}

	public static User of(@NonNull String loginCommand, @NonNull Channel channel) {
		if (loginCommand.startsWith("login|")) {
			return new User(loginCommand.trim().substring("login|".length()), channel);
		}

		throw new IllegalArgumentException("loginCommand [" + loginCommand + "] can not be accepted");
	}

	public void login(ChannelRepository channelRepository, Channel channel) {
		channel.attr(USER_ATTRIBUTE_KEY).set(this);
		channelRepository.put(this.username, channel);
		log.info("channelSize={}", channelRepository.size());
	}

	public void logout(ChannelRepository channelRepository, Channel channel) {
		channel.attr(USER_ATTRIBUTE_KEY).getAndSet(null);
		channelRepository.remove(this.username);
	}

	public static User current(Channel channel) {
		User user = channel.attr(USER_ATTRIBUTE_KEY).get();
		if (user == null) {
			throw new UserNotFoundException();
		}
		return user;
	}

	public void tell(Channel targetChannel, @NonNull String username, @NonNull String message) {
		if (targetChannel != null) {
			targetChannel.write("[".concat(this.username).concat("] "));
			targetChannel.writeAndFlush(message + System.lineSeparator());
			this.channel.writeAndFlush("The message was sent to [" + username + "] successfully." + System.lineSeparator());
		} else {
			this.channel.writeAndFlush("No user named with [" + username + "]." + System.lineSeparator());
		}
	}

}
