package com.yichen.main;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;
import javax.websocket.server.PathParam;

import net.sf.json.JSONObject;

public class OpenService {
	public  void processOpen(Session session,String clientId,String objId,
			Map<String, Session> clients,Map<String,String> onetooneMap){
		Notice notice=new Notice();
		notice.setContent(clientId+"上线啦！");
		Set<String> olusers=clients.keySet();
		notice.setUnames(olusers);
		try{
			if(objId!=null&&!objId.equals("all")){
				onetooneMap.put(clientId, objId);//bind
				if(clients.get(objId)==null){
					notice.setContent(objId+"不在线！");
					clients.get(clientId).getBasicRemote().sendText(JSONObject.fromObject(notice).toString());
					}
				else{
					clients.get(objId).getBasicRemote().sendText(JSONObject.fromObject(notice).toString());
					notice.setContent(objId+"在线上！你们可以聊天了！");
					clients.get(clientId).getBasicRemote().sendText(JSONObject.fromObject(notice).toString());
					}
			}
		
		if(objId!=null&&objId.equals("all")){
			for(String oluser:olusers)
					clients.get(oluser).getBasicRemote().sendText(JSONObject.fromObject(notice).toString());
				
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			
		}
	}
}
