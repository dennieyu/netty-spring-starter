package netty.io.spring.server.netty.handler;

import org.springframework.stereotype.Component;

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
public class SimpleLoginHandler extends SimpleChannelInboundHandler<String> {

	private final ChannelRepository channelRepository;

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg) {
		if (!msg.startsWith("login|")) {
			ctx.fireChannelRead(msg);
			return;
		}
		log.debug("read from channel={}", msg);

		User user = User.of(msg, ctx.channel());
		user.login(channelRepository, ctx.channel());

		ctx.writeAndFlush("Successfully logged in as " + user.getUsername() + System.lineSeparator());
	}

}
