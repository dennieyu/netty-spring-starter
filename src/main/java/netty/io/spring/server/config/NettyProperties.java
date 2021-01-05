package netty.io.spring.server.config;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

// Netty ConfigurationProperties
@Getter
@Setter
@ConfigurationProperties(prefix = "netty")
public class NettyProperties {

	@NotNull
	@Size(min = 1000, max = 65535)
	private int tcpPort;

	@NotNull
	@Min(1)
	private int bossCount;

	@NotNull
	@Min(2)
	private int workerCount;

	@NotNull
	private boolean keepAlive;

	@NotNull
	private int backlog;

}
