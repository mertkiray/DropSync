package Client;

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
	        	
	            System.out.println("Server says " + connectionToServer.SendForAnswer(message));
	            message = scanner.nextLine();
	        }
	        
	        connectionToServer.Disconnect();
	    }

	}


