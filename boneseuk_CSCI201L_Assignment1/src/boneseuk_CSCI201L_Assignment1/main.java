package boneseuk_CSCI201L_Assignment1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;



//If user's input file is "cannotbefound" or "parsed bad" program terminates.
public class main {

	static ArrayList<Contact> allpeople = new ArrayList<Contact>();
	
	//print out main menu
	public static void printmenu() {
		
		System.out.println("   1) Contact look up\n" + 
				"   2) Add contact\n" + 
				"   3) Delete contact\n" + 
				"   4) Print to a file\n" + 
				"   5) Exit");
	}
	//Parsing function that outputs integer
	//Based on that integer we can identify the error-type.
	public static int parsedata (String filename, String fileline) {
		
		Contact newPerson = new Contact();
		boolean okayadd = false;
		String [] goodPerson;
		String regex1 = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		int personAge = 0;
		goodPerson = fileline.split("\\,");
		if(fileline.equals("")) {
			return 0;
		}
		try{
			if(goodPerson.length < 6) {
				return 6;
			}
			
		}catch(IllegalArgumentException iae) {
			
			
		}
		
	
		if(goodPerson[0].trim().matches( "[a-zA-Z]*" )) {
			okayadd=true;
			newPerson.firstName=goodPerson[0].trim();
		}
		else {
			return 1;
		}
		
		if(goodPerson[1].trim().matches( "[a-zA-Z]*" )) {
			okayadd=true;
			newPerson.lastName=goodPerson[1].trim();
		}else {
			
			return 2;
		}
		
		if((goodPerson[2].trim().matches(regex1))){
			if((goodPerson[2].trim().contains(".com"))||
				(goodPerson[2].trim().contains(".edu"))||
				(goodPerson[2].trim().contains(".net"))) {
				okayadd=true;
				newPerson.emailAddress=goodPerson[2].trim();
			}
			else {
				
				return 3;
			}
		}else {
			return 3;
		}
		try {
			
			personAge=Integer.parseInt(goodPerson[3].trim());
			okayadd=true;
		}catch(NumberFormatException nfe){
			okayadd=false;
			return 4;
		}
		newPerson.age = personAge;
		
		if(goodPerson[4].trim().equalsIgnoreCase("TRUE")) {
			okayadd=true;
			newPerson.nearCampus = true;
		}
		else if(goodPerson[4].trim().equalsIgnoreCase("FALSE")) {
			okayadd=true;
			newPerson.nearCampus = false;
		}
		else {
			okayadd=false;
			return 5;
		}
		
		for(int i = 5; i < goodPerson.length; i++){
			okayadd=true;
			newPerson.notes.add(goodPerson[i]);
		}
		
		if(okayadd == true) {
			allpeople.add(newPerson);
		}
		
		return 0;	
		
	}
	
