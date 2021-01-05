package netty.io.spring.server.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.Channel;

// Channel Repository using HashMap
public class ChannelRepository {

	private ConcurrentMap<String, Channel> channelCache = new ConcurrentHashMap<>();

	public void put(String key, Channel value) {
		channelCache.put(key, value);
	}

	public Channel get(String key) {
		return channelCache.get(key);
	}

	public void remove(String key) {
		this.channelCache.remove(key);
	}

	public int size() {
		return this.channelCache.size();
	}

}
