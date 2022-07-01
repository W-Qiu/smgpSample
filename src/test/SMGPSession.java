package test;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.wondertek.ctmp.protocol.smgp.SMGPActiveTestMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPActiveTestRespMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPApi;
import com.wondertek.ctmp.protocol.smgp.SMGPBaseMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPDeliverMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPLoginRespMessage;
import com.wondertek.ctmp.protocol.smgp.SMGPSubmitMessage;

public class SMGPSession implements Runnable {

	protected static final SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");

	protected Logger log = LogManager.getLogger("gam_smgp");

	protected Logger fromGatewayLog = LogManager.getLogger("gam_smgp_from_gateway");

	protected Logger toGatewayLog = LogManager.getLogger("gam_smgp_to_gateway");

	protected SMGPApi api;

	protected boolean allowSend = true;

	protected Object sendLock = new Object();

	private void checkBind() {

		while (!api.isConnected()) {
			try {
				SMGPLoginRespMessage resp = api.connect();
				if (resp != null && resp.getStatus() == 0) {
					log.info("SMGPSession login success host=" + api.getHost() + ",port=" + api.getPort()
							+ ",clientId=" + api.getClientId());
					synchronized (api) {
						api.notifyAll();
					}
					break;
				} else {
					log.error("SMGPSession login fail, host=" + api.getHost() + ",port=" + api.getPort() + ",clientId="
							+ api.getClientId() + ",result=" + resp.getStatus());
				}
			} catch (IOException e) {
				log.error("SMGPSession login error", e);
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {}
		}
	}

	public void send(SMGPSubmitMessage submit) {
		while (!allowSend) {
			synchronized (sendLock) {
				try {
					sendLock.wait();
				} catch (InterruptedException e) {}
			}
		}

		while (!api.isConnected()) {
			synchronized (api) {
				try {
					api.wait();
				} catch (InterruptedException e) {}
			}
		}
		try {
			api.sendMsg(submit);
			toGatewayLog.info("����Ϣ�ɹ����͵�����:" + submit);
		} catch (IOException e) {
			api.close();
			toGatewayLog.warn("SMGPSession send msg fail,retry :" + submit, e);
			send(submit);
		}

	}

	public void run() {
		Thread.currentThread().setName("SMGPSession");
		checkBind();
		ActiveTestThread activeThread = new ActiveTestThread(api);
		activeThread.start();
		while (true) {
			try {
				SMGPBaseMessage baseMsg = api.receiveMsg();
				if (baseMsg == null) {
					log.error("SMGPSession receive msg null");
					api.close();
					checkBind();
					continue;
				}
				if (baseMsg instanceof SMGPActiveTestMessage) {
					SMGPActiveTestRespMessage resp = new SMGPActiveTestRespMessage();
					resp.setSequenceNumber(baseMsg.getSequenceNumber());
					api.sendMsg(resp);
				} else if (baseMsg instanceof SMGPActiveTestRespMessage) {
					//do nothing;
					fromGatewayLog.info("�������յ�ActiveTestResp��Ϣ:" + baseMsg);
				} else {
					fromGatewayLog.info("�������յ���Ϣ:" + baseMsg);
//					if (baseMsg instanceof SMGPDeliverMessage) {
//						Thread.sleep(1000 * 60);
//					}
				}
			} catch (Exception e) {
				log.error("SMGPSession receive msg error:" + e.getMessage());
				api.close();
				checkBind();
			}
		}
	}

}
