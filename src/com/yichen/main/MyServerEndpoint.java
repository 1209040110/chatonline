package com.yichen.main;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


import javax.enterprise.inject.New;
import javax.servlet.jsp.tagext.TryCatchFinally;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.yichen.entity.Doctor;
import com.yichen.entity.Patient;
import com.yichen.entity.User;
import com.yichen.util.Const;
import com.yichen.util.MyQueue;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@ServerEndpoint(value="/myShow/{myid}/{objid}")
public class MyServerEndpoint {
	 private Session session;
	 public  static final SimpleDateFormat DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 private static Map<String,Session> onlineMap= new HashMap<String, Session>();
	 //这个队列若医生在线，则对头元素为医生正在聊天的患者，若医生不在线，则为等待的患者队列
	 //聊天完才poll
	 private static Map<String,Queue<String>> paQueueMap=new HashMap<String,Queue<String>>();
	 //医生objid为empty
	 @OnOpen
	 public void onOpen(Session session,@PathParam("myid") String myId,
			 @PathParam("objid") String objId){
		 try{
		 if(onlineMap.get(myId)!=null)
		 {
			 session.getBasicRemote().sendText("你的用户id已被登录！请更换id");
			 session.close();//会自动调用onerror事件与onclose事件(先onerror后onclose）
		 } 
		 else
		 {
			 System.out.println("57--"+onlineMap);
			 System.out.println("57--"+paQueueMap);
			 this.session=session;
			 onlineMap.put(myId,session);
			 System.out.println("60--"+onlineMap);
			 if(objId.equals(Const.DOCTOR_OBJID)){//医生start
				 Queue<String> paqu=paQueueMap.get(myId);
				 System.out.println("62---"+paqu);
				 if(paqu==null||paqu.peek()==null){
					 System.out.println("64---");
					 session.getBasicRemote().sendText("当前没有患者向您咨询！");
				 }
				 else if(onlineMap.get(paqu.peek())==null){
					 System.out.println("67---");
					 paqu.poll();
					 while(paqu.peek()!=null&&onlineMap.get(paqu.peek())==null)
						 paqu.poll();
					 if(paqu.peek()!=null)
					 {
						 String pa=paqu.peek();
						 onlineMap.get(pa).getBasicRemote().sendText("医生"+myId+"上线啦！你们可以聊天了");
						 session.getBasicRemote().sendText("你当前和患者"+pa+"聊天");
					 }else{
						 session.getBasicRemote().sendText("当前没有患者向您咨询！");
					 }
				 }else
				 	{
					 System.out.println("79---");
					 String pa=paqu.peek();
					 onlineMap.get(pa).getBasicRemote().sendText("医生"+myId+"上线啦！你们可以聊天了");
					 session.getBasicRemote().sendText("你当前和患者"+pa+"聊天");
				 	}
					 
				 }//医生end
			 
			 else//患者start
			 {
				 if(onlineMap.get(objId)==null)//医师不在线
				 {
					 if(paQueueMap.get(objId)==null){
						 Queue<String> qu=new LinkedList<String>();
						 boolean f=qu.offer(myId);
						 if(f) paQueueMap.put(objId, qu);
						 else{
							 System.out.println("队列已满！");
						 }
						 
					 }else{
						 paQueueMap.get(objId).add(myId);
					 }
					 session.getBasicRemote().sendText("您咨询的医师不在线上，请等待");
				 }else{//医师在线
					 if(paQueueMap.get(objId)==null||paQueueMap.get(objId).peek()==null){
						 Queue<String> qu=new LinkedList<String>();
						 qu.add(myId);
						 paQueueMap.put(objId, qu);
						 session.getBasicRemote().sendText("您咨询的医师当前在线且空闲，可以聊天了");
						 onlineMap.get(objId).getBasicRemote().sendText("患者"+myId+"将向您咨询！");
					 }else{
						 System.out.println("116---"+paQueueMap.get(objId)==null);
						 System.out.println("117---"+paQueueMap.get(objId));
						 paQueueMap.get(objId).add(myId);
						 session.getBasicRemote().sendText("您咨询的医师正忙，请等待");
					 }
				 }
				
			 }//患者end
			 
		 }
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			
		}
	 } 
	 
