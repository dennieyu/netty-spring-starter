package netty.io.spring.server.config;

import java.net.InetSocketAddress;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import netty.io.spring.server.netty.handler.SimpleChatChannelInitializer;
import netty.io.spring.server.repository.ChannelRepository;

// NettyConfiguration
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(NettyProperties.class)
public class NettyConfiguration {

	private final NettyProperties nettyProperties;

	@Bean(name = "serverBootstrap")
	public ServerBootstrap bootstrap(SimpleChatChannelInitializer simpleChatChannelInitializer) {
		// Code to bootstrap server channel & start server. It binds the server to the port on which it will listen for connection.
		// 첫번째 부모 쓰레드는 클라이언트 연결 요청의 수락을 담당 한다. 두번째 인수는 연결된 소켓에 대한 I/O 를 처리 하는 자식 쓰레드 이다.
		// (클라이언트 연결 요청 수락 이벤트루프와, 데이터 송수신 처리를 위한 이벤트 루프 )
		ServerBootstrap b = new ServerBootstrap();

		b.group(bossGroup(), workerGroup())
				.channel(NioServerSocketChannel.class) // Configure server to use NIO selector based implementation to accept new connections.
				.handler(new LoggingHandler(LogLevel.DEBUG))
				.childHandler(simpleChatChannelInitializer); // ChannelInitializer 클라이언트로부터 연결된 채널이 초기화 될때 기본 동작이 지정된 추상 클레스
		b.option(ChannelOption.SO_BACKLOG, nettyProperties.getBacklog()); // SO_BACKLOG: 동시에 수용 가능한 소켓 연결 요청수
		return b;
	}

	// This event loop group looks for & accepts incoming connections.
	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(nettyProperties.getBossCount());
	}

	// Boss accepts connection & registers it to worker EventLoopGroup. Worker handles all the events during the communication through that connection.
	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(nettyProperties.getWorkerCount());
	}

	@Bean
	public InetSocketAddress tcpSocketAddress() {
		return new InetSocketAddress(nettyProperties.getTcpPort());
	}

	@Bean
	public ChannelRepository channelRepository() {
		return new ChannelRepository();
	}

}
