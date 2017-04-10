package client;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import vfs.NotFileHandler;
import vfs.ServerInterface;
public class ClinetTest {
	public static byte[] filetobyte (String path) throws IOException{
		File file=new File(path);
		byte[] bytesArray=new byte[(int) file.length()];
		FileInputStream fileinputStream=new FileInputStream(file);
		fileinputStream.read(bytesArray);
		fileinputStream.close();
		return bytesArray;
	}
	public static byte deletefiledetail(String filepath){
		File f=new File(filepath);
		if(!f.isFile()){
			File [] list=f.listFiles();
		    if(list.length>0){
		    	for(int i=0;i<list.length;i++){
		    		if(list[i].isFile()){
		    			list[i].delete();
		    		}else{
		    			deletefiledetail(list[i].getPath());
		    		}
		    	}
		    }else{
		    	f.delete();
		    }
		}else{
			f.delete();
		}
		return 1;
	}
	 public static boolean deleteDir(File dir) {
		 //File dir=new File(path);
	      if (dir.isDirectory()) {
	         String[] children = dir.list();
	         for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir (new File(dir, children[i]));
	            
	            if (!success) {
	               return false;
	            }
	         }
	      }
	      return dir.delete();
	   }
	
	public static byte deletefile(String filepath){
		File f=new File(filepath);
		if(!f.isFile()){
			//deletefiledetail(filepath);
			deleteDir(f);
			return 1;
		}else{
			f.delete();
			return 1;
		}
	}
	public static void main(String []args) throws RemoteException, NotBoundException{
			//ServerInterface service=(ServerInterface)Naming.lookup("rmi://127.0.0.1/Remote_server");
			deletefile("/Users/ymyuen/Desktop/go_learning/TEXT2");
	}
}
