package MultiThreadServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.dropbox.core.v2.files.Metadata;

import Client.FileTuples;

public class ConsoleReader implements Runnable{

	Scanner sc;
	public final static String FOLLOWERPATH = "C:\\Users\\Mert\\Desktop\\DropSync1\\";
	public final static String MASTERPATH = "C:\\Users\\Mert\\Desktop\\DropSync\\";
	
	public ConsoleReader(){
		
	}
	
	
	@Override
	public void run() {
		sc = new Scanner(System.in);
	   	   DropBoxFileManagement dropBoxFileManagement = new DropBoxFileManagement();

		
		while(true){
			
			if(sc.nextLine().equalsIgnoreCase("dropbox sync")){
   SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
            	   
            	   
            	   
            	   BufferedReader br = null;
              		FileReader fr = null;
              		
              		String lastActionDateString="";
               	   try {

              			//br = new BufferedReader(new FileReader(FILENAME));
              			fr = new FileReader("LastAction");
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
               	   
            	   
            	   
            	   
            	   
            	   
            	   
            	           	   
            	   
            	   
            	   
            	   
            	   
            	   
            	   
            	   
            	   String ans= "The following files are going to be synchronized with the Dropbox\n";
            	   boolean updateNeeded = false;
            	   ArrayList<Metadata> alreadyUploadedFiles = dropBoxFileManagement.getAlreadyUploadedFiles();
            	   ArrayList<FileTuples> filesInFolder = getFilesFromFolder("DropSync");
            	   boolean found;
            	   for(FileTuples x : filesInFolder){
            		   found = false;
            		   for(Metadata y : alreadyUploadedFiles){
            			   if(x.getName().equals(y.getName())){
            				   found=true;
            				   
            				   if(lastActionDate.before(x.getUpdateDate())){
            					   //newer
            					   updateNeeded=true;
                    			   dropBoxFileManagement.deleteFile(y.getPathLower());
            					   dropBoxFileManagement.uploadFile(x.getName(), MASTERPATH+x.getName());
            					   ans+=x.getName()+" "+"update" +" "+ x.getSize()/1048576+" MB\n";
            				   }
            			   }
            		   }
            		   if(!found){
            			   //upload to dropbox
            			   updateNeeded=true;
            			   System.out.println("upload "+x.getName());            			   

            			   dropBoxFileManagement.uploadFile(x.getName(), MASTERPATH+x.getName());
            			   ans+=x.getName()+" "+"add" +" "+ x.getSize()+"\n";
            			   
            		   }
            	   }
            	   
            	   for(Metadata y : alreadyUploadedFiles){
            		   found = false;
            		   for(FileTuples x : filesInFolder){
            			   if(x.getName().equals(y.getName())){
            				   found = true;
            			   }
            		   }
            		   if(!found){
            			   updateNeeded=true;
            			   dropBoxFileManagement.deleteFile(y.getPathLower());
            			   ans+=y.getName()+" "+"delete" +" "+ "\n";
            		   }
            	   }
            	   
            	   
            	   
            	   
            	   
            	   
            	   ans+="Synchronization done with Dropbox";
            	   if(!updateNeeded){
            		   ans="No update is needed. Already synced!";
            	   }
            	
            	   System.out.println(ans);
            	   
    			   Date date = new Date();
    			   

    			   
    			   PrintWriter writer = null;
				try {
					writer = new PrintWriter("lastAction", "UTF-8");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			   writer.println(dateFormat.format(date));
    			   writer.close();
//            	   dropBoxFileManagement.uploadFile("meme.jpg", PATH+"meme.jpg");
            	   
            	   
            	   
			}
			
		}
		
	}
	

	public ArrayList<FileTuples> getFilesFromFolder(String dir){

    	File folder = new File(MASTERPATH);

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
   

}
