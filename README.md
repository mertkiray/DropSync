# Comp416
COMP 416 PROJECT 1

MERT KIRAY
DENİZ TOPRAK

DROP SYNC PROJECT


				OVERVIEW
In this project, we implemented a Dropbox Sync that works on the application layer project. The project aims to sync the folder of the user with the Dropbox folder that is capable of handling multiple clients.   
	There are two packages in our project which is “Client” and “MultiThreadServer”. We can think them as two separate projects working as the follower and the master respectively.  
	First of all, we created our ServerSocket which can handle multiple clients by running the client socket connections on separate threads. After the client socket accepted and the connection between the master and follower is established the master starts waiting for client commands. The follower can execute a sync check on the dropbox folder to see the deleted/added/updated files in the master and sync his/her files with the dropbox by getting the files from the master. 
	The master can execute a dropbox sync method to sync its files with the Dropbox folder. When the follower added/deleted/updated the files in the master, the master can execute these required actions to sync itself with the dropbox.
	
				IMPLEMENTATION
	We named the DropSync folder in the follower as DropSync1 and the DropSync folder in the master as DropSync. These can be changed in the code.
To establish the data flow between the master and the follower, we created the socket connections between them and used DataInputStream and DataOutputStream java classes to fetch,send the commands and two retrieve and send the data between the followers and the masters.  We send the commands and the data between the master and the follower as bytes. 
To divide the file in chunks we get the size of the file and decided on the port number to be opened. After the port size is determined we opened the data sockets as new threads and wait for the connection to be established. After that we decided how much size the buffers that is used to send and retrieve the data to be. 
After the ports are opened and the buffer size is determined we started to send the data through the socket. To retrieve the file, we should have synchronized the data coming from the sockets. For this purpose we used synchronized(buffer) loop to retrieve the data in a synchronized way to be sure we created the file with the correct bytes as the file is sent.  So we will make sure the data is not corrupted or combined falsely. If the data is corrupted in the socket, we catch this error in the catch area of the combining data and tell other threads to wait for the data to be sent again. When the data sent again, if it passes the validity check, other threads can continue executing the combining part. After we had sent or retrieve the data 
However, we had some issues after we implement these methods. We had some problems while determining the file size because file.getLength() method which should return us the file size, returned always 0 and the socket connections could not run as the expected way. So we decided to cancel these and decided to move on with only one socket. However, with more debugging we could have achieved what is the problem and could be implemented.
We have a class to store the files called FileTuples.java. FileTuples(name,hash,updateDate) which has other attributes named, consistency and size. We used this object while determining if we should update,delete or add the file to master and the client.
To sync with the master with the Dropbox, we used Dropbox Core Api v2. 
We created an application in the Dropbox. Than we need to connect to our Dropbox account with the Dropbox core Api. For this purpose, we created a separate class named DropboxFileManagement.java. We linked our account with the core api with the access token we get after created the app.
We have functions named: getAlreadyUploadedFiles(), deleteFile(path),uploadFile(fileName,path).
getAlreadyUploadedFiles returns the files in the linked Dropbox account app folder. 
deleteFile, deletes the file in the path.
uploadFile, creates a new file with the file name in the Dropbox account app folder.

			





Master Implementation
Master listens the console and the data retrieved from the socket. These are the commands that can be executed with the command coming from the follower. 
-dropbox sync
-delete
-sendFile
-getFile
-uploadFile
-sync check

Functions in the master:
-getFilesFromFolder(directory) 
-getFileCheckSum 

getFilesFromFolder returns the files in the specified directory  as FileTuples. Also calls getFileCheckSum function to hash the file.
getFileCheckSum(MessageDigest,file) hashes the file with the specified MessageDigest hash algorithm.
		
			Follower Implementation
Follower listens to the console and sends the commands to the master via the socket connection so that the master will execute the appropriate operations.  The commands expected by the follower is:
-sync check
-dropbox sync
-sync <filename>
Sync check commands request the list of files in the masters DropSync folder. After that, compares them with the folders in its own DropSync folder and creates a list of inconsistent files and displays them.
Dropbox Sync command, transmit the command to the master to execute it.
Sync <filename> sync the file in the followers DropSync  folder with the file in the masters DropSync folder.

					Conclusion
	In this project we implemented a server/client application that can handle multiple clients by creating a multithreaded server socket. The follower can update its DropSync folder with the masters DropSync folder which syncs itself with the given Dropbox folder. 
	We learned how to create sockets, how socket communication works and how can we send data from the socket. We also learnt how to create multiple data sockets by creating them as threads and merge these data in one file. 
	We worked on Dropbox api and learned how to execute commands to interact with the Dropbox. 
	We used source control applications(Git) to work better on the project as a team which increased our team collaboration.
