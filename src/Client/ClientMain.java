package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ClientMain {
	final static String MASTERPATH = Constants.Constants.MASTERPATH;
	final static String FOLLOWERPATH = Constants.Constants.FOLLOWERPATH;
	//helloo

	public static void main(String[] args) throws IOException {
		ConnectionToServer connectionToServer = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, ConnectionToServer.DEFAULT_SERVER_PORT);
	        connectionToServer.Connect();
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter a message for the echo");

	        
	        String message = scanner.nextLine();

	        while (!message.equals("QUIT"))
	        {
	        	
	        	if(message.contains("sendFile")){
	        		
	        		String[] messageParsed = message.split(" ");
	        		String fileName = messageParsed[1];
	        		sendFile(connectionToServer, fileName);
		        	
		        
		        
		    //    connectionToDataServer.Disconnect();
		   
	        	}else if(message.contains("getFile")){
	        		String[] messageParsed = message.split(" ");
	        		String fileName = messageParsed[1];
	        		getFile(connectionToServer, fileName);

	        		
	        	}
	        	
	        	else if(message.equalsIgnoreCase("sync check")){
	        		ArrayList<FileTuples> inconsistencies = syncCheck(connectionToServer);
	        		double totalSize=0;
	        		for(FileTuples tuple : inconsistencies){
	        			System.out.println(tuple);
	        			totalSize+=tuple.getSize();
	        		}
	        		
	        		System.out.println("The total size of the updates is "+new DecimalFormat("#.##").format(totalSize/1048576)+" MB");
	        		

	        	}else if(message.contains("dropbox sync")){
	        		System.out.println(connectionToServer.SendForAnswer(message));
	        	}
	        	else if(message.contains("sync")){
	        		String[] messageParsed = message.split(" ");
	        		String fileName = messageParsed[1];
	        		ArrayList<FileTuples> inconsistencies = syncCheck(connectionToServer);
	        		for(FileTuples tuple : inconsistencies){
	        			if(tuple.getName().equals(fileName)){
	        				if(tuple.getConsistency().equalsIgnoreCase("add(f)") || tuple.getConsistency().equalsIgnoreCase("update(f)")){
	        					getFile(connectionToServer, fileName);
	        				}else if(tuple.getConsistency().equalsIgnoreCase("add(m)") || tuple.getConsistency().equalsIgnoreCase("update(m)")){
	        					sendFile(connectionToServer, fileName);
	        				}else if(tuple.getConsistency().equalsIgnoreCase("delete(f)")){
	        					File file = new File(FOLLOWERPATH+fileName);
	        					file.delete();
	        				}else if(tuple.getConsistency().equalsIgnoreCase("delete(m)")){
	        					System.out.println("silcem");
	        					System.out.println(connectionToServer.SendForAnswer("delete "+fileName));
	        				}
	        			}
	        		}
	        		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
	        		Date date = new Date();	     		   
	     		   PrintWriter writer = null;
	     		try {
	     			writer = new PrintWriter("DropSyncAction", "UTF-8");
	     		} catch (FileNotFoundException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		} catch (UnsupportedEncodingException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		}
	     		   writer.println(dateFormat.format(date));
	     		   writer.close();
	        		
	        		
	        	}
	        	
	        	else{
	            System.out.println("Server says " + connectionToServer.SendForAnswer(message));
	        	}
	            message = scanner.nextLine();

	        }
	        
	        connectionToServer.Disconnect();
	    }
	
	public static ArrayList<FileTuples> getFilesFromFolder(String dir){
    	File folder = new File(FOLLOWERPATH);
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
				tuple.setSize(listOfFiles[i].length());
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
	
	public static ArrayList<FileTuples> syncCheck(ConnectionToServer connectionToServer){
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
 	   
 	   
 	   
 	   BufferedReader br = null;
   		FileReader fr = null;
   		
   		String lastActionDateString="";
    	   try {

   			//br = new BufferedReader(new FileReader(FILENAME));
   			fr = new FileReader("DropSyncAction");
   			br = new BufferedReader(fr);

   			String sCurrentLine;

   			while ((sCurrentLine = br.readLine()) != null) {
   				lastActionDateString = sCurrentLine;
   			}

   		} catch (IOException e) {

   			e.printStackTrace();

   		} finally {

   			try {

   				if (br != null)
   					br.close();

   				if (fr != null)
   					fr.close();

   			} catch (IOException ex) {

   				ex.printStackTrace();

   			}

   		}
    	   Date date1 = new Date();
    	
    	Date lastActionDate = null;
    	   try{
    	   lastActionDate = dateFormat.parse(lastActionDateString);
    	   }catch (ParseException e) {
            e.printStackTrace();
    	   }	
		
		
		
		
		
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
				if(lastActionDate.before(m.updateDate)){
					temp = m;
					temp.setConsistency("Add(f)");
					inconsistencies.add(temp);
				}else if(lastActionDate.after(m.updateDate)){
					temp = m;
					temp.setConsistency("Delete(m)");
					inconsistencies.add(temp);
				}
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
				if(lastActionDate.before(c.updateDate)){
					temp = c;
					temp.setConsistency("Add(m)");
					inconsistencies.add(temp);
				}else if(lastActionDate.after(c.updateDate)){
					temp = c;
					temp.setConsistency("Delete(f)");
					inconsistencies.add(temp);
				}
			}
		}
		
//		System.out.println(fileList);
//		for(int i = 0; i< fileList.size();i++)
//		System.out.println(fileList.get(i).getName());
		return inconsistencies;
	}
	public static void getFile(ConnectionToServer connectionToServer, String fileName){
		
		connectionToServer.SendForAnswer("getFile "+fileName);

    	ConnectionToDataServer connectionToDataServer = new ConnectionToDataServer(ConnectionToDataServer.DEFAULT_SERVER_ADDRESS, ConnectionToDataServer.DEFAULT_SERVER_PORT);

    	connectionToDataServer.Connect();

        System.out.println(connectionToDataServer.getFile(FOLLOWERPATH+fileName));
	}
	
	public static void sendFile(ConnectionToServer connectionToServer, String fileName){
		connectionToServer.SendForAnswer("sendFile "+fileName);
    	
    	ConnectionToDataServer connectionToDataServer = new ConnectionToDataServer(ConnectionToDataServer.DEFAULT_SERVER_ADDRESS, ConnectionToDataServer.DEFAULT_SERVER_PORT);
    	
    	connectionToDataServer.Connect();
    		
        try {
			System.out.println(connectionToDataServer.sendFile(FOLLOWERPATH+fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	}


