package netty.io.spring.server.netty;

import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Main Server
@Slf4j
@RequiredArgsConstructor
@Component
public class TCPMainServer {

	private final ServerBootstrap serverBootstrap;

	private final InetSocketAddress tcpPort;

	private Channel serverChannel;

	public void start() {
		try {
			// Start the server
			ChannelFuture serverChannelFuture = serverBootstrap.bind(tcpPort).sync();
			log.info("Server is started.. port={}", tcpPort.getPort());

			// Wait until the server socket is closed.
			serverChannel = serverChannelFuture.channel().closeFuture().sync().channel();
			log.info("Server socket is closed..");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@PreDestroy
	public void stop() {
		if (serverChannel != null) {
			serverChannel.close();
			serverChannel.parent().close();
		}
	}

}
