package boneseuk_CSCI201L_Assignment1;

import java.util.ArrayList;

public class Contact {
	
	
	Contact() 
	{
		notes = new ArrayList<String>();
	}	
		String firstName;
		String lastName;
		String emailAddress;
		int age;
		boolean nearCampus;
		ArrayList<String> notes;
		public void setfirstname(String firstname) {
			this.firstName = firstname;
		}
		public void setlastname(String lastname) {
			this.lastName = lastname;
		}
		public void setemail(String email) {
			this.emailAddress = email;
		}
		public void setage(String a) {
			this.age = Integer.parseInt(a);
		}
		public void setNC(String b) {
			this.nearCampus = Boolean.parseBoolean(b);
		}
		public void setnotes(ArrayList<String> n) {
			this.notes.addAll(n);
		}
		
		
		public String getfirstname() {
			return firstName;
		}
		public String getlastname() {
			return lastName;
		}
		
		public String getemailaddress() {
			return emailAddress;
		}
		public int getage() {
			return age;
		}
		public boolean getnearcampus() {
			return nearCampus;
		}
		public ArrayList<String> getnotes(){
			return notes;
		}

}
