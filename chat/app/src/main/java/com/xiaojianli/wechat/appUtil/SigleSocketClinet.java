package com.xiaojianli.wechat.appUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.UnknownHostException;

public class SigleSocketClinet extends Socket {
	
	private final static String TAG = "SigleSocketClinet.";
	
	private static SigleSocketClinet instance;
	
	
	private SigleSocketClinet() {
		super();
		// TODO Auto-generated constructor stub
	}


	private SigleSocketClinet(InetAddress dstAddress, int dstPort,
			InetAddress localAddress, int localPort) throws IOException {
		super(dstAddress, dstPort, localAddress, localPort);
		// TODO Auto-generated constructor stub
	}


	private SigleSocketClinet(InetAddress dstAddress, int dstPort)
			throws IOException {
		super(dstAddress, dstPort);
		// TODO Auto-generated constructor stub
	}


	private SigleSocketClinet(Proxy proxy) {
		super(proxy);
		// TODO Auto-generated constructor stub
	}


	private SigleSocketClinet(SocketImpl impl) throws SocketException {
		super(impl);
		// TODO Auto-generated constructor stub
	}


	private SigleSocketClinet(String dstName, int dstPort,
			InetAddress localAddress, int localPort) throws IOException {
		super(dstName, dstPort, localAddress, localPort);
		// TODO Auto-generated constructor stub
	}


	private SigleSocketClinet(String dstName, int dstPort)
			throws UnknownHostException, IOException {
		super(dstName, dstPort);
		// TODO Auto-generated constructor stub
	}

	public static SigleSocketClinet getInstance() {
		
		if (instance == null) {
			try {
				appLog.e(TAG + "START");
//				instance = new SigleSocketClinet("192.168.1.100",2016);
				instance = new SigleSocketClinet("120.27.49.81",2016);
				appLog.e(TAG + "END");

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				appLog.e(TAG + e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				appLog.e(TAG + e.toString());
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public static void closeSocket() {
		if (instance != null) {
			try {
				instance.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				instance = null;				
			}
		}
	}

}
