package test;

import java.io.UnsupportedEncodingException;

import com.wondertek.ctmp.protocol.smgp.SMGPApi;
import com.wondertek.ctmp.protocol.smgp.SMGPSubmitMessage;
import com.wondertek.ctmp.protocol.util.RandomGenerator;

public class SMGPApiTest {

	public static void main(String[] args) {

		SMGPApi mtApi = new SMGPApi();
		mtApi.setHost("222.66.24.235");
		mtApi.setPort(8900);
		
		mtApi.setClientId("XXXXX"); //online
		mtApi.setPassword("XXXXX");
		
		
		mtApi.setVersion((byte) 0);
		mtApi.setLoginMode(SMGPApi.MT_MO);

		SMGPSession session = new SMGPSession();
		session.api = mtApi;

		new Thread(session).start();

		for (int k = 0; k < 1; k++) {
			

			SMGPSubmitMessage submit = new SMGPSubmitMessage();
			submit.setSrcTermId("XXXXXXXX");
			submit.setDestTermIdArray(new String[] { "18012345678" });
			
			submit.setMsgFmt((byte) 8);

			String content = "smgp����test";
			content =  k+"�������Ƕ��ٵĴ��������˯����������sfjlsdksdjklsklsdlksdˮ��Ѽ����sd����ŵ��ǿ��ǵ���ˮ��Ѽ�ʶ���˿ͷ�ʥ���ڵĵط������33�ḻ��";
			
			byte[] bContent = null;

			try {
				bContent = content.getBytes("iso-10646-ucs-2");
			} catch (UnsupportedEncodingException e) {}

			if (bContent.length <= 140) {
				submit.setBMsgContent(bContent);
				
				submit.setMsgFmt((byte) 8);
//				submit.setMsgFmt((byte) 0);
				submit.setNeedReport((byte) 1);
				submit.setServiceId("");
				submit.setSequenceNumber(RandomGenerator.getAbsInt());
				submit.setAtTime("");
				//opt          
				submit.setTpPid((byte) 0);
				submit.setTpUdhi((byte) 0);
				
				session.send(submit);
			} else { //  �����Ų��
				int num = bContent.length / 134;//������ŷָ�����
				int endNum = bContent.length % 134;
				if ( endNum != 0) {
					num = num + 1;
				}

				byte[] head = new byte[6];
				head[0] = 5;
				head[1] = 0;
				head[2] = 3;
				head[3] = (byte) RandomGenerator.getByte();
				head[4] = (byte) num;

				for (int i = 0; i < num; i++) {
					byte[] b = null;
					if(i==num-1 && endNum != 0){ //�ָ�����һ������134
						b = new byte[(endNum+6)];
					}else{
						b = new byte[140];
					}
					
					head[5] = (byte) (i + 1);
					System.arraycopy(head, 0, b, 0, head.length);

					int len = Math.min(134, bContent.length - 134 * i);

					System.arraycopy(bContent, i * 134, b, head.length, len);
					
					submit.setBMsgContent(b);
					submit.setMsgFmt((byte) 8);
					submit.setNeedReport((byte) 1);
					submit.setServiceId("");
					submit.setSequenceNumber(RandomGenerator.getAbsInt());
					submit.setAtTime("");

					//opt          
					submit.setTpPid((byte) 0);
					submit.setTpUdhi((byte) 1);

					session.send(submit);
				}
			}
			
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e1) {}
		}
	}

}
