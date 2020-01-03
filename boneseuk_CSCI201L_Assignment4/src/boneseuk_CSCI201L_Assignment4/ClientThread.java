package boneseuk_CSCI201L_Assignment4;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

public class ClientThread extends Thread {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public ClientThread(Properties pp) {
		try {
			System.out.print("Trying to connect to server...");
			Socket s = new Socket(pp.getProperty("ServerHostname"), Integer.parseInt(pp.getProperty("ServerPort")));
			System.out.println("Connected!");
			try {
				this.oos = new ObjectOutputStream(s.getOutputStream());
				this.ois = new ObjectInputStream(s.getInputStream());
				this.start();
				
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
			
		} catch (IOException ioe) {
			System.out.println("Unable to connect to server localhost on port " + pp.getProperty("ServerPort")+".");
		}
	}
	public void run() {
		Scanner scan = new Scanner(System.in);
		try {
			while(true) {
				Message ms = (Message)this.ois.readObject();
				System.out.print(ms.getMessage());
				
				if(ms.getType().equals("2")) {
					System.in.read(new byte[System.in.available()]);
					String line = scan.nextLine();
					ms = new Message(line);
					oos.writeObject(ms);
					oos.flush();
					
				}
			}
		
		} catch (IOException ioe) {
			System.out.println("ioe in ChatClient.run(): " + ioe.getMessage());
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		}
	}
	public static void main(String [] args) {
		System.out.println("What is the name of the configuration file?");
		Properties pp = new Properties();
		String fileName="";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			fileName = br.readLine();
			FileReader fr = new FileReader(fileName); 
			pp.load(fr);
			Set<String> keys = pp.stringPropertyNames();
			if(keys.size()<6) {
				if(!keys.contains("ServerHostname")) 
					System.out.println("ServerHostname is a required parameter in the configuration file.");
				if(!keys.contains("ServerPort")) 
					System.out.println("ServerPort is a required parameter in the configuration file.");
				if(!keys.contains("DBConnection")) 
					System.out.println("DBConnection is a required parameter in the configuration file.");
				if(!keys.contains("DBUsername")) 
					System.out.println("DBUsername is a required parameter in the configuration file.");
				if(!keys.contains("DBPassword")) 
					System.out.println("DBPassword is a required parameter in the configuration file.");
				if(!keys.contains("SecretWordFile")) 
					System.out.println("SecretWordFile is a required parameter in the configuration file.");
				return;	
			}
			System.out.println("Reading config file...");
			for(String key: keys) {
				if(pp.getProperty(key) == null) {
					System.out.println(key + " is a required parameter in the configuration file.");
				}
			}
			File file = new File(pp.getProperty("SecretWordFile"));
			if(!file.exists()) {
				System.out.println("Secret word file " + pp.getProperty("SecretWordFile") + " could not be found.");
				return;
			}
			System.out.println("Server Hostname - " + pp.getProperty("ServerHostname"));
			System.out.println("Server Port - " + pp.getProperty("ServerPort"));
			System.out.println("Database Connection - " + pp.getProperty("DBConnection"));
			System.out.println("Database Username - " + pp.getProperty("DBUsername"));
			System.out.println("Database Password - " + pp.getProperty("DBPassword"));
			System.out.println("Secret Word File - " + pp.getProperty("SecretWordFile"));
		}catch(IOException ioe) {
			
			System.out.println("Configuration file " + fileName + " could not be found.");
		}
		ClientThread cc = new ClientThread(pp);
	}
	
	
	
}
