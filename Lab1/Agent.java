package month_list;

public class Agent {
	// Prints out the string name for the month corresponding to the int
	public String getMonth (int month) {
		String return_str = "";
		switch (month) {
		case 1:
			return_str = "January";
			break;
		case 2:
			return_str = "February";
			break;
		case 3:
			return_str = "March";
			break;
		case 4:
			return_str = "April";
			break;
		case 5:
			return_str = "May";
			break;
		case 6:
			return_str = "June";
			break;
		case 7:
			return_str = "July";
			break;
		case 8:
			return_str = "August";
			break;
		case 9:
			return_str = "September";
			break;
		case 10:
			return_str = "October";
			break;
		case 11:
			return_str = "November";
			break;
		case 12:
			return_str = "December";
			break;
		}
		return return_str;
	}
}
