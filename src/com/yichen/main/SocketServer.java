package com.yichen.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	
	public static void main(String[] agrs){
		ServerSocket serverSocket=null;
		Socket socket=null;
		BufferedReader  br=null;
		InputStreamReader ins=null;
		OutputStreamWriter out=null;
		BufferedWriter bw=null;
		try{
		serverSocket=new ServerSocket(8080);
		socket=serverSocket.accept();
		ins=new InputStreamReader(socket.getInputStream());
		out=new OutputStreamWriter(socket.getOutputStream());
		br=new BufferedReader(ins);
		bw=new BufferedWriter(out);
		String str;
		while((str=br.readLine())!=null){
			System.out.println(str);
		}
		bw.write("server:lallala");
		bw.flush();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				br.close();
				socket.close();
				bw.close();
				ins.close();
				out.close();
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
