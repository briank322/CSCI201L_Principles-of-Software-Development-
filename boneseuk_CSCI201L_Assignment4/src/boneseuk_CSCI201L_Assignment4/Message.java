package boneseuk_CSCI201L_Assignment4;

import java.io.Serializable;

public class Message implements Serializable{
	
	public static final long serialVersionUID = 1;
	
	private String message;
	private String messagetype;
	
	public Message(String message) {
		this.message = message;
		this.messagetype = "1";
	}
	public String getMessage() {
		return this.message;
	}
	public String getType() {
		return this.messagetype;
	}
	
	public Message(String message,String messagetype) {
		
		//create type 2
		this.messagetype = messagetype;
		this.message = message;
	}
	

}
