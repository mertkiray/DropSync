package MultiThreadServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MultiThreadedDataServer implements Runnable{
ServerSocket myServerSocket;
private boolean sendToClient = false;
String fileToClient = null;
boolean ServerOn = true;
final String MASTERPATH = Constants.Constants.MASTERPATH;
final String FOLLOWERPATH = Constants.Constants.FOLLOWERPATH;
private String fileName;

public MultiThreadedDataServer() { 
	System.out.println("Mult");
}
public MultiThreadedDataServer(String fileName) { 
	this.fileName=fileName;
	
}


public MultiThreadedDataServer(boolean sendToClient,String file){
	System.out.println("Hello");
	this.sendToClient = sendToClient;
	fileToClient = file;

}

public void run(){
	 try {
	      myServerSocket = new ServerSocket(8889);
	      
	   } catch(IOException ioe) { 
	      System.out.println("Could not create server socket on port 8889. Quitting.");
	      System.exit(-1);
	   } 
	   
	 System.out.println("SEND OR GET"+sendToClient+"");
			
	   Calendar now = Calendar.getInstance();
	   SimpleDateFormat formatter = new SimpleDateFormat(
	      "E yyyy.MM.dd 'at' hh:mm:ss a zzz");
	   System.out.println("PORT 8889 IS ON");
	   
	   while(ServerOn) { 
	      try { 
	         Socket clientSocket = myServerSocket.accept();
	         ServerOn = false;
	         ClientServiceDataThread cliThread;
	         if(!sendToClient){
	          cliThread = new ClientServiceDataThread(clientSocket, fileName);
	          Thread t = new Thread(cliThread);
	          t.start();
	         }
	         else{
	         cliThread = new ClientServiceDataThread(clientSocket,sendToClient,fileToClient);
	         Thread t = new Thread(cliThread);
	         t.start();
	         }
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





class ClientServiceDataThread implements Runnable{
	   Socket myClientSocket;
	   boolean m_bRunThread = true; 
	 private  boolean sendToClient = false;
	   String fileToClient;
	   private String fileName;
	   
	
	      
	      public ClientServiceDataThread() { 
	         super(); 
	      } 
	      
	      ClientServiceDataThread(Socket s, String fileName) { 
	          myClientSocket = s; 
	          this.fileName = fileName;
	       } 
	      
	      ClientServiceDataThread(Socket s, boolean sendToClient,String fileToClient){
	    	  myClientSocket = s;
	    	  this.sendToClient = sendToClient;
	    	  this.fileToClient = fileToClient;
	      }
	      
	      
	      
	      public void run() { 
	    	  System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());
	    	   FileOutputStream fos = null;
	    	   DataInputStream dis = null;
	    	   DataOutputStream dos = null;
	    	    FileInputStream fis = null;

	     
	          
	          try { 
	        	  
	    
	        	  dos = new DataOutputStream(myClientSocket.getOutputStream());
	        	  dis = new DataInputStream(myClientSocket.getInputStream());
               fos = new FileOutputStream(MASTERPATH+fileName);
               
	            if(!sendToClient){
	            	
	            	System.out.println("SEND SEND");
	            	
	          		byte[] buffer = new byte[4096];
	          		int filesize = 15123; // Send file size in separate msg
	          		int read = 0;
	          		int totalRead = 0;
	          		
	          	
	          		while((read = dis.read(buffer)) > -1) {
	          			
	          			System.out.println("read " + read + " bytes.");
	          			fos.write(buffer, 0, read);
	          		}
	          		
	            } else{
	            
	            	System.out.println("SEND ASD");

	            	System.out.println("Started Sending");

	          	  fis = new FileInputStream(fileToClient);
	          	  
	          	byte[] buffer = new byte[4096];
	    		
	    		while (fis.read(buffer) > -1) {
	    			dos.write(buffer);
	    		}
	    			            	
	            	System.out.println("Finished Sending");
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