package vfs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface database extends Remote {
	
	 public void setConnection() throws SQLException,RemoteException;
	 public void createTable()throws SQLException,RemoteException;
	 public boolean login(String name,String password) throws SQLException,RemoteException;
	 public int getUserId(String name) throws SQLException,RemoteException;
	 public void insertUser(String UserName,String Password,String path)throws SQLException,RemoteException;
	 public void insertDisk(String DiskName,int Userid ,int size) throws SQLException,RemoteException;
	 public void dropDiskTable()throws SQLException,RemoteException;
	 public void dropUserTable()throws SQLException,RemoteException;
	 public void updateUserPassword(String name,String password)throws SQLException,RemoteException;
	 public void updateDiskName(String Newname,String Oldname) throws SQLException,RemoteException;
	 public void updateDiskSize(String DiskName ,int NEWSIZE) throws SQLException,RemoteException;
	 public ArrayList<Integer> GetDiskSize(String DiskName) throws SQLException,RemoteException;
	 public void deleteDISK(String DiskName )throws SQLException,RemoteException;
}
