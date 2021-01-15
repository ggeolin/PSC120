package talkers;

public class MyMath {

    public static double arithmetic(String operator, double value1, double value2) {
    	String key = operator.toLowerCase();
    	double val = 0;

		switch(key) {
			case "add":
				val = value1 + value2;
				break;
			case "plus":
				val = value1 + value2;
				break;
			case "+":
				val = value1 + value2;
				break;
			case "subtract":
				val = value1 - value2;
				break;
			case "minus":
				val = value1 - value2;
				break;
			case "-":
				val = value1 - value2;
				break;
			case "multiply":
				val = value1 * value2;
				break;
			case "times":
				val = value1 * value2;
				break;
			case "*":
				val = value1 * value2;
				break;
			case "divide":
				val = value1 / value2;
				break;
			case "/":
				val = value1 / value2;
				break;
			default:
			    System.out.println("Operator "+"'"+operator+"'"+" is not defined");
		}
		return val;
	 }

	public static void main(String[] args) {
		double x = arithmetic("divide", 2, 1);
		System.out.println(x);
	}

}
