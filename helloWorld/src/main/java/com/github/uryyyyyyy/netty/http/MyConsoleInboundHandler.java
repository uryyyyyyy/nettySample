package com.github.uryyyyyyy.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.Charset;

@Sharable
public class MyConsoleInboundHandler extends SimpleChannelInboundHandler<Object> {

  @Override
  public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    println(msg);
    ReferenceCountUtil.retain(msg);
    ctx.fireChannelRead(msg);
  }

  private void println(Object msg) {
    System.out.println("inbound-------------------------------");
    if(msg instanceof ByteBuf){
      ByteBuf msg_ = (ByteBuf) msg;
      System.out.println(msg_.toString(Charset.forName("UTF-8")));
    }else if(msg instanceof DefaultHttpRequest){
      DefaultHttpRequest msg_ = (DefaultHttpRequest) msg;
      System.out.println(msg_.toString());
    }else if(msg instanceof TextWebSocketFrame){
      TextWebSocketFrame msg_ = (TextWebSocketFrame) msg;
      System.out.println(msg_.toString());
      System.out.println(msg_.text());
    }
    else{
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
