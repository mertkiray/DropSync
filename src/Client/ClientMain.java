package Client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ClientMain {
	
	//helloo

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
		   
	        	}else if(message.equalsIgnoreCase("sync check")){
	        		
	        		
	        		ArrayList<FileTuples> fileList = (ArrayList<FileTuples>) connectionToServer.syncCheck("sync check");
	        		
	        		System.out.println(fileList);
	        		for(int i = 0; i< fileList.size();i++)
	        		System.out.println(fileList.get(i).getName());

	        	}
	        	
	        	else{
	            System.out.println("Server says " + connectionToServer.SendForAnswer(message));
	        	}
	            message = scanner.nextLine();

	        }
	        
	        connectionToServer.Disconnect();
	    }
	
	public ArrayList<FileTuples> getFilesFromFolder(String dir){
    	File folder = new File("C:\\Users\\Mert\\Desktop\\"+dir);
		File[] listOfFiles = folder.listFiles();
		ArrayList<FileTuples> fileList = new ArrayList<>();
		
			for(int i = 0; i< listOfFiles.length;i++){
				FileTuples tuple = new FileTuples(listOfFiles[i].getName(),new Date(listOfFiles[i].lastModified()));
				fileList.add(tuple);
				}
			

		return fileList;
    }
	
	}


