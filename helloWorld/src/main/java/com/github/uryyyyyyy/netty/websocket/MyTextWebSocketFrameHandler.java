package com.github.uryyyyyyy.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
  private final ChannelGroup group;

  MyTextWebSocketFrameHandler(ChannelGroup group) {
    this.group = group;
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {

      ctx.pipeline().remove(MyHttpRequestHandler.class);

      group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));

      group.add(ctx.channel());
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
    group.writeAndFlush(msg.retain());
  }
}
