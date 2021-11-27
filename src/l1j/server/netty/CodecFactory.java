package l1j.server.netty;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;


public final class CodecFactory implements ChannelPipelineFactory {
	
	private final ProtocolHandler handler;

	public CodecFactory(ProtocolHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		
		pipeline.addLast("handler", handler);
		
		return pipeline;
	}
}
