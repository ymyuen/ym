package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.w3c.dom.NameList;

import vfs.ServerInterface;
import vfs.database;

public class GUIDisk {
	private database service;
	private int userid;
    private ServerInterface system;
	private String currentpath;
	private String username;
	private ArrayList<File> disklist=new ArrayList<File>();
	private ArrayList<Integer> disksize=new ArrayList<Integer>();
	private int selectedfile=0;
	
    public GUIDisk(String path ,String username,database service ,ServerInterface system,int userid){
    	this.currentpath=path;
    	this.username=username;
    	this.service=service;
    	this.system=system;
    	this.userid=userid;
    	try {
			File[] location=system.readfloder(this.currentpath);
			if(location!=null &&location.length!=0){
				for(int i=0;i<location.length;i++){
					disklist.add(location[i]);
					ArrayList<Integer> tem=service.GetDiskSize(location[i].getName());
					if(tem.size()>0&&tem.size()==1){
						int te=tem.get(0)-system.getDiskCurrentSize(location[i].getPath());
						disksize.add(te);
					}
					
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
	public static void main(String [] args){
	  	//GUIDisk gui=new GUIDisk();
	  	//gui.DiskGUI();
	  	
	}
	public void updatelist(DefaultListModel nameModel,DefaultListModel sizeModel ,JList disk,JList size){
		nameModel.clear();
		disklist.clear();
		disksize.clear();
		sizeModel.clear();
		try {
			File [] fi=system.readfloder(currentpath);
			if(fi!=null&&fi.length>0){
				for(int i=0;i<fi.length;i++){
					nameModel.addElement(fi[i].getName());
					disklist.add(fi[i]);
					ArrayList<Integer> tem=service.GetDiskSize(fi[i].getName());
					if(tem.size()>0&&tem.size()==1){
						int te=tem.get(0)-system.getDiskCurrentSize(fi[i].getPath());
						disksize.add(te);
						sizeModel.addElement(Integer.toString((te)));
					}
					
				}
			}
			disk.setSelectedIndex(selectedfile);
			size.setSelectedIndex(selectedfile);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	public void CreateGUI (DefaultListModel nameModel,DefaultListModel sizeModel ,JList disk,JList size){
		JFrame gui=new JFrame();
		JPanel panel=new JPanel(new GridLayout(3,2));
		JLabel name=new JLabel("Disk name");
		JLabel maxSize=new JLabel("Disk max size");
		JTextField nameText=new JTextField();
		JTextField maxSizeText=new JTextField();
		JButton create=new JButton("Create Disk");
		panel.add(name);
		panel.add(nameText);
		panel.add(maxSize);
		panel.add(maxSizeText);
		panel.add(create);
		create.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					service.insertDisk(nameText.getText(),userid,Integer.parseInt(maxSizeText.getText()));
					system.CreateFloder(currentpath, nameText.getText());
					gui.setVisible(false);
				    updatelist(nameModel,sizeModel,disk,size);
				    

				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		gui.add(BorderLayout.CENTER,panel);
		gui.setSize(400, 150);
		gui.setVisible(true);
		
	}
	public void updateDiskName(DefaultListModel nameModel,DefaultListModel sizeModel ,JList disk,JList size){
		JFrame gui=new JFrame();
		JPanel mainGUI=new JPanel(new GridLayout(2,2));
		JLabel label=new JLabel("Name Of Disk");
		JTextField data=new JTextField();
	    JButton update=new JButton("Update");
	    mainGUI.add(label);
	    mainGUI.add(data);
	    mainGUI.add(update);
	    update.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					system.renamefile(disklist.get(selectedfile).getPath(), data.getText());
					
					service.updateDiskName(data.getText(),disklist.get(selectedfile).getName());
					gui.setVisible(false);
					updatelist(nameModel,sizeModel,disk,size);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    	
	    });
	    gui.add(BorderLayout.CENTER, mainGUI);
	    gui.setSize(400, 120);
	    gui.setVisible(true);
	}
	public void updateDiskSize(DefaultListModel nameModel,DefaultListModel sizeModel ,JList disk,JList size){
		JFrame gui=new JFrame();
		JPanel mainGUI=new JPanel(new GridLayout(2,2));
		JLabel label=new JLabel("SIze Of Disk");
		JTextField data=new JTextField();
	    JButton update=new JButton("Update");
	    update.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					service.updateDiskSize(disklist.get(selectedfile).getName()	,Integer.parseInt(data.getText()));
					
					updatelist(nameModel,sizeModel,disk,size);
					gui.setVisible(false);

				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    	
	    });
	    mainGUI.add(label);
	    mainGUI.add(data);
	    mainGUI.add(update);
	    gui.add(BorderLayout.CENTER, mainGUI);
	    gui.setSize(400, 120);
	    gui.setVisible(true);
	}
	public void DiskGUI(){
		JFrame gui=new JFrame();
		JPanel mainGUI=new JPanel(new GridLayout(2,2));
	    JScrollPane scroll=new JScrollPane();
	    DefaultListModel nameModel=new DefaultListModel();
	    DefaultListModel sizeModel=new DefaultListModel();
	    if(disklist.size()>0){
	    	for(int i=0;i<disklist.size();i++){
	    		nameModel.addElement(disklist.get(i).getName());
	    		sizeModel.addElement(Integer.toString(disksize.get(i)));
	    	}
	    }
		JList namelist=new JList(nameModel);
		JList sizelist=new JList(sizeModel);
		namelist.addMouseListener( new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				String path=currentpath+File.separator+disklist.get(selectedfile).getName();
				GUIMain ma=new GUIMain(path,username,service,system,disksize.get(selectedfile));
				ma.GUIDick();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		namelist.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				JList a=(JList)e.getSource();
				sizelist.setSelectedIndex(a.getSelectedIndex());
				selectedfile=a.getSelectedIndex();
			}
			
		});
		sizelist.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				String path=currentpath+File.separator+disklist.get(selectedfile).getName();
				
				GUIMain ma=new GUIMain(path,username,service,system,disksize.get(selectedfile));
				ma.GUIDick();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		sizelist.addListSelectionListener( new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				JList b=(JList)e.getSource();
				namelist.setSelectedIndex(b.getSelectedIndex());
				selectedfile=b.getSelectedIndex();
				
			}
			
		});
		JPanel buttonGUI=new JPanel(new GridLayout(4,1));
		JButton createDisk=new JButton("Create Disk");
		createDisk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CreateGUI(nameModel,sizeModel,namelist,sizelist);
				
			}
			
		});
		JButton delteDisk=new JButton("Delete Disk");
		delteDisk.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					system.deletefile(disklist.get(selectedfile).getPath());
					service.deleteDISK(disklist.get(selectedfile).getPath());
					updatelist(nameModel,sizeModel,namelist,sizelist);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		JButton rename=new JButton("Rename Disk");
		rename.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateDiskName(nameModel,sizeModel,namelist,sizelist);
			}
			
		});
		JButton resize=new JButton("Resize Disk");
		resize.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateDiskSize(nameModel,sizeModel,namelist,sizelist);
			}
			
		});
		
		buttonGUI.add(createDisk);
		buttonGUI.add(delteDisk);
		buttonGUI.add(rename);
		buttonGUI.add(resize);
		namelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sizelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		namelist.setSelectedIndex(0);
		sizelist.setSelectedIndex(namelist.getSelectedIndex());
		mainGUI.add(new JLabel("Disk Name"));
		mainGUI.add(new JLabel("Disk size(Byte)"));
		mainGUI.add(namelist);
		mainGUI.add(sizelist);
		scroll.setViewportView(mainGUI);
		
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.add(BorderLayout.CENTER,scroll);
		gui.add(BorderLayout.EAST,buttonGUI);
		gui.setSize(700, 100);
		gui.setVisible(true);
		
	}
}