		//main function that scans and writes the data.
		public static void main(String [] args) {
			Scanner in = new Scanner(System.in);
			while(true) {	
			System.out.print("What is the name of the contacts file? ");
			
			String name = in.nextLine();
			FileReader FR = null;
			BufferedReader BR = null;
			
			boolean validfile = true;
			try {
				File file = new File(name);
				FR = new FileReader(file);
				BR = new BufferedReader(FR);
				String line;
				
				
				while ((line = BR.readLine()) != null) {
					String[] temp = line.split("\\,");
					if(parsedata(name, line)== 0) {
						line = new BufferedReader(FR).readLine();
					}
					else if(parsedata(name, line)==6) {
						validfile = false;
						System.out.println("");
						System.out.println("This file "+file+" is not formatted properly.");
						System.out.println("There are not enough parameters on line ");
						System.out.println("'"+line+"’.\n");
						System.out.println("");
						return;
						
					}
					else if(parsedata(name,line)==1) {
						validfile = false;
						String fName = temp[0];
						System.out.println("");
						System.out.println("This file "+file+" is not formatted properly.");
						System.out.println("The parameter “"+fName+"” cannot be parsed as an firstname");
						System.out.println("");
						
					}
					else if(parsedata(name,line)==2) {
						validfile = false;
						//quit = true;
						String lName = temp[1];
						System.out.println("");
						System.out.println("This file "+file+" is not formatted properly.");
						System.out.println("The parameter “"+lName+"” cannot be parsed as an lastname");
						System.out.println("");
						
					}
					else if(parsedata(name, line)== 3) {
						validfile = false;
						//quit = true;
						String eMail = temp[2];
						System.out.println("");
						System.out.println("This file "+file+" is not formatted properly.");
						System.out.println("The parameter “"+eMail+"” cannot be parsed as an email");
						System.out.println("");
						
					}
					else if(parsedata(name, line)== 4) {
						validfile = false;
						String AGE = temp[3];
						System.out.println("");
						System.out.println("This file "+file+" is not formatted properly.");
						System.out.println("The parameter “"+AGE+"” cannot be parsed as an age");
						System.out.println("");
						
					}
					else if(parsedata(name, line)== 5) {
						validfile = false;
						String NCampus = temp[4];
						System.out.println("");
						System.out.println("This file "+file+" is not formatted properly.");
						System.out.println("The parameter “"+NCampus+"” cannot be parsed as near campus");
						System.out.println("");
					    return;
						
					}
							
					
				} 
				
				
			} catch (FileNotFoundException fnfe) {
				System.out.println("The file "+ name +" could not be found.\n");
				return;
				
			}
			catch (IOException ioe) {
				return;
			}
			
			if(validfile == true) 
			{
				break;
			}
			
		}
		

		while(true) {
			System.out.println("");
			printmenu();
			String option = "";
			
			while(true) {
				
				System.out.println("");
			
				try{
						
					System.out.print("What option would you like to select? ");
						
					option = in.nextLine();
					
					
					if(!(option.equals("5")) && !(option.equals("4"))  && 
							!(option.equals("3"))  && !(option.equals("2"))  && !(option.equals("1")) &&!(option.equals("EXIT")) )
					{
						System.out.println("\nThat is not a valid option.");
					}
					
					
					
				}catch(InputMismatchException ime) {
					System.out.println("\nThat is not a valid option.");
				}
			
			
				if(option.equals("1") || option.equals("2") || option.equals("3")
						||option.equals("4") ||option.equals("5")||option.equals("EXIT")) {
					break;
					}
			}
			if(option.equals("1")) {
				System.out.println("");
				System.out.print("Enter the contact’s last name. ");
				String lname = in.nextLine();
				boolean tempo = false;
				System.out.println("");
				Collections.sort(allpeople, new sortcomp());
				for(int i =0; i<allpeople.size();i++)
				{
					if(lname.equalsIgnoreCase(allpeople.get(i).lastName))
					{
						tempo = true; 
						System.out.println("Name: "+ allpeople.get(i).firstName+" "+
						allpeople.get(i).lastName);
						System.out.println("Email: "+allpeople.get(i).emailAddress);
						System.out.println("Age: "+allpeople.get(i).age);
						System.out.println("Near Campus: "+allpeople.get(i).nearCampus);
						
						System.out.print("Notes: ");
						for(int j = 0; j<allpeople.get(i).notes.size(); j++) {
							if( j != allpeople.get(i).notes.size()-1) {
								System.out.print(allpeople.get(i).notes.get(j)+", ");
							}
							else
							{
								System.out.println(allpeople.get(i).notes.get(j));
								System.out.println("");
							}
						}
						
					}
					else {
						
					}
				}
				if(tempo == false) {
					System.out.println("There is no one with the last name "+lname+" in your contact book.");

				}
				
				
			}
			if(option.equals("2")) {
				System.out.println("");
				Contact newPerson1 = new Contact();
				String newfName = "";
				String newlName = "";
				String newemail = "";
				String newage = "";
				int nage = 0;
				String nnotes = "";
				String nNC = "";
				boolean newNC = false;
				ArrayList<String>newnotes = new ArrayList<String>();
				
				while(true) {
				System.out.print("What is the first name of your new contact? ");
				newfName = in.nextLine();
				System.out.println("");
				if(newfName.trim().matches( "[a-zA-Z]*" )) {
					break;
				}
				else {
					System.out.println("That is not a valid first name. An first name must have this formatting: ");
					System.out.println("Abcd");
					System.out.println("");

				}
				
			   }
				while(true) {
					System.out.print("What is the last name of your new contact? ");
					newlName = in.nextLine();
					System.out.println("");
					if(newlName.trim().matches( "[a-zA-Z]*" )) {
						break;
					}
					else {
						System.out.println("That is not a valid last name. An last name must have this formatting: ");
						System.out.println("Abcd");
						System.out.println("");

					}
				}
				while(true) {
					String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
					System.out.print("What is the email of your new contact? ");
					newemail = in.nextLine();
					if(newemail.trim().matches(regex)) {
						if((newemail.trim().contains(".com"))||
								(newemail.trim().contains(".edu"))||
								(newemail.trim().contains(".net"))) {
						System.out.println("");
						break;
						}
						else
						{
							System.out.println("");
							System.out.println("That is not a valid email. An email must have this formatting: ");
							System.out.println("xxx@yyy.com");
							System.out.println("");
						}
					}
					else {
						System.out.println("");
						System.out.println("That is not a valid email. An email must have this formatting: ");
						System.out.println("xxx@yyy.com");
						System.out.println("");
					}
				}
				while(true) {
					System.out.print("What is the age of your new contact? ");
					newage = in.nextLine();
					System.out.println("");
					try {
						nage=Integer.parseInt(newage);
					}catch(NumberFormatException nfe){
						System.out.println("That is not a valid age. An age must have this formatting: ");
						System.out.println("20 (integer)");
						System.out.println("");
						continue;
					}
					break;
				}
				while(true) {
					System.out.print("Does your new contact live near campus? ");
					nNC = in.nextLine();
					System.out.println("");
					if(nNC.trim().equalsIgnoreCase("yes")) {
						
						newNC = true;
						break;
					}
					else if(nNC.trim().equalsIgnoreCase("no")) {
						
						newNC = false;
						break;
					}
					else {
						System.out.println("Invalid Input! Answer Yes or No!");
						System.out.println("");
					}
				}
				
				
				
				boolean quit = false;
				while(!quit) {
					System.out.print("Add a note about your new contact. ");
					nnotes = in.nextLine();
					newnotes.add(nnotes);
					System.out.println("");
					System.out.print("Do you want to add another note? ");
				    String response = in.nextLine();
				    if(response.trim().equalsIgnoreCase("yes")) {
				    	System.out.println("");
				    	continue;
				    }
				    else if(response.trim().equalsIgnoreCase("no")){
				    	System.out.println("");
				    	break;
				    }
				    else {
				    	System.out.println("");
				    	System.out.println("Invalid input");
				    	while(true) {
							System.out.print("Do you want to add another note? ");
				    		String temp = in.nextLine();
				    		if(temp.trim().equalsIgnoreCase("yes")) {
				    			System.out.println("");
						    	break;
						    }
						    else if(temp.trim().equalsIgnoreCase("no")){
						    	System.out.println("");
						    	quit = true;
						    	break;
						    }
						    else {
						    	System.out.println("Invalid input");
						    }
				    	}
				    }
				}
					
				
				System.out.println(newfName+" "+newlName+" has been added to your contact list.");
				
				newPerson1.setfirstname(newfName);
				newPerson1.setlastname(newlName);
				newPerson1.setemail(newemail);
				newPerson1.setage(newage);
				newPerson1.setNC(nNC);
				newPerson1.setnotes(newnotes);
				allpeople.add(newPerson1);
				
				
			}
			
			
			
			if(option.equals("3")) {
				boolean keepasking = true;
				while(keepasking) {
					System.out.println("");
					System.out.print("Enter the email of the contact you would like to delete.");
					String emailinput = in.nextLine();
					System.out.println("");
					boolean validemail = false;
					
				
				
				
					for(int i = 0; i < allpeople.size();i++) {
						
						if(emailinput.trim().equalsIgnoreCase(allpeople.get(i).getemailaddress())){
							validemail = true;
							keepasking = false;
							System.out.println(allpeople.get(i).getfirstname()+" "+allpeople.get(i).getlastname()+" "+
							"was successfully deleted from your contact list.");
							allpeople.remove(i);
							//System.out.println("");
							
							break;
						}
						
						
					}
					if(validemail == false) {
						System.out.println(emailinput+" does not exist in your contact list.");
						keepasking = true;
						continue;
					}
				
				}
					
			}
			
			if(option.equals("4")) {
				System.out.println("");
				System.out.print("Enter the name of the file that you would like to print your contact\n" + 
						"list to.");
				String outputname = "";
				try {
				outputname = in.nextLine();
				File output = new File(outputname);
				
				@SuppressWarnings("resource")
				FileWriter fileWriter = new FileWriter(output);
				
				for(int i = 0; i<allpeople.size(); i++) {
					StringBuilder temp = new StringBuilder();
					temp.append(allpeople.get(i).getfirstname());
					temp.append(",");
					temp.append(allpeople.get(i).getlastname());
					temp.append(",");
					temp.append(allpeople.get(i).getemailaddress());
					temp.append(",");
					temp.append(allpeople.get(i).getage());
					temp.append(",");
					temp.append(allpeople.get(i).getnearcampus());
					
					for( int j = 0; j<allpeople.get(i).getnotes().size(); j++) {
						
						temp.append(",");
						temp.append(allpeople.get(i).getnotes().get(j));
					}
					temp.append("\n");
					temp.toString();
					fileWriter.write(temp.toString());
					fileWriter.flush();
				}
				}
				catch(IOException ioe) {
					
				}
				System.out.println("");
				System.out.println("Successfully printed all your contacts to "+outputname);
				
				
			}
			
			if((option.equals("5"))||(option.equals("EXIT"))){
				System.out.println("");
				System.out.println("Thank you for using my contacts program. Goodbye!");
				break;
			}

			
		}
	
}
	
}
