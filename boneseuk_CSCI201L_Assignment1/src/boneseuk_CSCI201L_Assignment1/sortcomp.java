package boneseuk_CSCI201L_Assignment1;

import java.util.Comparator;

public class sortcomp implements Comparator<Contact>{
	public int compare(Contact a, Contact b) {
		if(a.lastName.equals(b.lastName))
		{
			return a.firstName.compareTo(b.firstName);
		}
		else {
			return a.lastName.compareTo(b.lastName);
		}
	}
	
}
