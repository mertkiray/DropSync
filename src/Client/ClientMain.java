package Client;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientMain {
	
	

	public static void main(String[] args) throws IOException {
		ConnectionToServer connectionToServer = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, ConnectionToServer.DEFAULT_SERVER_PORT);
	        connectionToServer.Connect();
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter a message for the echo");

	        
	        String message = scanner.nextLine();

	        while (!message.equals("QUIT"))
	        {
	        	
	        	if(message.equals("sendFile")){
	        		
	        		connectionToServer.SendForAnswer("sendFile");
	        	
	        	ConnectionToDataServer connectionToDataServer = new ConnectionToDataServer(ConnectionToDataServer.DEFAULT_SERVER_ADDRESS, ConnectionToDataServer.DEFAULT_SERVER_PORT);
	        	
	        	connectionToDataServer.Connect();
	        		
		        System.out.println(connectionToDataServer.sendFile("C:\\Users\\Mert\\Desktop\\asd.jpg"));
		        
		        
		    //    connectionToDataServer.Disconnect();
		        
		        
		        
	        	}else{
	            System.out.println("Server says " + connectionToServer.SendForAnswer(message));
	        	}
	            message = scanner.nextLine();

	        }
	        
	        connectionToServer.Disconnect();
	    }

	}


