package com.github.uryyyyyyy.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;


class MyHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  @Override
  public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
    ctx.fireChannelRead(request.retain());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
