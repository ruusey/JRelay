package com.jr2;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.data.GameData;
import com.models.Packet;
import com.models.PacketModel;
import com.owlike.genson.Genson;

/**
 * Base class of {@link org.apache.mina.core.service.IoHandler} classes which
 * handle proxied connections.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public abstract class AbstractProxyIoHandler extends IoHandlerAdapter {
	private static final Charset CHARSET = Charset.forName("iso8859-1");
	public static final String OTHER_IO_SESSION = AbstractProxyIoHandler.class.getName() + ".OtherIoSession";
	public static Genson gen = new Genson();
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractProxyIoHandler.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		session.suspendRead();
		session.suspendWrite();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if (session.getAttribute(OTHER_IO_SESSION) != null) {
			IoSession sess = (IoSession) session.getAttribute(OTHER_IO_SESSION);
			sess.setAttribute(OTHER_IO_SESSION, null);
			sess.closeOnFlush();
			session.setAttribute(OTHER_IO_SESSION, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		IoBuffer rb = (IoBuffer) message;

		IoBuffer wb = IoBuffer.allocate(rb.remaining());
		rb.mark();
		wb.put(rb);
		wb.flip();

		((IoSession) session.getAttribute(OTHER_IO_SESSION)).write(wb);
		rb.reset();

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
//				System.out.print("length=" + acLength + ", ");
//				System.out.print("packetID=" + pId + ", name= ");
//				System.out.print(model.name + "\n");
				byte[] bytes = new byte[acLength - 5];
				IoBuffer data = rb.getSlice(5, acLength - 5);
				data.get(bytes);
				byte[] clone = bytes;
  				
  				try {
					Packet pack = Packet.create(pId);
					if(Packet.isClientPacket(pack)) {
						Main.remoteRecvRC4.cipher(bytes);
						pack = Packet.create(pId,bytes);
						System.out.println(gen.serialize(pack));
						
					}else {
						
					}
					
					
				} catch (Exception e) {
					Packet pack = Packet.create(pId);
					Main.localRecvRC4.cipher(clone);
					pack = Packet.create(pId,clone);
					System.out.println(gen.serialize(pack));
					//e.printStackTrace();
				}
			}
		} catch (Exception e) {
			
//        	IoBuffer length = wb.getSlice(4);
//            int acLength = length.getInt();
//            if(acLength>1024 || acLength<0) {
//           	 return;
//            }
//            IoBuffer idB = wb.getSlice(4,1);
//            byte pId = idB.get();
//           
//            
//            //IoBuffer data = rb.getSlice(5,acLength-5);
//           
//            PacketModel model = GameData.packets.get(pId);
//            if(model!=null) {
//            	
//            	System.out.print("length="+acLength+", ");
//            	 System.out.print("packetID="+pId+", name= ");
//           	 System.out.print(model.name+"\n");
//            	 //Packet packet = Packet.create(pId);
//            }
		}

	}
}