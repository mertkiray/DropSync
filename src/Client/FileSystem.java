package Client;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import Objects.FileTuples;

public class FileSystem {
	public FileSystem()
    {
        
    }
	
	//asdada
	
	//asd
	
	public ArrayList<FileTuples> getFilesFromFolder(String dir){
    	File folder = new File("C:\\Users\\MONSTER\\Desktop\\"+dir);
		File[] listOfFiles = folder.listFiles();
		ArrayList<FileTuples> fileTuplesList = new ArrayList<>();
		for (int i = 0; i < listOfFiles.length; i++) {
				Date d = new Date(listOfFiles[i].lastModified());
		        FileTuples temp = new FileTuples(listOfFiles[i].getName(), d);
		        fileTuplesList.add(temp);
		        
		}
		return fileTuplesList;
    }
}
