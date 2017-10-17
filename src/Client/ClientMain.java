package Client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	        		
		        System.out.println(connectionToDataServer.sendFile("C:\\Users\\MONSTER\\Desktop\\asd.jpg"));
		        
		        
		    //    connectionToDataServer.Disconnect();
		   
	        	}else if(message.equalsIgnoreCase("sync check")){
	        		
	        		
	        		ArrayList<FileTuples> masterFileList = (ArrayList<FileTuples>) connectionToServer.syncCheck("sync check");
	        		ArrayList<FileTuples> clientFileList = getFilesFromFolder("DropSync1");
	        		ArrayList<FileTuples> inconsistencies = new ArrayList<>();
	        		FileTuples temp;
	        		boolean found = false;
	        		for(FileTuples m : masterFileList){
	        			found = false;
	        			for(FileTuples c : clientFileList){
	        				if(m.getName().equals(c.getName())){
	        					found = true;
	        					if(m.getHash().equals(c.getHash())){
	        						//consistent
	        					}else{
	        						//inconsistent
	        						if(m.getUpdateDate().compareTo(c.getUpdateDate())>0){
	        							//master newer
	        							temp = m;
	        							temp.setConsistency("Update(f)");
	        							inconsistencies.add(temp);
	        						}else{
	        							//client newer
	        							temp = c;
	        							temp.setConsistency("Update(m)");
	        							inconsistencies.add(temp);
	        						}
	        					}
	        				}	        				
		        			}	   
	        			if(!found){
	        				temp = m;
							temp.setConsistency("Add(f)");
							inconsistencies.add(temp);
	        			}
	        		}
	        		
	        		for(FileTuples c : clientFileList){
	        			found = false;
	        			for(FileTuples m : masterFileList){
	        				if(c.getName().equals(m.getName())){
	        					found=true;
	        				}
	        			}
	        			if(!found){
	        				temp = c;
							temp.setConsistency("Add(m)");
							inconsistencies.add(temp);
	        			}
	        		}
	        		System.out.println(inconsistencies);
//	        		System.out.println(fileList);
//	        		for(int i = 0; i< fileList.size();i++)
//	        		System.out.println(fileList.get(i).getName());

	        	}
	        	
	        	else{
	            System.out.println("Server says " + connectionToServer.SendForAnswer(message));
	        	}
	            message = scanner.nextLine();

	        }
	        
	        connectionToServer.Disconnect();
	    }
	
	public static ArrayList<FileTuples> getFilesFromFolder(String dir){
    	File folder = new File("D:\\"+dir);
		File[] listOfFiles = folder.listFiles();
		ArrayList<FileTuples> fileList = new ArrayList<>();
		
			for(int i = 0; i< listOfFiles.length;i++){
				//Use MD5 algorithm
				MessageDigest md5Digest = null;
				try {
					md5Digest = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				//Get the checksum
				String checksum = null;
				try {
					checksum = getFileChecksum(md5Digest, listOfFiles[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FileTuples tuple = new FileTuples(listOfFiles[i].getName(),checksum,new Date(listOfFiles[i].lastModified()));
				fileList.add(tuple);
				}
			

		return fileList;
    }
	private static String getFileChecksum(MessageDigest digest, File file) throws IOException
	{
	    //Get file input stream for reading the file content
	    FileInputStream fis = new FileInputStream(file);
	     
	    //Create byte array to read data in chunks
	    byte[] byteArray = new byte[1024];
	    int bytesCount = 0;
	      
	    //Read file data and update in message digest
	    while ((bytesCount = fis.read(byteArray)) != -1) {
	        digest.update(byteArray, 0, bytesCount);
	    };
	     
	    //close the stream; We don't need it now.
	    fis.close();
	     
	    //Get the hash's bytes
	    byte[] bytes = digest.digest();
	     
	    //This bytes[] has bytes in decimal format;
	    //Convert it to hexadecimal format
	    StringBuilder sb = new StringBuilder();
	    for(int i=0; i< bytes.length ;i++)
	    {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	     
	    //return complete hash
	   return sb.toString();
	}
	
	}


