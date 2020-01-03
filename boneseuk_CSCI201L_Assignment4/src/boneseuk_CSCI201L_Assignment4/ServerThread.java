package boneseuk_CSCI201L_Assignment4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class ServerThread extends Thread{
	
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public String filename;
	private String username;
	private String password;
	private Hangman hangman;
	public String record = "";
	private String url;
	public boolean playerdead = false;
	private Vector<GameRoom> gamerooms;
	private Vector<ServerThread> logins;
	private int numwin = -1;
	private int numlose = -1;
	private boolean pwvalidate = false;
	private Connection conn = null;
	private PreparedStatement ps = null;
	private Statement st = null;
	private ResultSet rs = null;
	
	
	public ServerThread(Socket s, Hangman hangman, String url, String filename) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.sss");
		this.hangman = hangman;
		this.gamerooms = hangman.getGameRooms();
		this.logins = hangman.getLogins();
		this.filename = filename; 
		this.url = url;
		
		
		try {
			
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			try {
				String inputuser = "";
				boolean loggedin = false;
				while(loggedin == false) {
					Message ms;
					//Getting Username
					this.sendMessage(new Message("Username: ", "2"));
					ms = (Message)this.ois.readObject();
					this.username = ms.getMessage();
					
					//Getting Password
					this.sendMessage(new Message("Password: ", "2"));
					ms = (Message)this.ois.readObject();
					this.password = ms.getMessage();
					
					System.out.println(dateFormat.format(new Date()) + ": <" + username + "> - trying to log in with password <" + password+">.");
					boolean jdbcuserexist = false;
						
					try {
						Class.forName("com.mysql.jdbc.Driver");
						conn = DriverManager.getConnection(this.url);
						ps = conn.prepareStatement("SELECT * FROM user WHERE BINARY username=?");
						ps.setString(1, this.username);
						rs = ps.executeQuery();
						if(rs.next()) {
							jdbcuserexist = true;
						}
						}catch(SQLException e) {
							e.printStackTrace();
						}catch(ClassNotFoundException cnfe) {
							cnfe.printStackTrace();
						}
							
					
					if(jdbcuserexist == false) {
						System.out.println(dateFormat.format(new Date()) + 
								": <" + this.username + "> - does not have an account so not successfully logged in.");
						this.sendMessage(new Message("No account exists with those credentials.\nWould you like to create a new account? ", "2"));
						while(true) {
							ms = (Message)ois.readObject();
							inputuser = ms.getMessage().toLowerCase();
							if(inputuser.equals("yes")) {
								this.sendMessage(new Message("Would you like to use the username and password above? ", "2"));
								ms = (Message)ois.readObject();
								inputuser = ms.getMessage().toLowerCase();
								if(inputuser.equals("yes")) {
									try {
										Class.forName("com.mysql.jdbc.Driver");
										conn = DriverManager.getConnection(this.url);
										ps = conn.prepareStatement("INSERT INTO user(username, password, win, lose) VALUES (?,?,?,?)");
										ps.setString(1, this.username);
										ps.setString(2, this.password);
										ps.setInt(3, 0);
										ps.setInt(4, 0);
										ps.executeUpdate();
									}catch(SQLException e) {
										e.printStackTrace();
									}catch(ClassNotFoundException cnfe) {
										cnfe.printStackTrace();
									}
									
									System.out.println(dateFormat.format(new Date()) + ": <" 
									+ this.username + "> - created an account with password <" + this.password + ">.");
									this.sendMessage(new Message("Great! You are now logged in as " + this.username + "!\n\n"));
									
									
									//////////////record!
					
									
									
									
									try {
										Class.forName("com.mysql.jdbc.Driver");
										conn = DriverManager.getConnection(this.url);
										ps = conn.prepareStatement("SELECT win,lose FROM user WHERE username=?");
										ps.setString(1, this.username);
										rs = ps.executeQuery();
										rs.next();
										this.numwin = rs.getInt("win");
										this.numlose = rs.getInt("lose");
									}catch (SQLException sqle) {
										sqle.printStackTrace(); 
									}catch(Exception e) {
										e.printStackTrace();
									}
									
									try {
										Class.forName("com.mysql.jdbc.Driver");
										conn = DriverManager.getConnection(this.url);
										ps = conn.prepareStatement("SELECT win, lose FROM user WHERE username=?");
										ps.setString(1, this.username);
										rs = ps.executeQuery();
										rs.next();
										record = this.username + "'s Record\n--------------\n" +
										"Wins - " + String.valueOf(rs.getInt("win")) + "\n" +
										"Losses - " + String.valueOf(rs.getInt("lose")) + "\n\n";
									}catch (SQLException sqle) {
										sqle.printStackTrace(); 
									}catch(Exception e) {
										e.printStackTrace();
									}
									this.sendMessage(new Message(record));
									
									System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - has record <" 
									+ numwin + "> wins and <" + numlose + "> losses.");
									loggedin = true;
									break;
								}
								else if(inputuser.equals("no")) {
									this.sendMessage(new Message("Try another username and password\n"));
									break;
								}else {
									this.sendMessage(new Message("Answer either yes or no!\n"));
									this.sendMessage(new Message("Would you like to create a new account? ", "2"));
								}
								
						
							}else if(inputuser.toLowerCase().equals("no")) {
								this.sendMessage(new Message("Try another username and password\n"));
								break;
							}else {
								this.sendMessage(new Message("Answer either yes or no!\n"));
								this.sendMessage(new Message("Would you like to create a new account? ", "2"));
							} 
						}
			
						
					}// end of if((jdbcuserexist == false)
					
					else if(jdbcuserexist == true){
						
						// double check password
						
						try {
							Class.forName("com.mysql.jdbc.Driver");
							conn = DriverManager.getConnection(this.url);
							ps = conn.prepareStatement("SELECT * FROM user WHERE BINARY username=? AND BINARY password=?");
							ps.setString(1, this.username);
							ps.setString(2, this.password);
							rs = ps.executeQuery();
							if(rs.next()) {
								pwvalidate = true;
							}
						}catch(SQLException e) {
							e.printStackTrace();
						}catch(ClassNotFoundException cnfe) {
							cnfe.printStackTrace();
						}
						
						
						if(pwvalidate) {
							
							if(this.hangman.alreadyLogin(this.username)) {
								this.sendMessage(new Message("User <" + this.username + "> is already logged in.\n"));
							//successfully logged in!
							}else {
								
								
								try {
									Class.forName("com.mysql.jdbc.Driver");
									conn = DriverManager.getConnection(url);
									ps = conn.prepareStatement("SELECT win,lose FROM user WHERE username=?");
									ps.setString(1, username);
									rs = ps.executeQuery();
									rs.next();
									numwin = rs.getInt("win");
									numlose = rs.getInt("lose");
								}catch (SQLException sqle) {
									sqle.printStackTrace(); 
								}catch(Exception e) {
									e.printStackTrace();
								}
								
								
								System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - successfully logged in.");
								System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - has record <" 
								+ numwin + "> wins and <" 
								+ numlose + "> losses.");
								this.sendMessage(new Message("Great! You are now logged in as " + this.username + "!\n\n"));
								
								
								
								try {
									Class.forName("com.mysql.jdbc.Driver");
									conn = DriverManager.getConnection(this.url);
									ps = conn.prepareStatement("SELECT win, lose FROM user WHERE username=?");
									ps.setString(1, username);
									rs = ps.executeQuery();
									rs.next();
									record = username + "'s Record\n" +
									"--------------\n" +
									"Wins - " + String.valueOf(rs.getInt("win")) + "\n" +
									"Losses - " + String.valueOf(rs.getInt("lose")) + "\n\n";
								}catch (SQLException sqle) {
									sqle.printStackTrace(); 
								}catch(Exception e) {
									e.printStackTrace();
								}
								
								
								
								this.sendMessage(new Message(record));
								this.hangman.addnewlogs(this);
								loggedin = true;
							}
						//username and password do not match
						}else {
							System.out.println(dateFormat.format(new Date()) + ": <" + this.username
									+ "> - has an account but not successfully logged in.");
							this.sendMessage(new Message("Incorrect password! (case-sensitive for username & pw) \n"));
						}
					}
					
				}	
				this.start();
			}catch (IOException ioe) {
				System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
			} catch (ClassNotFoundException cnfe) {
				System.out.println("cnfe: " + cnfe.getMessage());
			}
			
			
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
			ioe.printStackTrace();
		} 
	}
	
	
	
	public void run() {
		Message ms;
		GameRoom gr;
		String input = "";
		String gamename = "";
		String tword = "";
		String word = "";
		String[] words;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.sss");;
		try {
			while(true) {
				
				this.sendMessage(new Message("   1) Start a Game.\n"));
				this.sendMessage(new Message("   2) Join a Game.\n\n"));
				this.sendMessage(new Message("Would you like to start a game or join a game?", "2"));
				ms = (Message)ois.readObject();
				input = ms.getMessage().toLowerCase();
				if(input.equals("1") || input.equals("2") ) {
					
						this.sendMessage(new Message("What is the name of the game?", "2"));
						ms = (Message)ois.readObject();
						gamename = ms.getMessage();
						
						if(input.equals("1")) {
							System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - wants to start a game called <" + gamename + ">.");
							
							if(hangman.gamenamecheck(gamename)) {
								System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - <" + gamename + "> already exists, so unable to start <" + gamename + ">.");
								this.sendMessage(new Message(""+gamename + " already exists.\n"));
							
							}else {
								while(true) {
									this.sendMessage(new Message("How many users will be playing (1-4)?", "2"));
									ms = (Message)ois.readObject();
									input = ms.getMessage();
									
									if((!input.equals("1")) && (!input.equals("2")) && (!input.equals("3")) && (!input.equals("4"))) {
										this.sendMessage(new Message("A game can only have between 1-4 players.\n"));
									
									}
									else{
										break;
									}
								}
										
									
									
										gr = new GameRoom(this, gamename, Integer.parseInt(input));
										this.hangman.addGame(gr);
										System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - successfully started game " + gamename + ".");
										break;
									
									
								
								
							}
						
						}else {
							System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - wants to join a game called <" + gamename + ">.");
							
							if(!this.hangman.gamenamecheck(gamename)) {
								this.sendMessage(new Message("There is no game with name <"+ gamename + ">.\n"));
							
							}else {
								
								if(this.hangman.getGameroom(gamename).start == true) {
									System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - <" + gamename + "> exists, but <" + this.username +"> unable to join because maximum number of players have already joined <" + gamename + ">.");
									this.sendMessage(new Message("The game <" + gamename + "> does not have space for another user to join.\n"));
								
								}else {
									gr = this.hangman.getGameroom(gamename);
									gr.addUser(this, this.url);
									System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - successfully joined game <" + gamename + ">.");
									break;
								}
							}
							
						}
					}
				
				else{
					this.sendMessage(new Message("Invalid option! Please type 1 or 2! \n"));
				}
				
			}
			if(gr.start==false) {
				System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - <" + gamename + "> needs <" + gr.usersize() + "> to start game.");
				gr.broadcast(new Message("Waiting for " + String.valueOf(gr.usersize()-gr.totaluser()) + " other user to join...\n\n"));
				
				while(gr.start == false) {
				}
			}
			System.out.println(dateFormat.format(new Date()) + ": <" + this.username + "> - <" + gamename + "> has <" + gr.usersize() + "> so starting game."); 
			
			try {
				File file = new File(filename);
				FileReader fr = new FileReader(filename);
				BufferedReader br = new BufferedReader(fr);
				int ran_num = (int)(Math.random()*4373);
				int index = 0;
				while(index<ran_num) {
					++index;
					br.readLine();
				}
				word = br.readLine().trim().toLowerCase();
				br.close();
				
		        
		        for(int i=0; i<word.length(); ++i) {
		        	tword+="_";
		        }
//		        this.sendMessage(new Message("All users have joined.\n\n"));
//				this.sendMessage(new Message("Determining secret word...\n\n"));
		       
		        System.out.println("Secret word is <" + word + ">.");
		        gr.playGame(word, tword, url);
			}catch(FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		}
	}
	
	public void sendMessage(Message ms) {
		try {
			oos.writeObject(ms);
			oos.flush();
		}
		catch(IOException e) {
			e.printStackTrace();
		}	
	}
	public String getUsername() {
		return this.username;
	}
	


}
