package netty.io.spring.server.netty.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.SocketAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import netty.io.spring.server.repository.ChannelRepository;

/**
 * Test for SimpleChatServerHandler.java
 *
 */
public class SimpleChatServerHandlerTest {

	private SimpleChatServerHandler somethingServerHandler;

	private ChannelHandlerContext channelHandlerContext;

	private Channel channel;

	private SocketAddress remoteAddress;

	@Before
	public void setUp() {
		somethingServerHandler = new SimpleChatServerHandler(new ChannelRepository());
		channelHandlerContext = mock(ChannelHandlerContext.class);
		channel = mock(Channel.class);
		remoteAddress = mock(SocketAddress.class);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testChannelActive() throws Exception {
		when(channelHandlerContext.channel()).thenReturn(channel);
		when(channelHandlerContext.channel().remoteAddress()).thenReturn(remoteAddress);
		somethingServerHandler.channelActive(channelHandlerContext);
	}

	@Test
	public void testChannelRead() {
		when(channelHandlerContext.channel()).thenReturn(channel);
		ByteBuf buf = Unpooled.directBuffer();
		buf.writeBytes("test message".getBytes());
		try {
			somethingServerHandler.channelRead(channelHandlerContext, buf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExceptionCaught() {

	}

}
