package carmelo.netty.websocket;

import carmelo.netty.http.HttpServerHandler;
import carmelo.servlet.Servlet;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

	private Servlet servlet;
	
	private final static EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(8);;
	
    public WebSocketServerInitializer(Servlet servlet){
    	this.servlet = servlet;
    }
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("codec", new HttpServerCodec());
        pipeline.addLast("chunked", new ChunkedWriteHandler());
        pipeline.addLast("aggregator", new HttpObjectAggregator(8192));
        pipeline.addLast("protocol", new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(businessGroup, "handler", new WebSocketServerHandler(servlet));
    }
}
