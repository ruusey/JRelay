package com.jr2;
import java.net.SocketAddress;
  
  import org.apache.mina.core.RuntimeIoException;
  import org.apache.mina.core.future.ConnectFuture;
  import org.apache.mina.core.future.IoFutureListener;
  import org.apache.mina.core.service.IoConnector;
  import org.apache.mina.core.session.IoSession;
  
  /**
   * Handles the client to proxy part of the proxied connection.
   *
   * @author <a href="http://mina.apache.org">Apache MINA Project</a>
   */
  public class ClientToProxyIoHandler extends AbstractProxyIoHandler {
      private final ServerToProxyIoHandler connectorHandler = new ServerToProxyIoHandler();
  
      private final IoConnector connector;
  
      private final SocketAddress remoteAddress;
  
      public ClientToProxyIoHandler(IoConnector connector,
              SocketAddress remoteAddress) {
          this.connector = connector;
          this.remoteAddress = remoteAddress;
          connector.setHandler(connectorHandler);
      }
  
      @Override
      public void sessionOpened(final IoSession session) throws Exception {
  
          connector.connect(remoteAddress).addListener(new IoFutureListener<ConnectFuture>() {
              public void operationComplete(ConnectFuture future) {
                  try {
                      future.getSession().setAttribute(OTHER_IO_SESSION, session);
                      session.setAttribute(OTHER_IO_SESSION, future.getSession());
                      IoSession session2 = future.getSession();
                      session2.resumeRead();
                      session2.resumeWrite();
                  } catch (RuntimeIoException e) {
                      // Connect failed
                      session.closeNow();
                  } finally {
                      session.resumeRead();
                      session.resumeWrite();
                  }
              }
          });
      }
  }