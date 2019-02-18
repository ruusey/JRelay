package com.jr2;
import java.io.IOException;
  import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
  import org.apache.mina.core.session.IdleStatus;
  import org.apache.mina.core.session.IoSession;
  import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.crypto.RC4;
import com.data.GameData;
import com.models.Packet;
  
  /**
   * An TCP server used for performance tests.
   * 
   * It does nothing fancy, except receiving the messages, and counting the number of
   * received messages.
   * 
   * @author <a href="http://mina.apache.org">Apache MINA Project</a>
   */
  public class TcpServer extends IoHandlerAdapter {
      /** The listening port (check that it's not already in use) */
      public static final int PORT = 2050;
      public static String key1 = "c79332b197f92ba85ed281a023";
  	public static String key0 = "6a39570cc9de4ec71d64821894";
  	public RC4 localRecvRC4;
  	public RC4 localSendRC4;
  	public RC4 remoteRecvRC4;
	public RC4 remoteSendRC4;
	private IoBuffer buffer = IoBuffer.allocate(5);
      /** The number of message to receive */
      public static final int MAX_RECEIVED = 100000;
      private static IoSession session;
      /** The starting point, set when we receive the first message */
      private static long t0;
  
      /** A counter incremented for every recieved message */
      private AtomicInteger nbReceived = new AtomicInteger(0);
  
      /**
       * {@inheritDoc}
       */
      @Override
      public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
          cause.printStackTrace();
          session.closeNow();
      }
  
      /**
       * {@inheritDoc}
       */
      @Override
      public void messageReceived(IoSession session, Object message) throws Exception {
    	  IoBuffer received = buffer.get(new byte[5]);
  		int bytes = received.getInt();
  		byte id = received.get();
  		byte[] data = new byte[bytes-5];
  		IoBuffer fullData = received.get(data);
  		this.localRecvRC4.cipher(data);
  		if (bytes != data.length) {
  			System.out.println("Error !");
  			session.closeNow();
  		} else {
  			this.localSendRC4.cipher(data);
  			buffer.reset();
  			buffer.putInt(bytes)	;
  			buffer.put(id)	;
  			buffer.put(data);	
  			session.write(buffer,new InetSocketAddress("52.91.68.60", 2050));
  			
  		}
      }
  
      /**
       * {@inheritDoc}
       */
      @Override
      public void sessionClosed(IoSession session) throws Exception {
          System.out.println("Session closed...");
  
          // Reinitialize the counter and expose the number of received messages
          System.out.println("Nb message received : " + nbReceived.get());
          nbReceived.set(0);
      }
  
      /**
       * {@inheritDoc}
      */
     @Override
     public void sessionCreated(IoSession session) throws Exception {
    	 TcpServer.session=session;
         System.out.println("Session created...");
     }
 
     /**
      * {@inheritDoc}
      */
     @Override
     public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
         System.out.println("Session idle...");
     }
 
     /**
      * {@inheritDoc}
      * @param session the current seession
      * @throws Exception If something went wrong
      */
     @Override
     public void sessionOpened(IoSession session) throws Exception {
         
         
     }
 
     /**
      * Create the TCP server
      * 
      * @throws IOException If something went wrong
      */
     public TcpServer() throws IOException {
         NioSocketAcceptor acceptor = new NioSocketAcceptor();
         acceptor.setHandler(this);
         buffer.setAutoExpand(true);
 		this.localRecvRC4 = new RC4(key0);
 		this.localSendRC4 = new RC4(key1);
 		this.remoteRecvRC4 = new RC4(key1);
 		this.remoteSendRC4 = new RC4(key0);
         // The logger, if needed. Commented atm
         //DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
         //chain.addLast("logger", new LoggingFilter());
 
         acceptor.bind(new InetSocketAddress(PORT));
 
         System.out.println("Server started...");
     }
 
     /**
      * The entry point.
      * 
      * @param args The arguments
      * @throws IOException If something went wrong
      */
     public static void main(String[] args) throws IOException {
    	 GameData.loadData();
    	 Packet.init();
         new TcpServer();
     }
 }
