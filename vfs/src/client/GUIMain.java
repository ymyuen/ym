package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import vfs.NotFileHandler;
import vfs.ServerInterface;
import vfs.database;

public class GUIMain {
	private String currentpath;
	private String username;
	private ArrayList<File> file=new ArrayList<File>();
	private ArrayList<String> type=new ArrayList<String>();
	private int selectedfile=0;
    private ServerInterface system;
	private database service;
	private String diskpath;
	private String selectForMove;
	private int avaliableSize;
  
	public GUIMain(String currentpath ,String username ,database service ,ServerInterface system,int avaliable){
		this.currentpath=currentpath;
		this.username=username;
		this.service=service;
		this.system=system;
		this.diskpath=currentpath;
		this.avaliableSize=avaliable;
		File[] filelist;
		try {
			filelist = system.readfloder(currentpath);
			if(filelist!=null&&filelist.length>0){
				   for(int i=0;i<filelist.length;i++){
					   file.add(filelist[i]);
					   if(system.readfloder(filelist[i].getPath())==null){
						   type.add("File");
					   }else{
						   type.add("Floder");
					   }
				   }
			   }
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		}
	
   public static void main(String []args){
	 //  GUIMain gui=new GUIMain();
	 //  gui.GUIDick();
   }
	public void saving(String savedpath ,byte[] a) throws IOException{
		byte[] bfile=a;
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
	/*public void savingFloder(String clientpath,String serverPath) throws IOException{
		File file=new File(clientpath);
		if(!file.isFile()){
			String Filepath=+File.separator+file.getName();
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
		
	}*/
   
	public void updatelist(DefaultListModel nameModel,DefaultListModel typeModel ,JList namelist ,JList typelist){
		nameModel.clear();
		file.clear();
		type.clear();
		typeModel.clear();
		try {
			File [] fi=system.readfloder(currentpath);
			if(fi!=null&&fi.length>0){
				for(int i=0;i<fi.length;i++){
					nameModel.addElement(fi[i].getName());
					file.add(fi[i]);
					if(system.readfloder(fi[i].getPath())==null){
						typeModel.addElement("File");
						type.add("File");
					}else{
						System.out.println(fi[i].getName()+"is a floder");
						typeModel.addElement("Floder");
						type.add("Floder");
						
					}
				
					
				}
			}
			namelist.setSelectedIndex(selectedfile);
			typelist.setSelectedIndex(selectedfile);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
public void printResult(ArrayList<File> f,DefaultListModel nameModel,DefaultListModel typeModel ,JList namelist ,JList typelist){
	if(f.size()>0){
		nameModel.clear();
		file.clear();
		type.clear();
		typeModel.clear();
		for(File i:f){
			nameModel.addElement(i.getName());
			file.add(i);
			try{
			if(system.readfloder(i.getPath())==null){
				typeModel.addElement("File");
				type.add("File");
			}else{
				typeModel.addElement("Floder");
				type.add("Floder");
			}}catch(Exception e){
				e.printStackTrace();
			}
		}
		namelist.setSelectedIndex(selectedfile);
		typelist.setSelectedIndex(selectedfile);
		}
} 
   public void SearchGUI(DefaultListModel nameModel,DefaultListModel typeModel ,JList namelist ,JList typelist){
	   JFrame  gui=new JFrame();
	   JPanel mainGui=new JPanel(new GridLayout(2,2));
	   JLabel keyword=new JLabel("Keyword");
	   JButton search=new JButton("Search");
	   
	   JTextField keyWordText=new JTextField();
	   search.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					ArrayList<File> result=system.SearchFile(currentpath, keyWordText.getText());
					printResult(result,nameModel,typeModel,namelist,typelist);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				gui.setVisible(false);
			}
	   });
	   mainGui.add(keyword);
	   mainGui.add(keyWordText);
	   mainGui.add(search);
	   gui.add(BorderLayout.CENTER,mainGui);
	   gui.setSize(400,150);
	   gui.setVisible(true);
   }
   public void CreateGUI(Boolean fileFlag ,DefaultListModel nameModel,DefaultListModel typeModel ,JList namelist ,JList typelist ){
	   JFrame  gui=new JFrame();
	   JPanel mainGui=new JPanel(new GridLayout(2,2));
	   JLabel name=new JLabel("Name");
	   JButton create =new JButton("Create");
	   JTextField nameText=new JTextField();
	   create.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(fileFlag){
					try {
						system.createfile(currentpath, nameText.getText());
						updatelist(nameModel,typeModel,namelist,typelist);
						
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					try {
						system.CreateFloder(currentpath, nameText.getText());
						updatelist(nameModel,typeModel,namelist,typelist);

					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				gui.setVisible(false);
			}
	   });
	   mainGui.add(name);
	   mainGui.add(nameText);
	   mainGui.add(create);
	   gui.add(BorderLayout.CENTER,mainGui);
	   gui.setSize(400,150);
	   gui.setVisible(true);

   }
   public void uploadfile(File source ,String savedpath){
	   if(!source.isFile()){
		try {
			system.CreateFloder(savedpath,source.getName());
		    File[] filelist=source.listFiles();
		    if(filelist!=null && filelist.length!=0){
		    	String filepath=savedpath+File.separator+source.getName();
		    	for(int i=0;i<filelist.length;i++){
		    		if(system.readfloder(filelist[i].getPath())==null){
		    		     byte[] a=filetobyte(filelist[i].getPath());
		    		     system.saving(filepath+File.separator+filelist[i].getName(), a);
		    		}else{
		    			uploadfile(filelist[i],filepath);
		    		}
		    	}
		    }
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	   }
   }
   public void downloadfile(File s,String savedpath){
	   String filepath=savedpath+File.separator+s.getName();
	   File f=new File(filepath);
	   if(!f.exists()){
		   f.mkdirs();
	   }
	   File[] flist;
	try {
		flist = system.readfloder(s.getPath());
		  if(flist!=null&&flist.length!=0){
			   for(int i=0;i<flist.length;i++){
				   if(system.readfloder(flist[i].getPath())==null){
					   try {
						byte[] c=system.filetobyte(flist[i].getPath());
						saving(filepath+File.separator+flist[i].getName(), c);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					   
				   }else{
					   downloadfile(flist[i],filepath);
					   
				   }
			   }
			   }
	} catch (RemoteException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		
	}
	 
 }
	   
	   
   
   public void updateFilename(DefaultListModel nameModel,DefaultListModel typeModel ,JList namelist ,JList typelist){
		JFrame gui=new JFrame();
		JPanel mainGUI=new JPanel(new GridLayout(2,2));
		JLabel label=new JLabel("Name");
		JTextField data=new JTextField();
	    JButton update=new JButton("Update");
	    update.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					system.renamefile(file.get(selectedfile).getPath(), data.getText());
					updatelist(nameModel,typeModel,namelist,typelist);
					gui.setVisible(false);
				} catch (RemoteException e1) {
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
   
   public void GUIDick(){
	   JFrame gui=new JFrame();
	   JPanel pan=new JPanel(new GridLayout(1,2));	   
	   DefaultListModel fileModel=new DefaultListModel();
	   if(file.size()>0){
		   for(File f:file){
			   fileModel.addElement(f.getName());
		   }
	   }
	   JList filelist=new JList(fileModel);
	   DefaultListModel typeModel=new DefaultListModel();
	   if(type.size()>0){
		   for(String s:type){
			   typeModel.addElement(s);
		   }
	   }
	   JList type=new JList(typeModel);
	   filelist.addListSelectionListener(new ListSelectionListener(){

		@Override
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			JList b=(JList)e.getSource();
			 type.setSelectedIndex(b.getSelectedIndex());
             selectedfile=b.getSelectedIndex();
			
		}
		   
	   } );
	   type.addListSelectionListener(new ListSelectionListener(){

		@Override
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			JList b=(JList)e.getSource();
			filelist.setSelectedIndex(b.getSelectedIndex());
            selectedfile=b.getSelectedIndex();

		}
		   
	   });
	   JPanel text=	new JPanel( new GridLayout(1,2));
	   text.add(new JLabel("Name"));
	   text.add(new JLabel("File Type"));
	   pan.add(filelist);
	   pan.add(type);
	   JScrollPane scrollPane=new JScrollPane();
	   scrollPane.setViewportView(pan);
	   JPanel Button=new JPanel(new GridLayout(12,1));
	   
	   JButton forward=new JButton("Forward");
	   forward.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try{
			if(system.readfloder(file.get(selectedfile).getPath())!=null){
				currentpath=currentpath+File.separator+file.get(selectedfile).getName();
				updatelist(fileModel,typeModel,filelist,type);

			}
			}catch(Exception r){
				r.printStackTrace();
			}
			
		}
		   
	   });
	   JButton backward=new JButton("Backward");
	   backward.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(!currentpath.equals(diskpath)){
				File t=new File(currentpath);
				currentpath=t.getParent();
				updatelist(fileModel,typeModel,filelist,type);

			}
			
		}
		   
	   });
	   JButton createFile=new JButton("Create File");
	   createFile.addActionListener( new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			CreateGUI(true,fileModel,typeModel,filelist,type);

		}
		   
	   });
	   JButton createFloder=new JButton("Create Floder");
	   createFloder.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			CreateGUI(false,fileModel,typeModel,filelist,type);

		}
		   
	   });
	   JButton copy=new JButton("Copy");
	   copy.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				if(system.readfloder(file.get(selectedfile).getPath())==null){
				  system.copyfile(file.get(selectedfile).getPath());
				}else{
				  system.copyfloder(file.get(selectedfile).getPath());
				}
				updatelist(fileModel,typeModel,filelist,type);

			} catch (RemoteException   e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NotFileHandler e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		   
	   });
	   
	   JButton delete=new JButton("Delete");
	   delete.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				system.deletefile(file.get(selectedfile).getPath());
				updatelist(fileModel,typeModel,filelist,type);

			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		   
	   });
	   JButton search=new JButton("Search");
	   search.addActionListener( new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SearchGUI(fileModel,typeModel,filelist,type);
		}
		   
	   });
	   JButton select=new JButton("select");
	   select.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			selectForMove=file.get(selectedfile).getPath();
			
		}
		   
	   });
	   JButton pase=new JButton("Pase");
	   pase.addActionListener( new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(!selectForMove.isEmpty()){
			try {
				system.moveFile(selectForMove, currentpath);
				updatelist(fileModel,typeModel,filelist,type);

			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				
			}
			}
			
		}
		   
	   });
	   JButton imt=new JButton("import");
	   imt.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JFileChooser chooser=new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				int temsize = 0;
				try {
					temsize = system.getDiskCurrentSize(diskpath);
				} catch (RemoteException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(chooser.getSelectedFile().isFile()&&(chooser.getSelectedFile().length()+temsize)<avaliableSize){
					try {
						String path=currentpath+File.separator+chooser.getSelectedFile().getName();
						system.saving(path,filetobyte(chooser.getSelectedFile().getPath()));
						updatelist(fileModel,typeModel,filelist,type);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else if(!chooser.getSelectedFile().isFile()&&(chooser.getSelectedFile().length()+temsize)<avaliableSize){
					
					uploadfile(chooser.getSelectedFile(),currentpath);
					updatelist(fileModel,typeModel,filelist,type);				
					
				}
				
				
			}
		}
		   
	   });
	   JButton exp=new JButton("Export");
       exp.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JFileChooser chooser=new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				if(!chooser.getSelectedFile().isFile()){
					if(file.get(selectedfile).isFile()){
					String path=chooser.getSelectedFile().getPath()+File.separator+file.get(selectedfile).getName();
					byte[] filebit = null;
					try {
						filebit = system.filetobyte(file.get(selectedfile).getPath());
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(!filebit.equals(null)){
				  	try {
						saving(path,filebit);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
				 }else{
					 downloadfile(file.get(selectedfile),chooser.getSelectedFile().getPath());
					 
					 
					 
				 }
				
					
					
					
					
				}
			}

			
		}
    	   
       }); 
       JButton rename=new JButton("Rename");
       rename.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			updateFilename(fileModel,typeModel,filelist,type);
							

		}
    	   
       });
	   Button.add(forward);
	   Button.add(backward);
	   Button.add(createFile);
	   Button.add(createFloder); 
	   Button.add(copy);
	   Button.add(delete);
	   Button.add(search);
	   Button.add(select);
	   Button.add(pase);
	   Button.add(imt);
	   Button.add(exp);
	   Button.add(rename);
	   gui.add(BorderLayout.EAST, Button); 
	   gui.add(BorderLayout.NORTH,text);
	   gui.add(BorderLayout.CENTER,scrollPane);
	   gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   gui.setSize(600, 400);
	   gui.setVisible(true);
	   
	   
   }
}
