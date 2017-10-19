package Client;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Yahya Hassanzadeh on 20/09/2017.
 */

public class ConnectionToDataServer
{
    public static final String DEFAULT_SERVER_ADDRESS = "192.168.1.27";
    public static final int DEFAULT_SERVER_PORT = 8889;
    final String MASTERPATH = Constants.Constants.MASTERPATH;
	final String FOLLOWERPATH = Constants.Constants.FOLLOWERPATH;
    private Socket s;
    //private BufferedReader br;
    private DataOutputStream dos;
    private FileInputStream fis;
	private FileOutputStream fos;
    private DataInputStream dataInputStream;
    //asdasdas
    
    protected String serverAddress;
    protected int serverPort;

    /**
     *
     * @param address IP address of the server, if you are running the server on the same computer as client, put the address as "localhost"
     * @param port port number of the server
     */
    public ConnectionToDataServer(String address, int port)
    {
        serverAddress = address;
        serverPort    = port;
    }

    /**
     * Establishes a socket connection to the server that is identified by the serverAddress and the serverPort
     */
    public void Connect()
    {
        try
        {
            s=new Socket(serverAddress, serverPort);
            //br= new BufferedReader(new InputStreamReader(System.in));
            /*
            Read and write buffers on the socket
             */
      
            dataInputStream = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            

   		 
            System.out.println("Successfully connected to " + serverAddress + " on port " + serverPort);
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            System.err.println("Error: no server has been found on " + serverAddress + "/" + serverPort);
        }
    }

   
    
    
    public void Disconnect()
    {
        try
        {    
            dos.close();
            dataInputStream.close();
            //br.close();
            s.close();
            System.out.println("ConnectionToServer. SendForAnswer. Connection Closed");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    //hellooo
    
    public String sendFile(String file) throws IOException {
        String response = new String();


       
    	  fis = new FileInputStream(file);
		
		byte[] buffer = new byte[4096];
		
		while (fis.read(buffer) > -1) {
			dos.write(buffer);
		}
        
		dos.flush();
		fis.close();
		
		/*
		  try {
				response = dataInputStream.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		
		Disconnect();
		  
			
			return response;
	}
    
    public String getFile(String file){
    	 String response = new String();
    	 
     	System.out.println("Started Getting");
     	try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	 
    	byte[] buffer = new byte[4096];
   		int filesize = 15123; // Send file size in separate msg
   		int read = 0;
   		int totalRead = 0;
   		
   	
   		try {
			while((read = dataInputStream.read(buffer)) > -1) {
				fos.write(buffer, 0, read);
			}
			
			fos.close();
	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		
 
   		
		Disconnect();

   		    	 
    	 return response;
    	
    }
    
}
