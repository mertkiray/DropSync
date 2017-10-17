package MultiThreadServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MultiThreadedDataServer extends Thread{
ServerSocket myServerSocket;

boolean ServerOn = true;
public MultiThreadedDataServer() { 
	
  
}

public void run(){

	 try {
	      myServerSocket = new ServerSocket(8889);
	      
	   } catch(IOException ioe) { 
	      System.out.println("Could not create server socket on port 8889. Quitting.");
	      System.exit(-1);
	   } 
	   
			
	   Calendar now = Calendar.getInstance();
	   SimpleDateFormat formatter = new SimpleDateFormat(
	      "E yyyy.MM.dd 'at' hh:mm:ss a zzz");
	   System.out.println("PORT 8889 IS ON");
	   
	   while(ServerOn) { 
	      try { 
	         Socket clientSocket = myServerSocket.accept();
	         ServerOn = false;
	         ClientServiceDataThread cliThread = new ClientServiceDataThread(clientSocket);
	         cliThread.start(); 
	      } catch(IOException ioe) { 
	         System.out.println("Exception found on accept. Ignoring. Stack Trace :"); 
	         ioe.printStackTrace(); 
	      }  
	   } 
	   try { 
	      myServerSocket.close(); 
	      System.out.println("Server Stopped"); 
	   } catch(Exception ioe) { 
	      System.out.println("Error Found stopping server socket"); 
	      System.exit(-1); 
	   } 
}





class ClientServiceDataThread extends Thread{
	   Socket myClientSocket;
	   boolean m_bRunThread = true; 
	
	      
	      public ClientServiceDataThread() { 
	         super(); 
	      } 
	      
	      ClientServiceDataThread(Socket s) { 
	          myClientSocket = s; 
	       } 
	      
	      
	      
	      public void run() { 
	    	  System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());
	    	   FileOutputStream fos = null;
	    	   DataInputStream dis = null;
	    	   DataOutputStream dos = null;
	     
	          
	          try { 
	        	  
	        
	        	  System.out.println(
	     	             "Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());
	        	  
	        	  
	        	  
	        	  dos = new DataOutputStream(myClientSocket.getOutputStream());
	        	  dis = new DataInputStream(myClientSocket.getInputStream());
               fos = new FileOutputStream("testfile.jpg");
               
	            
	          		byte[] buffer = new byte[4096];
	          		int filesize = 15123; // Send file size in separate msg
	          		int read = 0;
	          		int totalRead = 0;
	          		
	          	
	          		while((read = dis.read(buffer)) > -1) {
	          			
	          			System.out.println("read " + read + " bytes.");
	          			fos.write(buffer, 0, read);
	          		}
	          		
	          		//helloo
	          		/*
	          		dos.writeUTF("hello");
	          		dos.flush();
	          		*/
	          		
	                myServerSocket.close();

	          	
	             } catch(Exception e) { 
	             e.printStackTrace(); 
	          } 
	          finally { 
	             try { 
	            
	            		dis.close();
		          		fos.close();
		          		dos.close();
	                myClientSocket.close(); 
	                System.out.println("...Stopped"); 
	             } catch(IOException ioe) { 
	                ioe.printStackTrace(); 
	             } 
	          }
	       }
	 		   
}




}