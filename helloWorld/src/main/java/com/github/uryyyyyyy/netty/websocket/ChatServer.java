package com.github.uryyyyyyy.netty.websocket;

import com.github.uryyyyyyy.netty.http.MyConsoleInboundHandler;
import com.github.uryyyyyyy.netty.http.MyConsoleOutboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

public class ChatServer {

  private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
  private final EventLoopGroup group = new NioEventLoopGroup();
  private Channel channel;

  private ChannelFuture start(InetSocketAddress address) {
    ServerBootstrap bootstrap  = new ServerBootstrap();
    bootstrap.group(group)
      .channel(NioServerSocketChannel.class)
      .childHandler(new ChannelInitializer<Channel>() {
        @Override
        protected void initChannel(Channel ch) throws Exception {
          ChannelPipeline pipeline = ch.pipeline();
          pipeline.addLast(new HttpResponseEncoder());
          pipeline.addLast("console1", new MyConsoleInboundHandler());
          pipeline.addLast(new HttpObjectAggregator(64 * 1024));
          pipeline.addLast(new ChunkedWriteHandler());
          pipeline.addLast(new MyHttpRequestHandler());
          pipeline.addLast("console2", new MyConsoleInboundHandler());
          pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
          pipeline.addLast(new MyTextWebSocketFrameHandler(channelGroup));

          pipeline.addFirst("console3", new MyConsoleOutboundHandler());
          pipeline.addFirst(new HttpRequestDecoder());
        }
      });
    ChannelFuture future = bootstrap.bind(address);
    future.syncUninterruptibly();
    channel = future.channel();
    return future;
  }

  private void destroy() {
    if (channel != null) {
      channel.close();
    }
    channelGroup.close();
    group.shutdownGracefully();
  }

  public static void main(String[] args) throws Exception{
    if (args.length != 1) {
      System.err.println("Please give port as argument");
      System.exit(1);
    }
    int port = Integer.parseInt(args[0]);

    final ChatServer endpoint = new ChatServer();
    ChannelFuture future = endpoint.start(new InetSocketAddress(port));

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        endpoint.destroy();
      }
    });
    future.channel().closeFuture().syncUninterruptibly();
  }
}
