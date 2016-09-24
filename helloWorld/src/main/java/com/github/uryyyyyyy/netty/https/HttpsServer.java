package com.github.uryyyyyyy.netty.https;

import com.github.uryyyyyyy.netty.http.MyConsoleInboundHandler;
import com.github.uryyyyyyy.netty.http.MyConsoleOutboundHandler;
import com.github.uryyyyyyy.netty.http.MyHttpRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.ImmediateEventExecutor;

import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;

/**
 * @author <a href="mailto:norman.maurer@googlemail.com">Norman Maurer</a>
 */
public class HttpsServer {

  private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
  private final EventLoopGroup group = new NioEventLoopGroup();
  private Channel channel;

  private final SslContext context;

  private HttpsServer(SslContext context) {
    this.context = context;
  }

  private ChannelFuture start(InetSocketAddress address) {
    ServerBootstrap bootstrap  = new ServerBootstrap();
    bootstrap.group(group)
      .channel(NioServerSocketChannel.class)
      .childHandler(new ChannelInitializer<Channel>() {
        @Override
        protected void initChannel(Channel ch) throws Exception {
          ChannelPipeline pipeline = ch.pipeline();
          pipeline.addLast("console1", new MyConsoleInboundHandler());
          pipeline.addLast(new HttpResponseEncoder());
          pipeline.addLast("console2", new MyConsoleInboundHandler());
          pipeline.addLast(new MyHttpRequestHandler());

          pipeline.addFirst("console3", new MyConsoleOutboundHandler());
          pipeline.addFirst(new HttpRequestDecoder());

          SSLEngine engine = context.newEngine(ch.alloc());
          engine.setUseClientMode(false);
          pipeline.addFirst(new SslHandler(engine));
        }
      });
    ChannelFuture future = bootstrap.bind(address);
    future.syncUninterruptibly();
    channel = future.channel();
    return future;
  }

  public static void main(String[] args) throws Exception{
    if (args.length != 1) {
      System.err.println("Please give port as argument");
      System.exit(1);
    }
    int port = Integer.parseInt(args[0]);
    SelfSignedCertificate cert = new SelfSignedCertificate();
    SslContext context = SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();
    final HttpsServer endpoint = new HttpsServer(context);
    ChannelFuture future = endpoint.start(new InetSocketAddress(port));

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        endpoint.destroy();
      }
    });
    future.channel().closeFuture().syncUninterruptibly();
  }

  private void destroy() {
    if (channel != null) {
      channel.close();
    }
    channelGroup.close();
    group.shutdownGracefully();
  }
}
