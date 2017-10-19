package MultiThreadServer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderContinueErrorException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.users.FullAccount;

public class DropBoxFileManagement {
	private static final String ACCESS_TOKEN = "iuSPKVmoYaUAAAAAAAABGFY5mDtx-vz1bHiyrrBcG0zG0M-N-pStqWaMwmHvlEpD";
    @SuppressWarnings("deprecation")
	private static final DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
    private static final DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
    
    
	public DropBoxFileManagement(){       
        
        FullAccount account = null;
		try {
			account = client.users().getCurrentAccount();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createFolder(String name){
		 try {
	            FolderMetadata folder = client.files().createFolder(name);
	            System.out.println(folder.getName());
	        } catch (CreateFolderErrorException err) {
	            if (err.errorValue.isPath() && err.errorValue.getPathValue().isConflict()) {
	                System.out.println("Something already exists at the path.");
	            } else {
	                System.out.print("Some other CreateFolderErrorException occurred...");
	                System.out.print(err.toString());
	            }
	        } catch (Exception err) {
	            System.out.print("Some other Exception occurred...");
	            System.out.print(err.toString());
	        }
		}
	
	public ArrayList<Metadata> getAlreadyUploadedFiles(){
		ArrayList<Metadata> alreadyUploadedFiles = new ArrayList<>();
		ListFolderResult result = null;
		try {
			result = client.files().listFolder("");
		} catch (ListFolderErrorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DbxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
		    for (Metadata metadata : result.getEntries()) {
		        alreadyUploadedFiles.add(metadata);
		    }

		    if (!result.getHasMore()) {
		        break;
		    }

		    try {
				result = client.files().listFolderContinue(result.getCursor());
			} catch (ListFolderContinueErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return alreadyUploadedFiles;
	}
	
	public void uploadFile(String fileName, String path){
		try (InputStream in = new FileInputStream(path)) {
		    try {
				FileMetadata metadata = client.files().uploadBuilder("/"+fileName)
				    .uploadAndFinish(in);
			} catch (UploadErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteFile(String path){
		try {
			client.files().delete(path);
		} catch (DeleteErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public void downloadFile(String fileName, String path){
//		//output file for download --> storage location on local system to download file
//        OutputStream downloadFile = null;
//		try {
//			downloadFile = new FileOutputStream(path);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        try
//        {
//        try {
//			FileMetadata metadata = client.files().downloadBuilder("/"+fileName)
//			        .download(downloadFile);
//		} catch (DbxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        }
//        finally
//        {
//            try {
//				downloadFile.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
//	}
}
