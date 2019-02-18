package com.jr2;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.crypto.RC4;


/**
 * An UDP client taht just send thousands of small messages to a UdpServer.
 * 
 * This class is used for performance test purposes. It does nothing at all, but
 * send a message repetitly to a server.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class TcpClient extends IoHandlerAdapter {
	/** The connector */
	private IoConnector connector;
	//key1=c79332b197f92ba85ed281a023
	public static String key1 = "c79332b197f92ba85ed281a023";
	public static String key0 = "6a39570cc9de4ec71d64821894";
	public RC4 localRecvRC4;
	public RC4 localSendRC4;
	/** The session */
	private static IoSession session;
	public RC4 remoteRecvRC4;
	public RC4 remoteSendRC4;
	/** The buffer containing the message to send */
	private IoBuffer buffer = IoBuffer.allocate(5);

	/** Timers **/
	private long t0;
	private long t1;

	/** the counter used for the sent messages */
	private CountDownLatch counter;

	/**
	 * Create the UdpClient's instance
	 */
	public TcpClient() {
		 NioSocketAcceptor acceptor = new NioSocketAcceptor();
         acceptor.setHandler(this);
         buffer.setAutoExpand(true);
		this.localRecvRC4 = new RC4(key0);
		this.localSendRC4 = new RC4(key1);
		this.remoteRecvRC4 = new RC4(key1);
		this.remoteSendRC4 = new RC4(key0);
		connector.setHandler(this);
		//acceptor.bind(new InetSocketAddress(TcpServer.PORT));
		ConnectFuture connFuture = connector.connect(new InetSocketAddress("localhost", TcpServer.PORT));

		connFuture.awaitUninterruptibly();

		session = connFuture.getSession();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
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
			buffer.rewind();
				

				buffer.flip();
				buffer.putLong(counter.getCount());
				buffer.flip();
				session.write(buffer);
			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		if (counter.getCount() % 10000 == 0) {
			System.out.println("Sent " + counter + " messages");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
	}

	/**
	 * The main method : instanciates a client, and send N messages. We sleep
	 * between each K messages sent, to avoid the server saturation.
	 * 
	 * @param args The arguments
	 * @throws Exception If something went wrong
	 */
	public static void main(String[] args) throws Exception {
		TcpClient client = new TcpClient();

		client.t0 = System.currentTimeMillis();
		client.counter = new CountDownLatch(TcpServer.MAX_RECEIVED);
		client.buffer.putLong(client.counter.getCount());
		client.buffer.flip();
		session.write(client.buffer);
		int nbSeconds = 0;

		while ((client.counter.getCount() > 0) && (nbSeconds < 120)) {
			// Wait for one second
			client.counter.await(1, TimeUnit.SECONDS);
			nbSeconds++;
		}

		client.connector.dispose(true);
	}
}