package talkers;

public class MyMath {
	
	public static double arithmetic(String operator, double value1, double value2) {
		//subtracts, add, multiply, divide
		double result = 0;
		String key = operator.toLowerCase();
		
		switch(key) {
			case "add":
				result = value1 + value2;
				break;
			case "plus":
				result = value1 + value2;
				break;
			case "+":
				result = value1 + value2;
				break;
			case "subtract":
				result = value1 - value2;
				break;
			case "minus":
				result = value1 - value2;
				break;
			case "-":
				result = value1 - value2;
				break;
			case "multiply":
				result = value1 * value2;
				break;
			case "times":
				result = value1 * value2;
				break;
			case "*":
				result = value1 * value2;
				break;
			case "divide":
				result = value1 / value2;
				break;
			case "/":
				result = value1 / value2;
				break;
			default:
				System.out.println("Operator "+"'"+operator+"'"+" is not defined");
				result = Double.NaN; //return not a number if operator not defined
		}//end switch statement
		return result;
	}
	
	public static void main(String[] args) {
		double x = arithmetic("*",2,3);
		System.out.println(x);
	}

}
