package netty.io.spring.server.netty;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.netty.channel.embedded.EmbeddedChannel;
import netty.io.spring.server.repository.ChannelRepository;

/**
 * Test for ChannelRepository.java
 *
 */
public class ChannelRepositoryTest {

	private ChannelRepository channelRepository = new ChannelRepository();

	@Test
	public void put() {
		EmbeddedChannel embeddedChannel = new EmbeddedChannel();
		channelRepository.put("yu", embeddedChannel);
		assertThat(channelRepository.get("yu"), is(embeddedChannel));
	}

	@Test
	public void remove() {
		EmbeddedChannel embeddedChannel = new EmbeddedChannel();
		channelRepository.put("yu", embeddedChannel);
		channelRepository.remove("yu");
		assertThat(channelRepository.get("yu"), is(nullValue()));
	}

	@Test
	public void size() {
		EmbeddedChannel embeddedChannel = new EmbeddedChannel();
		channelRepository.put("yu", embeddedChannel);
		channelRepository.remove("yu");
		assertThat(channelRepository.size(), is(0));
	}

}
