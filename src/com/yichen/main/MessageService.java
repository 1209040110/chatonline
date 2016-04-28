package com.yichen.main;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.New;
import javax.websocket.CloseReason;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.PathParam;

import net.sf.json.JSONObject;

public class MessageService {
	public  void processMessage(String message,String clientId,
			Map<String, Session> clients,Session session,Map<String,String> onetooneMap){
		if(message.equals("quit")){
            try {
            	clients.remove(clientId);
                session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "quit"));
            } catch (IOException e) {
                ;
            }
           
        }else{
        	Set<String> olusers=null;
        	String objId=onetooneMap.get(clientId);
        	Notice notice=new Notice();
        	StringBuilder sb=new StringBuilder();
		 	sb.append(MyServerEndpoint.DATE_FORMAT.format(new Date()));
		 	notice.setContent(clientId+"&nbsp;&nbsp;&nbsp;"+sb.toString()+"<br/>"+message);
        	if(objId==null)
        		olusers=clients.keySet();
        	else{
        		olusers=new HashSet<String>();
        		if(clients.get(objId)==null){
        			notice.setContent(objId+"未上线！");
        			olusers.add(clientId);
        		}else{
        		olusers.add(objId);
        		olusers.add(clientId);}
        	}
   		 	for(String uid:olusers){
   		 		try {
					clients.get(uid).
					getBasicRemote().
					sendText(JSONObject.fromObject(notice).toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
   		 }
        }
	}
}