	 @OnMessage
	 public void onMessage(String message,Session session,
			 @PathParam("myid") String myId,
			 @PathParam("objid") String objId){
		 try{
			 String datetimeStr=DATE_FORMAT.format(new Date());
			 String sendContent=myId+"&nbsp;&nbsp;&nbsp;"+datetimeStr+"<br/>"+message;
			 if(objId.equals(Const.DOCTOR_OBJID)){//医师start
				 Queue<String> queue=paQueueMap.get(myId);
				 if(queue!=null&&queue.peek()!=null&&onlineMap.get(queue.peek())!=null){
					 session.getBasicRemote().sendText(sendContent);
					 onlineMap.get(queue.peek()).getBasicRemote().sendText(sendContent);
				 } 
				 else{
					 //不处理
				 }
			 }//医师end
			 else{//患者start
				 if(onlineMap.get(objId)!=null&&
						 paQueueMap.get(objId)!=null&&paQueueMap.get(objId).peek().equals(myId))
				 {
					 session.getBasicRemote().sendText(sendContent);
					 onlineMap.get(objId).getBasicRemote().sendText(sendContent);
				 }else{
					 //不处理
				 }
					 
			 }//患者end
		 }catch(IOException ex){
			 ex.printStackTrace();
		 }finally{
			 
		 }
			
		
			
		
	     
	 }


	 @OnClose
	 public void onClose(@PathParam("myid") String myId,@PathParam("objid") String objId,
			 CloseReason reason,Session session) {
		try{
			String sendContent="<p style='color:red;'>"+myId+"已下线</p>";
			System.out.println("1---"+sendContent);

			if(onlineMap.get(myId)!=null&&session.getId().equals(onlineMap.get(myId).getId())){//判断非法用户 en
			if(objId.equals(Const.DOCTOR_OBJID))
			{//doctor start
				Queue<String> queue=paQueueMap.get(myId);
				if(queue!=null&&queue.peek()!=null&&onlineMap.get(queue.peek())!=null){
					 onlineMap.get(queue.peek()).getBasicRemote().sendText(sendContent);
					 System.out.println("2---");
				 } 
				 else{
					 //不处理
					 System.out.println("3---");
				 }
			}//doctor end
			else{//patient start
				if(onlineMap.get(objId)!=null&&
						 paQueueMap.get(objId)!=null&&paQueueMap.get(objId).peek().equals(myId))
				 {
					 onlineMap.get(objId).getBasicRemote().sendText(sendContent);
					 paQueueMap.get(objId).poll();
					 if(paQueueMap.get(objId).peek()!=null){
						 onlineMap.get(objId).getBasicRemote().sendText(paQueueMap.get(objId).peek()+"将向您咨询！");
						 onlineMap.get(paQueueMap.get(objId).peek()).getBasicRemote().sendText("你和医师可以聊天了");
					 }
					 System.out.println("203---"+paQueueMap.get(objId).peek());
				 }else if(paQueueMap.get(objId)!=null&&!paQueueMap.get(objId).peek().equals(myId))
				 {
					 System.out.println("197---");
					 MyQueue<String> queproces=new MyQueue<String>();
					 queproces.delAElem(myId, paQueueMap.get(objId));
				 }else{
					 //不处理了
					 System.out.println("202---");
				 }
			}//patient end
			
			}
			System.out.println("205--"+onlineMap);
			System.out.println("206----"+onlineMap.containsKey(myId));
			if(onlineMap.containsKey(myId)&&session.getId().equals(onlineMap.get(myId).getId())){
				onlineMap.remove(myId);
				System.out.println("4---");
			}
			if(session.isOpen()){
				session.close(reason);
				System.out.println("5---");
			}
		
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			
		}
	 } 
	 
	 //error事件发生后close事件也发生  用户只要一关闭窗口则触发error事件
	 @OnError
	 public void onError(@PathParam("myid") String myId,@PathParam("objid") String objId,
			 Throwable t,Session session) {
		 t.printStackTrace();
	 }
}
