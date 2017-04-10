package vfs;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.*;
import java.util.ArrayList;

public interface ServerInterface extends Remote{
	public String setCurrentpath(String path) throws RemoteException;
	public String getPath() throws RemoteException;
    public byte renamefile(String filepath,String newName)throws RemoteException;
	public byte deletefile(String filepath)throws RemoteException;
	public byte moveFile(String currentpath,String newPath) throws RemoteException;
	public byte createfile(String currentpath,String Filename) throws RemoteException;
	public ArrayList<File> SearchFile(String diskpath ,String Searchword) throws RemoteException;
	public byte copyfloder(String orgpath) throws RemoteException, NotFileHandler;
	public byte copyfile( String orgpath)throws RemoteException;
	public void saving(String savedpath , byte[] filearray) throws RemoteException, IOException;
	public byte[] filetobyte (String path) throws RemoteException, FileNotFoundException, IOException;
	public File[] readfloder (String Filepath) throws RemoteException;
	public void CreateFloder(String path ,String name) throws RemoteException;
	public int getDiskCurrentSize(String path) throws RemoteException;
}
