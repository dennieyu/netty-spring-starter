package netty.io.spring.server.netty.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netty.io.spring.server.domain.User;
import netty.io.spring.server.repository.ChannelRepository;

// event handler
@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {

	// List of connected client channels
	private final ChannelRepository channelRepository;

	/*
	 * Whenever client connects to server through channel, add his channel to the
	 * list of channels.
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		Assert.notNull(this.channelRepository, "ChannelRepository is required; it must not be null");

		ctx.fireChannelActive();
		String remoteAddress = ctx.channel().remoteAddress().toString();

		log.debug("remoteAddress={}", remoteAddress);
		ctx.writeAndFlush("Your remote address is " + remoteAddress + System.lineSeparator());

		log.info("channelSize={}", this.channelRepository.size());
	}

	/*
	 * When a message is received from client, send that message to all channels.
	 * For the sake of simplicity, currently we will send received chat message to
	 * all clients instead of one specific client. This code has scope to improve to
	 * send message to specific client as per senders choice.
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg) {
		if (msg.startsWith("login|")) {
			ctx.fireChannelRead(msg);
			return;
		}
		log.debug("read from channel={}", msg);

		String[] splitMessage = msg.split("\\|");
		if (splitMessage.length != 2) {
			ctx.channel().writeAndFlush(msg + System.lineSeparator());
			return;
		}

		User.current(ctx.channel()).tell(channelRepository.get(splitMessage[0]), splitMessage[0], splitMessage[1]);
	}

	/*
	 * In case of exception, close channel. One may chose to custom handle exception
	 * & have alternative logical flows.
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
		log.error(ctx.toString());
		log.error(t.getMessage());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Assert.notNull(this.channelRepository, "ChannelRepository is required; it must not be null");
		Assert.notNull(ctx, "ChannelHandlerContext is required; it must not be null");

		User.current(ctx.channel()).logout(this.channelRepository, ctx.channel());
		log.info("channelSize={}", this.channelRepository.size());
	}

}
