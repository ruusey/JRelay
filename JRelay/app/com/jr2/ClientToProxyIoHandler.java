package com.jr2;
import java.net.SocketAddress;
  
  import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
  import org.apache.mina.core.future.IoFutureListener;
  import org.apache.mina.core.service.IoConnector;
  import org.apache.mina.core.session.IoSession;

import com.data.GameData;
import com.models.Packet;
import com.models.PacketModel;
  
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
      @Override
  	public void messageReceived(IoSession session, Object message) throws Exception {
  		IoBuffer rb = (IoBuffer) message;

  		IoBuffer wb = IoBuffer.allocate(rb.remaining()).setAutoExpand(true);;
  		rb.mark();
  		wb.put(rb);
  		wb.flip();
  		rb.reset();
  		((IoSession) session.getAttribute(OTHER_IO_SESSION)).write(wb);
  		try {

  			IoBuffer length = rb.getSlice(4);

  			int acLength = length.getInt();
  			if (acLength > 1024 || acLength < 0) {
  				return;
  			}
  			IoBuffer idB = rb.getSlice(4, 1);
  			byte pId = idB.get();

  			// IoBuffer data = rb.getSlice(5,acLength-5);

  			PacketModel model = GameData.packets.get(pId);
  			if (model != null) {
  				System.out.print("length=" + acLength + ", ");
  				System.out.print("packetID=" + pId + ", name= ");
  				System.out.print(model.name + "\n");
  				byte[] bytes = new byte[acLength - 5];
  				IoBuffer data = rb.getSlice(5, acLength - 5);
  				data.get(bytes);
  				byte[] clone = bytes;
  				
  				try {
					Packet pack = Packet.create(pId);
					if(Packet.isClientPacket(pack)) {
						Main.localRecvRC4.cipher(bytes);
						pack = Packet.create(pId,bytes);
						System.out.println(gen.serialize(pack));
						
					}
					
					
					
  				} catch (Exception e) {
				
					
  					//System.err.println("Unable to parse "+ e.getMessage());
				}
  			}
  		} catch (Exception e) {
  			//e.printStackTrace();
//          	IoBuffer length = wb.getSlice(4);
//              int acLength = length.getInt();
//              if(acLength>1024 || acLength<0) {
//             	 return;
//              }
//              IoBuffer idB = wb.getSlice(4,1);
//              byte pId = idB.get();
//             
//              
//              //IoBuffer data = rb.getSlice(5,acLength-5);
//             
//              PacketModel model = GameData.packets.get(pId);
//              if(model!=null) {
//              	
//              	System.out.print("length="+acLength+", ");
//              	 System.out.print("packetID="+pId+", name= ");
//             	 System.out.print(model.name+"\n");
//              	 //Packet packet = Packet.create(pId);
//              }
  		}

  		
  		

  	}
  }