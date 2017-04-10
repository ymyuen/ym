package local;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.JPasswordField;
import vfs.databaseHandler;


public class clinet {
	String username;
	int userID;
	String password;
	String repeatPassword;
	Scanner scanner = new Scanner(System.in);
	JPasswordField passwordField = new JPasswordField(10);
	
	public boolean login() throws SQLException, RemoteException{
		
		int option;
		String password;
		String repeatPassword;
		boolean login = false;
		local.databaseHandler db = new local.databaseHandler();
		Connection con=db.getConnection();
		
		System.out.println("Welcome to VFS system!");
			do{
				System.out.println("Please select:");
				System.out.println("1. Login");
				System.out.println("2. Register");
				System.out.println("3. Exit");
				option = scanner.nextInt();
				
				switch(option) {
					case 1:
						System.out.println("\n\n ====================Login====================\n");
						System.out.println("Please enter username:");
						username = scanner.nextLine();
						System.out.println("Please enter password:");
						char[] input = passwordField.getPassword();
						password = new String(input);
						db.login(username,password);
						
						if(db.login(username,password)){
							System.out.println("Login success!");
							login = true;
						}
						break;
					
					case 2:
						System.out.println("\n\n ====================Register====================\n");
						System.out.println("Please enter username:");
						username = scanner.nextLine();
						do{
							System.out.println("Please enter password:");
							input = passwordField.getPassword();
							password = new String(input);
							System.out.println("Please repeat password:");
							input = passwordField.getPassword();
							repeatPassword = new String(input);
							if(password == repeatPassword){
								if(db.checkUsernameVaild(username)){
									db.insertUser( username, password, username);
								}else{
								System.out.println("username already taken!");
								}
							}
							else{
								System.out.println("Password and reapeat password doesn't match!");
								}
							}
							while(password != repeatPassword);
							break;
						
					case 3:
						return login;
						
					default:
						System.out.println("Please select option correctly!");
						break;
		   	}
				}
			while(!login);
			return login;
				}
	
	
		public void userMenu() throws SQLException, RemoteException{
			
			int option;
			int disksize;
			String diskname;
			vfs.databaseHandler db = new vfs.databaseHandler();
			userID = db.getUserId(username);
			
			System.out.println("====================VFS SYSTEM====================" );
			System.out.println("Welcome "+username+"!" );
			System.out.println("Please select option:");
			System.out.println("1. Add disk:");
			System.out.println("2. delete disk:");
			System.out.println("3. update disk size:");
			System.out.println("4. updateUserPassword:");
			System.out.println("5. Exit:");
			option = scanner.nextInt();
			
			do{
				switch(option){
						
					case 1:
						System.out.println("Please input diskname:");
						diskname = scanner.nextLine();
						System.out.println("Please input size:");
						disksize = scanner.nextInt();
						db.insertDisk(diskname,userID ,disksize);
						break;
						
					case 2:
						System.out.println("Plesae input the disk name you would like to delete:");
						diskname = scanner.nextLine();
						db.deleteDISK(diskname);
						break;
					
					case 3:	
						System.out.println("Please input the disk name you would like to update it's size:");
						diskname = scanner.nextLine();
						System.out.println("Please input the new disk size:");
						disksize = scanner.nextInt();
						db.updateDiskSize(diskname, disksize);
						break;
					
					case 4:
						System.out.println("Please enter old password:");
						char[] input = passwordField.getPassword();
						password = new String(input);
						if(db.login(username, password)){
							System.out.println("Please enter new password:");
							input = passwordField.getPassword();
							password = new String(input);
							db.updateUserPassword(username, password);}
						else{
							System.out.println("Old password doesn't match!");
						}
						break;
					
					case 5:
						System.out.println("Bye!");
						main();
						break;
						
					default:
						System.out.println("Please input select a vaild option!");
						break;
					}
				}
			while(option != 5);
			
			
		}
		
		
		
		public void main() throws SQLException, RemoteException{
			boolean loginVaild;
			loginVaild = login();
			if(loginVaild){
				userMenu();
			}
			
			
		}
	}

