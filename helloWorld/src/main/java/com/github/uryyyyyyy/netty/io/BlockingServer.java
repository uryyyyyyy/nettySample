package com.github.uryyyyyyy.netty.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listing 2.2  of <i>Netty in Action</i>
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class BlockingServer {

  static final int port = 9000;

  public static void main(String[] args) throws Exception {

    ServerSocket serverSocket = new ServerSocket(port);
    Socket clientSocket = serverSocket.accept();//blocking
    BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);

    String request, response;
    while ((request = br.readLine()) != null){//blocking
      if("Done".equals(request)){
        break;
      }
      response = func1(request);
      pw.println(response);
    }
  }

  static String func1(String str){
    System.out.println(str);
    return new StringBuilder(str).reverse().toString();
  }
}
