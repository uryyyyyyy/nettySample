package com.github.uryyyyyyy.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * @author <a href="mailto:norman.maurer@googlemail.com">Norman Maurer</a>
 */
public class HttpServer {

  private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
  private final EventLoopGroup group = new NioEventLoopGroup();
  private Channel channel;

  private ChannelFuture start(InetSocketAddress address) {
    ServerBootstrap bootstrap  = new ServerBootstrap();
    bootstrap.group(group)
      .channel(NioServerSocketChannel.class)
      .childHandler(new ChannelInitializer() {
        @Override
        protected void initChannel(Channel ch) throws Exception {
          ChannelPipeline pipeline = ch.pipeline();
          pipeline.addLast("console1", new MyConsoleInboundHandler());
          pipeline.addLast(new HttpResponseEncoder());
          pipeline.addLast("console2", new MyConsoleInboundHandler());
          pipeline.addLast(new MyHttpRequestHandler());

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

    final HttpServer endpoint = new HttpServer();
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
