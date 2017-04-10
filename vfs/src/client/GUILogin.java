package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import vfs.ServerInterface;
import vfs.database;

public class GUILogin {
	database service;
	private String ip_address;
	private String file;
	
    ServerInterface system; 
	public void setUp() {
		try {
			 service=(database)Naming.lookup("rmi://"+ip_address+"/Remote_DataBase");
			 System.out.print("ip");
			 system=(ServerInterface) Naming.lookup("rmi://"+ip_address+"/Remote_server");
			 try {
				service.createTable();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void setupGUI(){
		JFrame setupframe=new JFrame();
		JPanel mainGUI =new JPanel(new GridLayout(3,2));
		JLabel ip=new JLabel("IP address");
		JLabel save_folder=new JLabel("save folder");
		JTextField ip_text =new JTextField();
		JTextField path=new JTextField();
		JButton save=new JButton("save");
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 ip_address = ip_text.getText();
				 file=path.getText();
				 System.out.println(file+ip_address);
				 setUp();
				 System.out.println(file+ip_address);
				 loginGUI();
				setupframe.setVisible(false);
				System.out.println("d");
				
			}
			
		});
		mainGUI.add(ip);
		mainGUI.add(ip_text);
		mainGUI.add(save_folder);
		mainGUI.add(path);
		mainGUI.add(save);
		setupframe.add(BorderLayout.CENTER,mainGUI);
		setupframe.setSize(300, 150);
		setupframe.setVisible(true);
		
	}
	public static void main(String []args){
		
		GUILogin gui=new GUILogin();
		gui.setupGUI();
		
		
	}
	public boolean AccountMessage(Boolean d){
		JFrame MessageFrame=new JFrame();
		String message="";
		if(d){
			message+="Create Account OK";
		}else{
			message+="Create Account error";
		}
		JLabel Label=new JLabel(message);
		JButton OK= new JButton("OK");
		OK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				MessageFrame.setVisible(false);
			}
			
		});

		MessageFrame.add(BorderLayout.CENTER, Label);
		MessageFrame.add(BorderLayout.SOUTH,OK);
		MessageFrame.setSize(150, 100);
		MessageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MessageFrame.setVisible(true);
		return false;
		
	}
	public void loginError(){
		JFrame login= new JFrame();
		JLabel message =new JLabel("Login Error");
		JButton OK=new JButton("OK");
		OK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				login.setVisible(false);
			}
			
		});
		login.add(BorderLayout.CENTER,message);
		login.add(BorderLayout.SOUTH,OK);
		login.setSize(200, 150);
		login.setVisible(true);
		
		
	}
	public boolean loginGUI(){
		JFrame loginFrame=new JFrame();
		JPanel display=new JPanel(new GridLayout(3,3));
		JLabel username=new JLabel("username");
		JLabel password=new JLabel("password");
		JTextField userTextField=new JTextField();
		JPasswordField passwordText=new JPasswordField();
		
		username.setLabelFor(userTextField);
		password.setLabelFor(passwordText);
		JButton login=new JButton("login");
		JButton create=new JButton("Create Account");
		create.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean flag=false;
				System.out.println(userTextField.getText().toString()+passwordText.getPassword());

			    if(!userTextField.getText().toString().isEmpty()&&!passwordText.getPassword().toString().isEmpty()){
			    	try {
					    service.insertUser(userTextField.getText().toString(),new String(passwordText.getPassword()),userTextField.getText().toString());
					    int id=service.getUserId(userTextField.getText().toString());
					    if(id!=0){
					    	flag=true;
					    	system.setCurrentpath(file);
					        system.CreateFloder(system.getPath(), userTextField.getText().toString());
					    }
						AccountMessage(flag);

					    
			    	} catch (SQLException  e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    		
			    }
			   
				
			}
			
		});
		login.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!userTextField.getText().toString().isEmpty()&&!passwordText.getPassword().toString().isEmpty()){
				try {
					System.out.println(userTextField.getText().toString()+new String(passwordText.getPassword()));
					
					if(service.login(userTextField.getText().toString(), new String(passwordText.getPassword()))){
						System.out.println("Login Ok");
						GUIDisk one=new GUIDisk(system.getPath()+File.separator+userTextField.getText(),userTextField.getText(),service,system,service.getUserId(userTextField.getText()));
						one.DiskGUI();
						loginFrame.setVisible(false);
					}else{
						loginError();
					}
					
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				}
				
			}
			
		});
		display.add(username);
		display.add(userTextField);
		display.add(password);
		display.add(passwordText);
		display.add(login);
		display.add(create);
		loginFrame.add(BorderLayout.CENTER, display);
		loginFrame.setSize(400,200);
		loginFrame.setVisible(true);
		return false;
		
	}

}
