package MyMath;

public class MyClass {

	public static void main(String[] args) {
		try {
			MyClass myclass = new MyClass();
			myclass.arithmetic("?",20,25);
		}
		catch(Exception e) {
			System.out.println("Enter valid argument plz!");
		}
		

	}
	public double arithmetic(String operator, double value1, double value2) {
		double result;
		String key = operator.toLowerCase();
		//System.out.println(key);
		switch(key){
		 default:
		    System.out.println("Operator "+"'"+operator+"'"+" is not defined");
		    break;
	    case "add" :
	    	result = value1+ value2;
	    	System.out.println(result);
	    	break;
	    case "plus":
	    	result = value1+ value2;
	    	System.out.println(result);
	    	break;
	    case "+":
	    	result = value1+ value2;
	    	System.out.println(result);
	    	break;
	    case "subtract":
	    	result = value1 - value2;
	    	System.out.println(result);
	    	break;
	    case "minus":
	    	result = value1 - value2;
	    	System.out.println(result);
	    	break;
	    case "-":
	    	result = value1 - value2;
	    	System.out.println(result);
	    	break;
	    case "multiply":
	    	result = value1 * value2;
	    	System.out.println(result);
	    	break;
	    case "times":
	    	result = value1 * value2;
	    	System.out.println(result);
	    	break;
	    case "*":
	    	result = value1 * value2;
	    	System.out.println(result);
	    	break;
	    case "divide":
	    	result = value1 / value2;
	    	System.out.println(result);
	    case "/":
	    	result = value1 / value2;
	    	System.out.println(result);
		}
		
			
		return 0;
		
}
}
