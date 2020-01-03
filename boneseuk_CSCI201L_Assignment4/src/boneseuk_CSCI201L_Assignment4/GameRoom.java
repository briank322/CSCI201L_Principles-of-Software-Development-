package boneseuk_CSCI201L_Assignment4;

import java.util.Date;
import java.util.Vector;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GameRoom extends Thread{
	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.sss");
	private String losersNames = "";
	private Vector<ServerThread> threads;//use blocking queue
	private String gamename;
	public boolean start = false;
	private boolean end = false;
	private int numtry = 7;
	private int size;
	private int numwin =0;
	private int numlose =0;
	private String records2 = "";
	
	private Connection conn = null;
	private PreparedStatement ps = null;
	private Statement st = null;
	private ResultSet rs = null;
	private Vector<ServerThread> losers;
	
	public GameRoom(ServerThread st, String gamename, int size) {
		threads = new Vector<ServerThread>();
		this.threads.add(st);
		if(this.threads.size() == size)
		{
			this.start = true;
		}
		else {
			this.start = false;
		}
		this.size = size;
		gamename = gamename.toLowerCase();
		this.gamename = gamename;
		
	}
	public String targetwordproducer(String targetword)
	{
		String word = "";
		char word2;
		for(int i = 0; i<targetword.length(); i++) {
			word2 = targetword.charAt(i);
			word = word+ word2+ " ";
		}
		return word;
	}
	public String userrecords(ServerThread st, String url) {
		
		
		String records = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			ps = conn.prepareStatement("SELECT win, lose FROM user WHERE username=?");
			ps.setString(1, st.getUsername());
			rs = ps.executeQuery();
			rs.next();
			records = st.getUsername() + "'s Record\n" +
			"--------------\n" +
			"Wins - " + String.valueOf(rs.getInt("win")) + "\n" +
			"Loses - " + String.valueOf(rs.getInt("lose")) + "\n" + "\n";
		}catch (SQLException sqle) {
			sqle.printStackTrace(); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		for(ServerThread a: threads) {
			if(!a.equals(st)) {
				
				
				try {
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(url);
					ps = conn.prepareStatement("SELECT win, lose FROM user WHERE username=?");
					ps.setString(1, a.getUsername());
					rs = ps.executeQuery();
					rs.next();
					records2 = a.getUsername() + "'s Record\n" +
					"--------------\n" +
					"Wins - " + String.valueOf(rs.getInt("win")) + "\n" +
					"Loses - " + String.valueOf(rs.getInt("lose")) + "\n" + "\n";
				}catch (SQLException sqle) {
					sqle.printStackTrace(); 
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
				records += records2;
			}
		}
		return records;
	}
	
	public String getGamename() {
		//get game name
		return this.gamename;
	}
	public void broadcast(Message ms) {
		if (ms == null) {
			return;
		}
		
		else {
			
			for(ServerThread threads : this.threads) {
				boolean pdead = threads.playerdead;
				if(pdead==false) {
					
				
					threads.sendMessage(ms);
				}
			} 
		}
	}
	public void broadcast(Message ms, ServerThread st) {
		if (ms == null) {
			return;
		}
		else {
			for(ServerThread threads : this.threads) {
				boolean pdead = threads.playerdead;
				boolean isst = threads.equals(st);
				if(!isst)
					{
						if(!pdead)
						{
							threads.sendMessage(ms);
						}
				}
					
			}
		}
	}
	public void addUser(ServerThread st, String url) {
		if(start == true) {
			return;
		}
		
		this.threads.add(st);
		boolean startgame = false;
		if(this.threads.size()==this.size)
		{
			startgame = true;
		}
		else {
			startgame = false;
		}
		this.start = startgame;
		
		String records = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			ps = conn.prepareStatement("SELECT win, lose FROM user WHERE username=?");
			ps.setString(1, st.getUsername());
			rs = ps.executeQuery();
			rs.next();
			records = st.getUsername() + "'s Record\n" +
			"--------------\n" +
			"Wins - " + String.valueOf(rs.getInt("win")) + "\n" +
			"Loses - " + String.valueOf(rs.getInt("lose")) + "\n" + "\n";
		}catch (SQLException sqle) {
			sqle.printStackTrace(); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		this.broadcast(new Message("User " + st.getUsername() + " is in the game.\n\n" + 
		records), st);
		//user's getting other users' info in the gameroom
		
		for(ServerThread a: threads) {
			
				records2 = "";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url);
				ps = conn.prepareStatement("SELECT win, lose FROM user WHERE username=?");
				ps.setString(1, a.getUsername());
				rs = ps.executeQuery();
				rs.next();
				records2 = a.getUsername() + "'s Record\n" +
				"--------------\n" +
				"Wins - " + String.valueOf(rs.getInt("win")) + "\n" +
				"Loses - " + String.valueOf(rs.getInt("lose")) + "\n" + "\n";
			}catch (SQLException sqle) {
				sqle.printStackTrace(); 
			}catch(Exception e) {
				e.printStackTrace();
			}
			if(!a.equals(st)) {
				st.sendMessage(new Message("User " + a.getUsername() + " is in the game.\n\n" +records2));
			}
		}
	}
	public int totaluser() {
		return this.threads.size();
	}
	public int usersize() {
		return this.size;
	}
	
	public void playGame(String word, String targetword, String url) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.sss");
		losers = new Vector<>();
		Vector<Integer> positions = new Vector<>(); 
		String inputuser="";
		boolean correct = false;
		boolean correct2 = false;
		Message ms;
		this.broadcast(new Message("All users have joined.\n\n"));
		this.broadcast(new Message("Determining secret word...\n\n"));
		while(end == false) {
			//
			for(ServerThread st: threads) {
				if(st.playerdead) {
					continue;
				}
				
				this.broadcast(new Message("Secret Word " + this.targetwordproducer(targetword) + "\n\n"));
				this.broadcast(new Message("You have " + this.numtry + " incorrect guesses remaining.\n"));
				this.broadcast(new Message("Waiting for " + st.getUsername() + " to do something...\n\n\n"), st);
				while(true) {
					st.sendMessage(new Message("   1) Guess a letter.\n"));
					st.sendMessage(new Message("   2) Guess the word.\n\n"));
					st.sendMessage(new Message("What would you like to do? ", "2"));
					try {
						ms = (Message)st.ois.readObject();
						inputuser = ms.getMessage();
						
						if(inputuser.equals("1")) {
							st.sendMessage(new Message("Letter to guess – ", "2"));
							ms = (Message)st.ois.readObject();
							inputuser = ms.getMessage();
							System.out.println(dateFormat.format(new Date()) + ": <" + this.getGamename() + "> <" + st.getUsername() + "> - guessed letter <" + inputuser+">.");
							
							if(inputuser.length()!=1) {
								st.sendMessage(new Message("Must be a single character!\n"));
								continue;
							}
							positions.clear();
							correct = false;
							for(int i = 0; i<word.length(); ++i) {
								if(word.substring(i,i+1).toLowerCase().equals(inputuser.toLowerCase())) {
									positions.add(i);
									correct = true;
									String temp1 = targetword.substring(0, i);
									String temp2 = targetword.substring(i + 1);
									targetword = temp1 + inputuser.toUpperCase() + temp2;
								}	
							}
							this.broadcast(new Message("<"+st.getUsername() + 
									">  has guessed letter ‘" + inputuser + "'.\n"), st);
							
							if(correct) {
								String pos = "";
								for(int p:positions) {
									
									pos = pos + String.valueOf(p+1) + ", ";
								}
								System.out.println(dateFormat.format(new Date()) + ": <" + this.getGamename() + "> <" + st.getUsername() + "> - <" + 
								inputuser + "> is in <" + word + "> in position(s) " + pos.substring(0, pos.length()-2) + 
								". Secret word now shows " + targetwordproducer(targetword) + ".");
								
								this.broadcast(new Message("The letter ‘" + inputuser + "’" + " is in the secret word.\n\n"));
								
								if(word.equals(targetword.toLowerCase())) {
									for(ServerThread temp: this.threads) {
										if(temp.equals(st) == true) {
											
										}
										else {
											losersNames = losersNames + temp.getUsername() + ", ";
										}
									}
									if(losersNames.length()!= 0) {
									System.out.println("<"+st.getUsername() + "> wins game. <" + losersNames.substring(0, losersNames.length()-2) + "> have lost the game.");
									
									}st.sendMessage(new Message("That is correct! You win!\n\n"));
									if(losersNames.length()== 0) {
									System.out.println(dateFormat.format(new Date()) + ": <"+st.getUsername() + "> wins game. ");
									}
									this.broadcast(new Message(st.getUsername()+ " guessed the word correctly. You lose!\n"), st);
									this.broadcast(new Message("The word was \"" + word +"\".\n"), st);
									
									
									try {
										Class.forName("com.mysql.jdbc.Driver");
										conn = DriverManager.getConnection(url);
										ps = conn.prepareStatement("SELECT win FROM user WHERE username=?");
										ps.setString(1, st.getUsername());
										rs = ps.executeQuery();
										rs.next();
										numwin = rs.getInt("win");
									}catch (SQLException sqle) {
										sqle.printStackTrace(); 
									}catch(Exception e) {
										e.printStackTrace();
									}
									
									
									int num;
									try {
										Class.forName("com.mysql.jdbc.Driver");
										conn = DriverManager.getConnection(url);
										
											num = this.numwin + 1;
											ps = conn.prepareStatement("UPDATE user SET win=? WHERE username=?");
											ps.setInt(1, num);
											ps.setString(2, st.getUsername());
											ps.executeUpdate();
										
									}catch (SQLException sqle) {
										sqle.printStackTrace(); 
									}catch(Exception e) {
										e.printStackTrace();
									}
									
									
									
									for(ServerThread a: threads) {
										if(!a.equals(st)) {
											try {
												Class.forName("com.mysql.jdbc.Driver");
												conn = DriverManager.getConnection(url);
												ps = conn.prepareStatement("SELECT lose FROM user WHERE username=?");
												ps.setString(1, a.getUsername());
												rs = ps.executeQuery();
												rs.next();
												numlose = rs.getInt("lose");
											}catch (SQLException sqle) {
												sqle.printStackTrace(); 
											}catch(Exception e) {
												e.printStackTrace();
											}
											
											
											int num1;
											try {
												Class.forName("com.mysql.jdbc.Driver");
												conn = DriverManager.getConnection(url);
												
													num1 = this.numlose + 1;
													ps = conn.prepareStatement("UPDATE user SET lose=? WHERE username=?");
													ps.setInt(1, num1);
													ps.setString(2, a.getUsername());
													ps.executeUpdate();
												
											}catch (SQLException sqle) {
												sqle.printStackTrace(); 
											}catch(Exception e) {
												e.printStackTrace();
											}
											
											
											
											
										}
									}
									st.sendMessage(new Message(this.userrecords(st, url)));
									//st.sendMessage(new Message("Thank you for playing Hangman!"));
									for(ServerThread a: threads) {
										if(!a.equals(st)) {
											a.sendMessage(new Message(this.userrecords(a, url)));
										}
									}
									
									this.broadcast(new Message("Thank you for playing Hangman!"));
//									for(ServerThread a: threads) {
//										if(!a.equals(st)) {
//											a.sendMessage(new Message("Thank you for playing Hangman!"));
//										}
//									}
									return;
								}
								break;
							//user got it wrong!
							}
							else {
								
								numtry = numtry-1;
								this.broadcast(new Message("The letter ‘" + inputuser + "’ is not in the secret word.\n\n"));
								System.out.println(dateFormat.format(new Date()) + ": <" + this.getGamename() + "> <" + st.getUsername() + "> - " + inputuser
										+ " is not in " + word + "." + "<"+this.getGamename() + "> now has " + (this.numtry)+ " guesses remaining.");
								
								if(numtry == 0) {
									end = true;
									System.out.println(dateFormat.format(new Date()) + ": <" +st.getUsername()+ "> has lost and is no longer in the game.");
									for(ServerThread a: threads) {
										if(!a.equals(st)) {
											System.out.println(dateFormat.format(new Date()) + ": <" +a.getUsername()+ "> has lost and is no longer in the game.");
											
										}
									}
								}
								
								if(end == true) {
									this.broadcast(new Message("That is incorrect! You lose!\n\n"));
									this.broadcast(new Message("The word was \"" + word +"\".\n"));
									try {
										Class.forName("com.mysql.jdbc.Driver");
										conn = DriverManager.getConnection(url);
										ps = conn.prepareStatement("SELECT lose FROM user WHERE username=?");
										ps.setString(1, st.getUsername());
										rs = ps.executeQuery();
										rs.next();
										numlose = rs.getInt("lose");
									}catch (SQLException sqle) {
										sqle.printStackTrace(); 
									}catch(Exception e) {
										e.printStackTrace();
									}
									
									
									int num2;
									try {
										Class.forName("com.mysql.jdbc.Driver");
										conn = DriverManager.getConnection(url);
										
											num2 = this.numlose + 1;
											ps = conn.prepareStatement("UPDATE user SET lose=? WHERE username=?");
											ps.setInt(1, num2);
											ps.setString(2, st.getUsername());
											ps.executeUpdate();
										
									}catch (SQLException sqle) {
										sqle.printStackTrace(); 
									}catch(Exception e) {
										e.printStackTrace();
									}
									
									for(ServerThread a: threads) {
										if(!a.equals(st)) {
										try {
											Class.forName("com.mysql.jdbc.Driver");
											conn = DriverManager.getConnection(url);
											ps = conn.prepareStatement("SELECT lose FROM user WHERE username=?");
											ps.setString(1, a.getUsername());
											rs = ps.executeQuery();
											rs.next();
											numlose = rs.getInt("lose");
										}catch (SQLException sqle) {
											sqle.printStackTrace(); 
										}catch(Exception e) {
											e.printStackTrace();
										}
										
										
										int num3;
										try {
											Class.forName("com.mysql.jdbc.Driver");
											conn = DriverManager.getConnection(url);
											
												num3 = this.numlose + 1;
												ps = conn.prepareStatement("UPDATE user SET lose=? WHERE username=?");
												ps.setInt(1, num3);
												ps.setString(2, a.getUsername());
												ps.executeUpdate();
											
										}catch (SQLException sqle) {
											sqle.printStackTrace(); 
										}catch(Exception e) {
											e.printStackTrace();
										}
										
										
										}
										
										
									}
									st.sendMessage(new Message(this.userrecords(st, url)));
									//st.sendMessage(new Message("Thank you for playing Hangman!"));
									for(ServerThread a: threads) {
										if(!a.equals(st)) {
											a.sendMessage(new Message(this.userrecords(a, url)));
										}
									}
									
									
									
									this.broadcast(new Message("Thank you for playing Hangman!"));
//									for(ServerThread a: threads) {
//										if(!a.equals(st)) {
//											a.sendMessage(new Message("Thank you for playing Hangman!"));
//										}
//									}
									
									return;
								}
								break;
							}
						}
						else if(inputuser.equals("2")) {
							
							st.sendMessage(new Message("What is the secret word? ", "2"));
							ms = (Message)st.ois.readObject();
							inputuser = ms.getMessage();
							System.out.println(dateFormat.format(new Date()) + ": <" + this.getGamename() + "> <" + st.getUsername() + "> - guessed word <" + inputuser+">");
							
							if(inputuser.toLowerCase().equals(word)) {
								
								if(losersNames.length()==0) {
									System.out.println(dateFormat.format(new Date()) + ": <" + this.getGamename() + "> <" + st.getUsername() + "> - <" + inputuser + "> is correct."+"<"+st.getUsername()+">"+ " wins game."
											);
									
								}
								
								
								for(ServerThread temp: this.threads) {
									if(temp.equals(st)) {
										
									}
									else {
										losersNames = losersNames + temp.getUsername() + ", ";
									}
								}
								
								if(losersNames.length()!= 0) {
								System.out.println(dateFormat.format(new Date()) +": <"+st.getUsername() + "> wins game. <" + losersNames.substring(0, losersNames.length()-2) + "> have lost the game.");
								}st.sendMessage(new Message("That is correct! You win!\n"));
								this.broadcast(new Message(st.getUsername()+" has guessed the word '"+word+"'. "), st);
								this.broadcast(new Message(st.getUsername()+ " guessed the word correctly. You lose!\n\n"), st);
								
								
								try {
									Class.forName("com.mysql.jdbc.Driver");
									conn = DriverManager.getConnection(url);
									ps = conn.prepareStatement("SELECT win FROM user WHERE username=?");
									ps.setString(1, st.getUsername());
									rs = ps.executeQuery();
									rs.next();
									numwin = rs.getInt("win");
								}catch (SQLException sqle) {
									sqle.printStackTrace(); 
								}catch(Exception e) {
									e.printStackTrace();
								}
								
								
								int num3;
								try {
									Class.forName("com.mysql.jdbc.Driver");
									conn = DriverManager.getConnection(url);
									
										num3 = this.numwin + 1;
										ps = conn.prepareStatement("UPDATE user SET win=? WHERE username=?");
										ps.setInt(1, num3);
										ps.setString(2, st.getUsername());
										ps.executeUpdate();
									
								}catch (SQLException sqle) {
									sqle.printStackTrace(); 
								}catch(Exception e) {
									e.printStackTrace();
								}
								
								
								for(ServerThread a: threads) {
									if(!a.equals(st)) {
										
										
										try {
											Class.forName("com.mysql.jdbc.Driver");
											conn = DriverManager.getConnection(url);
											ps = conn.prepareStatement("SELECT lose FROM user WHERE username=?");
					
											
									ps.setString(1, a.getUsername());
											rs = ps.executeQuery();
											rs.next();
											numlose = rs.getInt("lose");
										}catch (SQLException sqle) {
											sqle.printStackTrace(); 
										}catch(Exception e) {
											e.printStackTrace();
										}
										
										
										int num2;
										try {
											Class.forName("com.mysql.jdbc.Driver");
											conn = DriverManager.getConnection(url);
											
												num2 = this.numlose + 1;
												ps = conn.prepareStatement("UPDATE user SET lose=? WHERE username=?");
												ps.setInt(1, num2);
												ps.setString(2, a.getUsername());
												ps.executeUpdate();
											
										}catch (SQLException sqle) {
											sqle.printStackTrace(); 
										}catch(Exception e) {
											e.printStackTrace();
										}
										
										
									}
								}
//								for(ServerThread x: threads) {
//									if(!x.equals(losers)) {
//										st.sendMessage(new Message(this.userrecords(st, url)));
//										
//										for(ServerThread a: threads) {
//											if(!a.equals(st)) {
//												a.sendMessage(new Message(this.userrecords(a, url)));
//											}
//										}
//									}
//								}
								
								
								st.sendMessage(new Message(this.userrecords(st, url)));
								
								for(ServerThread a: threads) {
									if(!a.equals(st)) {
										a.sendMessage(new Message(this.userrecords(a, url)));
									}
								}
								
								
								//st.sendMessage(new Message(this.userrecords(st, url)));
								
							
								this.broadcast(new Message("Thank you for playing Hangman!"));
								for(ServerThread a:threads) {
									if(!a.equals(st)) {
										a.sendMessage(new Message("Thank you for playing Hangman!"));
									}
								}
								
								//break;
								return;
							
							}else {
								System.out.println(dateFormat.format(new Date()) + ": <" + this.getGamename() + "> <" + st.getUsername() + "> - <" + inputuser + "> is incorrect!");
								System.out.println("<"+st.getUsername() + "> has lost and is no longer in the game.");
								st.sendMessage(new Message("That is incorrect! You lose!\n\n"));
								st.sendMessage(new Message("The word was \"" + word +"\".\n"));
								
								try {
									Class.forName("com.mysql.jdbc.Driver");
									conn = DriverManager.getConnection(url);
									ps = conn.prepareStatement("SELECT lose FROM user WHERE username=?");
									ps.setString(1, st.getUsername());
									rs = ps.executeQuery();
									rs.next();
									numlose = rs.getInt("lose");
								}catch (SQLException sqle) {
									sqle.printStackTrace(); 
								}catch(Exception e) {
									e.printStackTrace();
								}
								
								if(totaluser()==1) {
								int num2;
								try {
									Class.forName("com.mysql.jdbc.Driver");
									conn = DriverManager.getConnection(url);
									
										num2 = this.numlose + 1;
										ps = conn.prepareStatement("UPDATE user SET lose=? WHERE username=?");
										ps.setInt(1, num2);
										ps.setString(2, st.getUsername());
										ps.executeUpdate();
									
								}catch (SQLException sqle) {
									sqle.printStackTrace(); 
								}catch(Exception e) {
									e.printStackTrace();
								}
							}
								if(totaluser()==1) {
								st.sendMessage(new Message(this.userrecords(st, url)));
								
								st.sendMessage(new Message("Thank you for playing Hangman!\n\n"));
								}
//								if(totaluser()>1) {
//								st.sendMessage(new Message("Thank you for playing Hangman! \n\n(Only for Multiplayer mode)===>>You can wait if you want to get a final results for everyone\n\n"));
//								}
//								else {
//									st.sendMessage(new Message("Thank you for playing Hangman! \n\n"));
//								}
								losers.add(st);
								st.playerdead = true;
								this.broadcast(new Message(st.getUsername() + " made a wrong guess!\n" + "<"+st.getUsername() + "> left the game.\n\n"), st);
								if(threads.isEmpty())
								{
									return;
								}
								
								
								break;
							}
							
							
							
						}
						else {
							st.sendMessage(new Message("That is not a valid option.\n"));
						}
					}catch (IOException ioe) {
						System.out.println(st.getUsername() + " has lost and is no longer in the game.");
						
						losers.add(st);
						st.playerdead=true;
						this.broadcast(new Message("<"+st.getUsername() + "> left the game.\n\n"), st);
						if(threads.isEmpty()) return;
						break;
						
					} catch (ClassNotFoundException cnfe) {
						System.out.println("no user");
						System.out.println("cnfe: " + cnfe.getMessage());
					}
				}
				
			}
			
			for(ServerThread lostuser: losers) {
				losersNames = losersNames + lostuser.getUsername() + ", ";
				this.threads.remove(lostuser);
			}
			
			losers.clear();
		}
		
		
	}
	
}
