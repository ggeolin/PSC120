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
	
	public void printRandomList(int length) {
		for (int i = 1; i <= length; i++) {
			int rand_int = (int)(Math.random() * (double)length) + 1; 
			System.out.println(getMonth(rand_int));
		}
	}
	
	public void printMonths() {
		for (int i = 1; i <= 12; i++) {
			System.out.println(getMonth(i));
		}
	}
	
	public void printRangeOfMonths(int start, int end) {
		for (int i = start; i <= end; i++) {
			System.out.println(getMonth(i));
		}
	}
	
	public void printMonthNumber(String monthName) {
		int return_val = -1;
		switch (monthName) {
		case "January":
			return_val = 1;
			break;
		case "February":
			return_val = 2;
			break;
		case "March":
			return_val = 3;
			break;
		case "April":
			return_val = 4;
			break;
		case "May":
			return_val = 5;
			break;
		case "June":
			return_val = 6;
			break;
		case "July":
			return_val = 7;
			break;
		case "August":
			return_val = 8;
			break;
		case "September":
			return_val = 9;
			break;
		case "October":
			return_val = 10;
			break;
		case "November":
			return_val = 11;
			break;
		case "December":
			return_val = 12;
			break;
		}
		System.out.println(return_val);
	}
	
	public void number_to_date(int num) {
		boolean flag = false;
		if (num < 0 || num > 365) {
			System.out.println("Invaild day ");
			flag = true;
		}
		
		if(num <= 31 && !flag) {
			System.out.println("January " + num);
		} else if(num <= 59) {
			System.out.println("Feburay " + (num - 31));
		} else if(num <= 90) {
			System.out.println("March " + (num - 59));
		} else if(num <= 121) {
			System.out.println("April " + (num - 90));
		} else if(num <= 151) {
			System.out.println("May " + (num - 121));
		} else if(num <= 182) {
			System.out.println("June " + (num - 151));
		} else if(num <= 212) {
			System.out.println("July " + (num - 182));
		} else if(num <= 243) {
			System.out.println("August " + (num - 212));
		} else if(num <= 273) {
			System.out.println("September " + (num - 243));
		} else if(num <= 304) {
			System.out.println("October " + (num - 273));
		} else if(num <= 334) {
			System.out.println("November " + (num - 304));
		} else {
			System.out.println("December " + (num - 334));
		}
	}
}



















