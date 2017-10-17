package MultiThreadServer;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
 
public class MultiThreadedServer {
   ServerSocket myServerSocket;
   
   boolean ServerOn = true;
   public MultiThreadedServer() { 
      try {
         myServerSocket = new ServerSocket(8888);
         
      } catch(IOException ioe) { 
         System.out.println("Could not create server socket on port 8888. Quitting.");
         System.exit(-1);
      } 
		
      Calendar now = Calendar.getInstance();
      SimpleDateFormat formatter = new SimpleDateFormat(
         "E yyyy.MM.dd 'at' hh:mm:ss a zzz");
      System.out.println("It is now : " + formatter.format(now.getTime()));
      
      while(ServerOn) { 
         try { 
            Socket clientSocket = myServerSocket.accept();
            ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
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
	
   public static void main (String[] args) { 
	   new MultiThreadedServer();
   
   }

    class ClientServiceThread extends Thread { 
      Socket myClientSocket;
     
      boolean m_bRunThread = true; 
      public ClientServiceThread() { 
         super(); 
      } 
		
      ClientServiceThread(Socket s) { 
         myClientSocket = s; 
      } 
		
      public void run() { 
    	  DataInputStream dis = null;
   	   DataOutputStream dos = null;
   	   System.out.println(
            "Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());
         try { 
            dis = new DataInputStream(myClientSocket.getInputStream());
            dos = new DataOutputStream(myClientSocket.getOutputStream());
            

            while(m_bRunThread) { 
               String clientCommand = dis.readUTF(); 
               System.out.println("Client Says :" + clientCommand);
             
               if(!ServerOn) { 
                  System.out.print("Server has already stopped"); 
                  dos.writeUTF("Server has already stopped"); 
                  dos.flush(); 
                  m_bRunThread = false;
                  break;
               } 
               if(clientCommand.equalsIgnoreCase("quit")) {
                  m_bRunThread = false;
                  System.out.print("Stopping client thread for client : ");
                  break;
               } else if(clientCommand.equalsIgnoreCase("end")) {
                  m_bRunThread = false;
                  System.out.print("Stopping client thread for client : ");
                  ServerOn = false;
                  break;
               } else if(clientCommand.equalsIgnoreCase("send")){
                 	Scanner scanner = new Scanner(System.in);
            	   String meString = scanner.nextLine();
            	   dos.writeUTF(meString);
            	   dos.flush();
            	   scanner.close();
            
               }else if(clientCommand.equalsIgnoreCase("sendFile")){
            	   
            	   
            	   
                new MultiThreadedDataServer().start();       
                
                dos.writeUTF("started");
                dos.flush();

            	   
            	   /*
            	   
            	   
            	   	    	   
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
         		
         		fos.flush();
         		fos.close();
         		
         		out.println("done");
         		out.flush();
         		
         		System.out.println("Hello");
         		dis.close();
         		fos.close();
         		
         		*/
         		
         		
               }
            	   else{
            		   dos.writeUTF(clientCommand);
            		   dos.flush();
               }
             
            }
         } catch(Exception e) { 
            e.printStackTrace(); 
         } 
         finally { 
            try { 
           
            	dis.close();
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

   
        
   
