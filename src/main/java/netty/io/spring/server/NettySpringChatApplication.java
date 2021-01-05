package netty.io.spring.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import netty.io.spring.server.netty.TCPMainServer;

// Spring-Boot Java Configuration and Bootstrap
@RequiredArgsConstructor
@SpringBootApplication
public class NettySpringChatApplication {

	private final TCPMainServer tcpMainServer;

	public static void main(String[] args) {
		SpringApplication.run(NettySpringChatApplication.class, args);
	}

	@Bean
	public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
		return new ApplicationListener<ApplicationReadyEvent>() {
			@Override
			public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
				tcpMainServer.start();
			}
		};
	}

}
