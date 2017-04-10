package vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerSystem extends UnicastRemoteObject implements ServerInterface {
   private String Currentpath;
   public ServerSystem() throws RemoteException{}
   public String setCurrentpath(String path ){
	   boolean flag=false;
	   if(path.contains("../")&&this.Currentpath!=null){
		   String []tmefile=path.split("[.]");
		   File current=new File(Currentpath);
		   if(tmefile[tmefile.length-1].equals("/")){
			     path=current.getParent();
		   }else{
			      path = current.getParent()+tmefile[tmefile.length-1];

		   }
		   flag=true;
		  // System.out.println("parent ");
       }
	   if(path.contains("./")&&this.Currentpath!=null&&flag!=true){
		  String [] tmefiles=path.split("[.]");
		  if(tmefiles[tmefiles.length-1].equals("/")){
			     path=Currentpath;
		   }else{
				path=Currentpath+tmefiles[tmefiles.length-1];

		   }
		  
	   }
	   String [] pathlist=path.split("/");
	   
	   File file=new File(path);
	  // file.isDirectory()
	   if(file.isFile()&&file.exists()){
		   return pathlist[pathlist.length-1]+" is not directory";
	   }else if(!file.exists()){
		   return pathlist[pathlist.length-1]+" do not exist";
	   }else if(!file.isFile()&&file.exists()){
		   this.Currentpath=path;
		   return "Update success";
	   }
	   return "set path Error";
   }
   public String getPath(){
	   return Currentpath;
   }
public File[] readfloder (String Filepath) {
			 
		 File floder =new File(Filepath);
		 if(!floder.exists()){
			 //throw error file not exists
			System.out.println("No such file ");
		 }else{
		     File [] listofFile=floder.listFiles();
		     return listofFile;
		 }
		 File[] nullfil= new File[0];
		 return nullfil;
	}
	public byte createfloder(String currentpath ,String Floderename) throws NotFileHandler{
		String filepath=currentpath+File.separator+Floderename;
		File a=new File(filepath);
		if(a.mkdirs()){		
			System.out.println("Create floder "+Floderename+" success");
			return 1;
		}else{
			throw new NotFileHandler();
		}
		
	}
	public byte renamefile(String filepath,String newName){
		String[] comp=filepath.split("/");
		String filename=comp[comp.length-1];
		String filetyp="";
        File orfile=new File(filepath);
        if(orfile.isFile()&&filename.indexOf('.')!=-1){
        	String []tmp=filename.split("[.]");
        	//System.out.println(tmp.length);
        	filetyp+=tmp[1];
        }
       
		System.out.println("filename :"+filename);
		String Apath="";
		File file=new File(filepath);
		for(int i=0;i<(comp.length-1);i++){
			Apath+=File.separator+comp[i];
		}
		Apath+=File.separator+newName;
		if(orfile.isFile()&&filename.indexOf('.')!=-1){
			Apath+="."+filetyp;
		}
        System.out.println("Apath:"+Apath);
		if(file.renameTo(new File(Apath))){
		  return 1;	
		}else{
		  System.out.println("Remane File Error");
		  return 0;
		}
	}
	public boolean deleteDir(File dir) {
		
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
	
	public byte deletefile(String filepath){
		File f=new File(filepath);
		if(!f.isFile()){
			deleteDir(f);
			return 1;
		}else{
			f.delete();
			return 1;
		}
	}
	public byte moveFile(String currentpath,String newPath){
		File ofile=new File(currentpath);
		File newloca=new File(newPath);
		if(!newloca.isFile()){
		   String []filenamelist=currentpath.split("/");
		   String filename=filenamelist[filenamelist.length-1];
		   newPath+=File.separator+filename;
		   System.out.println("newPath: "+newPath);
		   if(ofile.renameTo(new File(newPath))){
			   System.out.println("Move file success");
			   return 1;
		   }else{
			   System.out.println("Move File error");   
			   return 0;
		   }
		}else{
			System.out.println("The destination cannot be a file");
			return 0;
		}
	}
	public byte createfile(String currentpath,String Filename ){
		String filepath=currentpath+File.separator+Filename;
		File f=new File(filepath);
		try {
			f.createNewFile();
			System.out.println("Create file "+Filename+" success");
			return 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public ArrayList<File> SearchFile(String diskpath ,String Searchword){
	   ArrayList<File> filelist=new ArrayList<File>();
	   File disk =new File (diskpath);
	   if(!disk.isFile()){
		  File [] fileindisk=disk.listFiles();
		  for(int i=0;i<fileindisk.length;i++){
		     if(fileindisk[i].getName().contains(Searchword))
		    {
		      filelist.add(fileindisk[i]);
		      if(!fileindisk[i].isFile()){
		    	  ArrayList<File> templist=SearchFile(fileindisk[i].getAbsolutePath(),Searchword);
		    	  filelist.addAll(templist);
		      }
		    }else if(fileindisk[i].isDirectory()&&!fileindisk[i].getName().contains(Searchword)){
		    	  ArrayList<File> templist=SearchFile(fileindisk[i].getAbsolutePath(),Searchword);
		    	  filelist.addAll(templist);
		     } 
		    }
		      
	   }		     
	   return filelist;
	}
	public byte copyfloder(String orgpath) throws NotFileHandler{
		InputStream inStream=null;
		OutputStream outStream=null;
		String[] fileArray=orgpath.split("/");
		String filename="";
		File copyedfloder=null;
		int copyedindex=0;
		File orfloder=new File(orgpath);
		if(!orfloder.isFile()){
			do{
			filename=fileArray[fileArray.length-1]+"_copy_"+copyedindex;
			copyedfloder=new File(orfloder.getParent()+File.separator+filename);
			//copyedfloder.mkdir();
			copyedindex++;
			}while(copyedfloder.exists());
			this.createfloder(orfloder.getParent(),filename);
			File[] listfile=orfloder.listFiles();
			for(int i=0;i<listfile.length;i++){
				copyfloderdetail(listfile[i],orfloder.getParent()+File.separator+filename);
			}
			
			return 1;
		}
		
		return 0;
		
	}
	public int getDiskCurrentSize(String path){
		int size=0;
		 File a=new File(path);
		 if(a.exists()){
			 size=(int) a.length();
		 }
		return size;
	}
	public void CreateFloder(String path ,String name){
		String filepath=path+File.separator+name;
		File floder=new File(filepath);
		floder.mkdir();
		
	}
	public void copyfloderdetail(File f , String current){
		String name=current+File.separator+f.getName();
		if(!f.isFile()){
			File file=new File(name);
			file.mkdirs();
			File[] list=f.listFiles();
			for(int i=0;i<list.length;i++){
				copyfloderdetail(list[i],name);
			}
		}else{
		   	File file=new File(name);
		   	try {
				file.createNewFile();
				FileInputStream fi=new FileInputStream(f);
				FileOutputStream fo=new FileOutputStream(file);
				byte[] buffer=new byte[1024];
				int length=0;
				while((length=fi.read(buffer))>0){
					fo.write(buffer, 0, length);
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   	
			
		}
	}
	
	public byte copyfile( String orgpath){
		InputStream inStream=null;
		OutputStream outStream=null;
		String[] fileArray=orgpath.split("/");
		String filename="";
		File copyedfile=null;
		int copyedindex=0;
		do{
		for(int i=0;i<fileArray.length-1;i++){
			filename+=fileArray[i]+"/";
		} 
		System.out.println(fileArray[fileArray.length-1]);
		if(fileArray[fileArray.length-1].indexOf('.')!=-1){
		    String[] nameArray=fileArray[fileArray.length-1].split("[.]");
		    filename+=nameArray[0]+"_Copy_"+copyedindex+"."+nameArray[1];
		}else{
			filename+=fileArray[fileArray.length-1]+"_Copy_"+copyedindex;
		}
		copyedfile=new File(filename);
		filename="";
		copyedindex++;
		}while(copyedfile.exists());
		System.out.println(filename);
		File orFile=new File(orgpath);
		
		try {
		    copyedfile.createNewFile();
			inStream=new FileInputStream(orFile);
			outStream=new FileOutputStream(copyedfile);
			byte[] buffer=new byte[1024];
			int length=0;
			while((length=inStream.read(buffer))>0){
				outStream.write(buffer, 0, length);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return 0;
	}
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
	
	public static void main(String[] args) {
		try{
			ServerInterface service=new ServerSystem();
			Naming.rebind("Remote_server", service);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}
}
