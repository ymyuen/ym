package local;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import vfs.database;


public class databaseHandler extends UnicastRemoteObject implements database {
    private Connection con;
	public databaseHandler ()throws RemoteException{
		try {
			setConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setConnection() throws SQLException{
	SQLiteConfig config=new SQLiteConfig();
	config.setSharedCache(true);
	config.enableRecursiveTriggers(true);
	SQLiteDataSource ds=new SQLiteDataSource(config);
	ds.setUrl("jdbc:sqlite:sampleLocal.db");
	con=ds.getConnection();
	
  }
	
	public Connection getConnection(){
		return con;
	}
	
  public void createTable()throws SQLException{
     // String dropUsersql = "DROP TABLE IF EXISTS USER;";
      String createUsersql=" CREATE TABLE USER (  USERID  INTEGER  PRIMARY KEY AUTOINCREMENT, "+
                   "USERNAME           TEXT      UNIQUE,"+
    		       "PASSWORD           TEXT      UNIQUE,"+
                   "PATH               TEXT      UNIQUE);";
      		
    		//  "create table user (Userid int AUTOINCREMENT, UserName TEXT UNIQUE ,Password string ,Path TEXT UNIQUE );"
      
    //  String dropdisksql= "DROP TABLE IF EXISTS DISK ; ";
      String createdisksql="CREATE TABLE DISK (  DISKID  INTEGER  PRIMARY KEY AUTOINCREMENT,"+
    		        " DISKNAME           TEXT      UNIQUE,"+
    		         "USERID            INTEGER    ,"+
    		          "SIZE       INTEGER);";
      //String diskSQL="DROP TABLE IF EXISTS disk; create table disk (DISKid int AUTOINCREMENT,DiskName TEXT UNIQUE ,Userid int , SIZE int);";
      Statement stat = null;
      stat = con.createStatement();
     // stat.execute(dropUsersql);
     // stat.execute(dropdisksql);
      stat.execute(createUsersql);
      stat.execute(createdisksql);
  }
  public boolean login(String name,String password) throws SQLException{
	  String sql="SELECT COUNT(*) AS A FROM USER WHERE USERNAME ='"+name+"' AND PASSWORD='"+password+"';";
	  Statement stat=con.createStatement();
	  ResultSet set=stat.executeQuery(sql);
	  int d=0;
	  while(set.next()){
		  d=set.getInt("A");
		  System.out.println(d);
	  }
	 // System.out.println(d);
	  if(d==0){
		  return false;
	  }else{
		  return true;
	  }
  }
  public int getUserId(String name) throws SQLException{
	  String sql="SELECT USERID FROM USER WHERE USERNAME='"+name+"';";
	  Statement stat=con.createStatement();
	  int id=0;
	  ResultSet  set=stat.executeQuery(sql);
	  while(set.next()){
		  id=set.getInt("USERID");
	  }
	  return id;
  }

  public boolean checkUsernameVaild(String name) throws SQLException{
	  String sql="SELECT USERNAME FROM USER WHERE USERNAME='"+name+"';";
	  Statement stat=con.createStatement();
	  ResultSet  set=stat.executeQuery(sql);
	  if(set.next()==true){
		  return false;
	  }else{
	     return true;
	  }
  }
  
  public boolean checkPassword(String name, String password) throws SQLException{
	  String oldpassword = null;
	  String sql="SELECT PASSWORD FROM USER WHERE USERNAME='"+name+"';";
	  Statement stat=con.createStatement();
	  ResultSet  set=stat.executeQuery(sql);
	  while(set.next()){
		  oldpassword = set.getString("PASSWORD");
	  }
	  if (oldpassword == password){
		  return true;
	  }else{
		  return false;
	  }
  }
 
  public void insertUser(String UserName,String Password,String path)throws SQLException{
      String sql = "INSERT INTO USER (USERNAME,PASSWORD,PATH) VALUES ('"+UserName+"','"+Password+"','"+path+"')";
      Statement stat =con.createStatement();
      stat.executeUpdate(sql);
  }
  public void insertDisk(String DiskName,int Userid ,int size) throws SQLException{
	  String sql="INSERT INTO DISK (DISKNAME,USERID,SIZE) VALUES ('"+DiskName+"',"+Userid+","+size+");";
	  Statement stat=con.createStatement();
	  stat.executeUpdate(sql);
  }
  
  public void dropDiskTable()throws SQLException{
      String sql = "DROP TABLE DISK ";
      Statement stat = null;
      stat = con.createStatement();
      stat.executeUpdate(sql);
  }
  
  public void dropUserTable()throws SQLException{
      String sql = "DROP TABLE USER";
      Statement stat = null;
      stat = con.createStatement();
      stat.executeUpdate(sql);
  } 
  public void updateUserPassword(String name,String password)throws SQLException{
      String sql = "UPDATE USER SET PASSWORD ='"+password +"' WHERE USERNAME='"+name+"';";
      Statement pst = null;
      pst = con.createStatement();
      pst.executeUpdate(sql);
  }
  public void updateDiskName(String Newname,String Oldname) throws SQLException{
	  String sql="UPDATE DISK SET DISKNAME ='"+Newname+"' WHERE DISKNAME='"+Oldname+"';";
	  Statement pst = null;
      pst = con.createStatement();
      pst.executeUpdate(sql);
  }
  public void updateDiskSize(String DiskName ,int NEWSIZE) throws SQLException{
	  String sql="UPDATE DISK SET SIZE="+NEWSIZE+" WHERE DISKNAME ='"+DiskName+"';";
	  Statement pst = null;
      pst = con.createStatement();
      pst.executeUpdate(sql);
  }
  
  public ArrayList<Integer> GetDiskSize(String DiskName) throws SQLException{
	  ArrayList<Integer> sizelist=new ArrayList<Integer>();
	  String sql="SELECT SIZE FROM DISK WHERE DISKNAME ='"+DiskName+"';";
	  Statement stat=con.createStatement();
	  ResultSet rs=stat.executeQuery(sql);
	  while(rs.next()){
		  sizelist.add(rs.getInt("SIZE"));
	  }
	  return sizelist;
  }  

  public void deleteDISK(String DiskName )throws SQLException{
      String sql = "DELETE FROM  DISK WHERE DISKNAME='"+DiskName+"';"; 
      Statement pst = null;
      pst = con.createStatement();
      pst.executeUpdate(sql);
      
  }

  public void listDisk() throws SQLException{
	  String sql="SELECT * FROM DISK;";
	  Statement stat=con.createStatement();
	  ResultSet rs=stat.executeQuery(sql);
	  System.out.println("DISKID  DISKNAME USERID  SIZE");
	  while(rs.next()){
		  System.out.print(rs.getInt("DISKID")+"\t");
		  System.out.print(rs.getString("DISKNAME")+"\t");
		  System.out.print(rs.getInt("USERID")+"\t");
		  System.out.println(rs.getInt("SIZE")+"\t");
		  
	  }
	  
  }
  public void listUser() throws SQLException{
	  String sql="SELECT * FROM USER;";
	  Statement stat=con.createStatement();
	  ResultSet rs=stat.executeQuery(sql);
	  System.out.printf("Userid  UserName Password Path\n");
	  while(rs.next()){
		  System.out.print(rs.getInt("USERID")+"\t");
		  System.out.print(rs.getString("USERNAME")+"\t");
		  System.out.print(rs.getString("PASSWORD")+"\t");
		  System.out.println(rs.getString("PATH")+"\t");		  
	  }
  }
  //TestCode
  public static void main(String[] args)  {
	/*  databaseHandler data=new databaseHandler();
	  try {
		Connection co=data.getConnection();
		data.createTable(co);
		data.insertUser(co,"Jason","Password","Jason");
		data.insertDisk(co,"FirstDisk",1,1024);
		data.insertDisk(co,"SecondDisk",2,1024);
		data.listUser(co);
		data.listDisk(co);
		data.updateUserPassword(co,"Jason","1234");
		data.listUser(co);
		data.updateDiskName(co, "Updata_Disk","FirstDisk");
		data.listDisk(co);
		data.updateDiskSize(co,"Updata_Disk",2048);
		data.listDisk(co);
		data.deleteDISK(co, "SecondDisk");
		data.listDisk(co);
		ArrayList<Integer> size=data.GetDiskSize(co,"Updata_Disk");
		System.out.println(size.get(0));
		System.out.println("Login  :"+data.login(co, "Jason","1234"));
		int id=data.getUserId(co, "Jason");
		System.out.println(id);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		System.out.println(e.getMessage());
	}*/
	  try {
     	database service =new databaseHandler();
		Naming.rebind("Remote_DataBase", service);
		System.out.println("Service running");
		/*service.createTable();
		service.insertUser("YmYuen", "1234", "YmYuen");
		if(service.login("YmYuen","1234"))
		{
			System.out.println("Login OK");
		}*/
		//service.createTable();
		//service.insertUser("YmYuen", "1234", "YmYuen");
		//System.out.println(service.getUserId("YmYuen"));
	} catch (RemoteException   e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  
	  
  }
  
}
