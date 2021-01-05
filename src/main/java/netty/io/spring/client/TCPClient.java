package netty.io.spring.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TCPClient {

	private static Channel channel;

	public static void main(String[] args) {
		// Since this is client, it doesn't need boss group. Create single group.
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap clientBootstrap = new Bootstrap(); // 연결 요청이 완료된 이후의 데이터 송수신 처리를 위해서 하나의 이벤트루프 생성

			clientBootstrap.group(group); // Set EventLoopGroup to handle all eventsf for client
			clientBootstrap.channel(NioSocketChannel.class); // Use NIO to accept new connections
			clientBootstrap.remoteAddress(new InetSocketAddress("localhost", 8090));
			clientBootstrap.handler(new ClientChannelInitializer());

			// Start the client
			ChannelFuture channelFuture = clientBootstrap.connect().sync();

			// Wait until the connection is closed
			channel = channelFuture.channel().closeFuture().sync().channel();
			log.info("The connection is closed..{}", channel.id());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				// Shut down the event loop to terminate all threads
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		// socketChannel.pipeline().addLast(new StringDecoder());
		// socketChannel.pipeline().addLast(new StringEncoder());
		socketChannel.pipeline().addLast(new ClientChannelHandler());
	}

}

@Slf4j
class ClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private static final String USER = "yu";

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(Unpooled.copiedBuffer("login|" + USER + System.lineSeparator(), CharsetUtil.UTF_8));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	/*
	 * Print chat message received from server.
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		log.info("received={}", ((ByteBuf) msg).toString(CharsetUtil.UTF_8).replaceAll(System.lineSeparator(), ""));
	}

}
