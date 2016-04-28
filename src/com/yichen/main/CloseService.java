package com.yichen.main;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

import net.sf.json.JSONObject;

public class CloseService {
	public  void processClose(String clientId, CloseReason reason,Map<String, Session> clients){
		 System.out.println("----------close");
		 clients.remove(clients.get(clientId));
		 Set<String> olusers=clients.keySet();
		 Notice notice=new Notice();
		 notice.setContent(clientId+"下线啦!");
		 notice.setUnames(olusers);
		 for(String uid:olusers){
			 try {
				clients.get(uid).getBasicRemote().sendText(JSONObject.fromObject(notice).toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
	}
}
