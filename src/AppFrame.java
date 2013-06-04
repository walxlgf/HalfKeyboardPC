import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.TypeReference;

import com.single.handed.vo.KeyData;
import com.single.handed.vo.KeyDatas;
import com.single.handed.vo.KeyValue;

public class AppFrame extends Frame {
	private static final long serialVersionUID = 1L;
	public static final int PORT = 8088;
	public static String host = "";
	private int height = 100;
	private int width = 400;
	private Font font;
	private List<IoSession> sessions;
	private Robot mRobot;
	private ObjectMapper mObjectMapper = new ObjectMapper();

	public AppFrame() throws HeadlessException {
		super();
		this.setSize(width, height);
		try {
			InetAddress localAddr = InetAddress.getLocalHost();
			if (localAddr.isLoopbackAddress()) {
				localAddr = LinuxInetAddress.getLocalHost();
			}
			host = localAddr.getHostAddress();
		} catch (UnknownHostException ex) {
			host = "Error finding local IP.";
		}
		this.font = new Font("ø¨ÃÂGB-2312", Font.BOLD, 25);
		try {
			mRobot = new Robot();
			mRobot.setAutoDelay(5);
		} catch (AWTException e) {
			System.out.println("robot:" + e);
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				IoAcceptor acceptor = new NioSocketAcceptor();
				acceptor.getFilterChain().addLast("logger", new LoggingFilter());
				acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
				//				acceptor.getFilterChain().addLast("objectFilter", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
				acceptor.setHandler(new ServerHandler());
				acceptor.getSessionConfig().setReadBufferSize(2048);
				acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
				System.out.println("begin bind");
				try {
					acceptor.bind(new InetSocketAddress(PORT));
					System.out.println(" bind end");
				} catch (IOException e) {
					System.out.println(e.getMessage());

				}
			}
		}).start();
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLUE);
		g.setFont(font);
		g.drawString("  IP:" + host, 10, 60);
	}

	@Override
	public void dispose() {
		super.dispose();
		System.out.println("dispose");
		if (sessions != null) {
			for (IoSession session : sessions) {
				session.close();
			}
		}
	}

	class ServerHandler extends IoHandlerAdapter {

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			super.sessionOpened(session);
			System.out.println("sessionOpened " + session);
			if (sessions == null || !sessions.contains(session)) {
				if (sessions == null) {
					sessions = new ArrayList<IoSession>();
				}
				sessions.add(session);
			}
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
			cause.printStackTrace();
		}

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			System.out.println("Message read:" + message.toString());
			KeyDatas keyDatas = null;
			try {
				keyDatas = mObjectMapper.readValue(message.toString(), new TypeReference<KeyDatas>() {
				});
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("keyDatas:" + keyDatas);
			if (keyDatas != null && keyDatas.getKeyDatas() != null) {
				for (KeyData keyData : keyDatas.getKeyDatas()) {
					if (keyData.getType() == KeyData.TYPE_KEY_PRESS) {
						mRobot.keyPress(keyData.getCode());
						System.out.println("press:" + keyData.getCode());
					} else {
						mRobot.keyRelease(keyData.getCode());
						System.out.println("release:" + keyData.getCode());
					}
				}
			}
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
			System.out.println("IDLE " + session.getIdleCount(status));
		}

		@Override
		public void messageSent(IoSession session, Object message) throws Exception {
			super.messageSent(session, message);
			System.out.println("messageSent " + session);
			if (sessions.contains(session)) {
				sessions.remove(session);
			}
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			super.sessionClosed(session);
			System.out.println("sessionClosed " + session);
			if (sessions.contains(session)) {
				sessions.remove(session);
			}
		}

	}

}
