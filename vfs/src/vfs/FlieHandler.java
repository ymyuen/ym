package vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FlieHandler {
	
	public void saving(String savedpath , byte[] filearray) throws IOException{
		Path path=Paths.get(savedpath);
		Files.write(path, filearray);
	}
	public byte[] filetobyte (String path) throws IOException{
		File file=new File(path);
		byte[] bytesArray=new byte[(int) file.length()];
		FileInputStream fileinputStream=new FileInputStream(file);
		fileinputStream.read(bytesArray);
		fileinputStream.close();
		return bytesArray;
	}
	
	public void savingFloder(String path,String CurrentPath) throws IOException{
		File file=new File(path);
		String[] filepathlist=path.split("/");
		if(!file.isFile()){
			String Filepath=CurrentPath+File.separator+file.getName();
			File floder=new File(Filepath);
			floder.mkdirs();
			File [] filelist=file.listFiles();
			for(int i=0;i<filelist.length;i++){
				if(!filelist[i].isFile()){
					savingFloder(filelist[i].getPath(),Filepath);
				}else {
				    saving(Filepath+File.separator+filelist[i].getName(),filetobyte(filelist[i].getPath()));
					
				}
			}
		}
		
	}
	
	

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		FlieHandler file=new FlieHandler();
		//System.out.println("Hello ");
		try {
			file.savingFloder("/Users/ymyuen/Desktop/go_learning/TEXT","/Users/ymyuen/Desktop");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
