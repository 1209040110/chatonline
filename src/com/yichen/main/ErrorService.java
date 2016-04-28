package com.yichen.main;

import java.util.Map;

import javax.websocket.Session;
import javax.websocket.server.PathParam;

public class ErrorService {
	public void processError(Throwable t,@PathParam("client-id") String clientId,Map<String, Session> clients){
		t.printStackTrace();
		clients.remove(clientId);
		System.out.println(clientId+" error");
		System.out.println("----------error");
		
	}
}
