package Testingfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileToArrayOfBytes {
	
	public void saving(String savedpath , String getpath) throws IOException{
		byte[] bfile=filetobyte(getpath);
		Path path=Paths.get(savedpath);
		Files.write(path, bfile);
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
		if(!file.isFile()){
			String Filepath=CurrentPath+File.separator+file.getName();
			File floder=new File(Filepath);
			floder.mkdirs();
			File [] filelist=file.listFiles();
			for(int i=0;i<filelist.length;i++){
				if(!filelist[i].isFile()){
					savingFloder(filelist[i].getPath(),Filepath);
				}else {
				    saving(CurrentPath+File.separator+filelist[i].getName(),filelist[i].getPath());
					
				}
			}
		}
		
	}
	
	
	public static void main(String []args){
		FileToArrayOfBytes filemove=new FileToArrayOfBytes();
	}

}
