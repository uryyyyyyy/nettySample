package com.github.uryyyyyyy.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author <a href="mailto:norman.maurer@googlemail.com">Norman Maurer</a>
 */
public class MyHttpRequestHandler extends SimpleChannelInboundHandler<HttpRequest> {

  @Override
  public void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {

    HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.BAD_REQUEST);
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

    ctx.write(response);

    ctx.write(Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8));
    ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    future.addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
