package vfs;

public class NotFileHandler  extends Exception{
	public NotFileHandler(){
		//System.out.print("Not such file ");
	}
	public String getMessage(){
		return "Create File Error";
	}

}
