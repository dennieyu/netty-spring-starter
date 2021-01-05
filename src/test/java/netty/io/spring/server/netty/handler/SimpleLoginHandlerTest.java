package netty.io.spring.server.netty.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.netty.channel.embedded.EmbeddedChannel;
import netty.io.spring.server.repository.ChannelRepository;

/**
 * Test for SimpleLoginHandler.java
 *
 */
class SimpleLoginHandlerTest {

	ChannelRepository channelRepository;

	@BeforeEach
	void setup() {
		channelRepository = mock(ChannelRepository.class);
	}

	@Test
	@DisplayName("login-handler-test")
	void testLogin() {
		// given
		EmbeddedChannel embeddedChannel = new EmbeddedChannel(new SimpleLoginHandler(this.channelRepository));

		// when
		embeddedChannel.writeInbound("login|yu\r\n");

		// then
		Queue<Object> outboundMessages = embeddedChannel.outboundMessages();
		// System.out.println(outboundMessages.poll());
		assertThat(outboundMessages.poll()).isEqualTo("Successfully logged in as yu" + System.lineSeparator());
	}

}
