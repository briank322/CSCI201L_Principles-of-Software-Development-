package boneseuk_CSCI201L_Assignment4;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class Hangman {
	private Vector<GameRoom> gamerooms;
	private Vector<ServerThread> logins;
	private String url;
	private String filename;
	private Connection conn = null;
	private PreparedStatement ps = null;
	private Statement st = null;
	private ResultSet rs = null;
	private boolean connected = true;
	
	
	
	
	
	public Hangman(int port, Properties pp) {
		this.gamerooms = new Vector<>();
		this.logins = new Vector<>();
		this.url = pp.getProperty("DBConnection") + pp.getProperty("DBUsername") + 
				"&password=" + pp.getProperty("DBPassword");
		this.filename = pp.getProperty("SecretWordFile");
		
		
		
		try {
			System.out.println("Trying to connect to database...");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(this.url);
		}catch(SQLException e) {
			System.out.println("Unable to connect to " + url +"with username "+pp.getProperty("DBUsername")+
					"and password "+pp.getProperty("DBPassword"));
			connected = false;
			
		}catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		if(connected) {
			System.out.println("Connected!");
		}
		
		
		
		
		
		
		if(connected == false)
			return;
		try {
			
			ServerSocket ss = new ServerSocket(Integer.parseInt(pp.getProperty("ServerPort")));
			while(true) {
				Socket s = ss.accept();
				ServerThread st = new ServerThread(s, this, this.url, this.filename);
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	public Vector<GameRoom> getGameRooms() {
		return this.gamerooms;
	}
	public Vector<ServerThread> getLogins(){
		return this.logins;
	}
	
	public GameRoom getGameroom(String gamename) {
		gamename = gamename.toLowerCase();
		for(GameRoom gameroom: gamerooms) {
			if(gameroom.getGamename().equals(gamename))
				return gameroom;
		}
		return null;
	}
	public boolean gamenamecheck(String gamename) {
		boolean checker = false;
		gamename = gamename.toLowerCase();
		for(GameRoom gr: gamerooms) {
			if(gr.getGamename().equals(gamename)) {
				checker = true;
			}
		}
		return checker;
	}
	public void addGame(GameRoom gameroom) {
		gamerooms.add(gameroom);
	}
	public boolean alreadyLogin(String username) {
		boolean checker = false;
		for(ServerThread st: logins) {
			if(st.getUsername().equals(username))
				checker = true;
		}
		return checker;
	}
	public void addnewlogs(ServerThread st) {
		this.logins.add(st);
	}
	public static void main(String[] args) {
		
		System.out.println("What is the name of the configuration file?");
		Scanner sc = null;
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
			return;
		}
		
		Hangman hangman = new Hangman(Integer.parseInt(pp.getProperty("ServerPort")), pp);
	}

}
