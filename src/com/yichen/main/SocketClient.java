package com.yichen.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient {

	public static void main(String[] args){
		Socket socket=null;
		BufferedReader  br=null;
		InputStreamReader ins=null;
		OutputStreamWriter out=null;
		BufferedWriter bw=null;
		Scanner scanner=new Scanner(System.in);
		try{
			socket=new Socket("localhost",8080);
			ins=new InputStreamReader(socket.getInputStream());
			out=new OutputStreamWriter(socket.getOutputStream());
			br=new BufferedReader(ins);
			bw=new BufferedWriter(out);
			String sendContent;
			while((sendContent=scanner.nextLine())!=null)
			{
			bw.write("client:"+sendContent);
			bw.flush();}
			}catch(Exception ex){
				
			}finally{
				try {
					br.close();
					socket.close();
					bw.close();
					ins.close();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}
