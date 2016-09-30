package com.github.uryyyyyyy.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.Charset;

@Sharable
public class MyConsoleOutboundHandler extends ChannelOutboundHandlerAdapter {

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    println(msg);
    super.write(ctx, msg, promise);
  }

  private void println(Object msg) {
    System.out.println("outbound-------------------------------");
    if(msg instanceof ByteBuf){
      ByteBuf msg_ = (ByteBuf) msg;
      System.out.println(msg_.toString(Charset.forName("UTF-8")));
    }else if(msg instanceof DefaultHttpResponse){
      DefaultHttpResponse msg_ = (DefaultHttpResponse) msg;
      System.out.println(msg_.toString());
    }else{
      System.out.println("other");
      System.out.println(msg.toString());
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
