package netty.io.spring.server.netty.handler;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.RequiredArgsConstructor;

// Channel Initializer
@Component
@RequiredArgsConstructor
public class SimpleChatChannelInitializer extends ChannelInitializer<SocketChannel> {

	// handler
	private final SimpleChatServerHandler simpleChatServerHandler;
	private final SimpleLoginHandler simpleLoginHandler;

	// Decoder/encoder (StringDecoder / StringEncoder)
	private final StringEncoder stringEncoder = new StringEncoder();
	private final StringDecoder stringDecoder = new StringDecoder();

	@Override
	protected void initChannel(SocketChannel socketChannel) {
		// channel pipeline
		ChannelPipeline pipeline = socketChannel.pipeline();

		// add the text line codec combination first
		pipeline.addLast(new DelimiterBasedFrameDecoder(1024 * 1024, Delimiters.lineDelimiter()));

		// Socket/channel communication happens in byte streams. String decoder & encoder helps conversion between bytes & String.
		pipeline.addLast(stringDecoder);
		pipeline.addLast(stringEncoder);

		pipeline.addLast(simpleChatServerHandler);
		pipeline.addLast(simpleLoginHandler);
	}

}
