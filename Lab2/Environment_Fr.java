package freezing_aggregation;

import sweep.SimStateSweep;
import spaces.Spaces;

public class Environment extends SimStateSweep {
	boolean broadRule = false; 
	public int n;
	public double p = 1.0; // Probability p of moving randomly
	
	
	public void makeRemainAgents() {
		for(int i=0; i<n-1;i++) {
			boolean frozen = false;
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			boundedSpaces(x, y,xdir,ydir);
			Agent a = new Agent(x,y,xdir,ydir);
			schedule.scheduleRepeating(a);
			sparseSpace.setObjectLocation(a,x,y);
		}
		
	}
	
	public void makeAgents() {
		for(int i=0; i<n;i++) {
			boolean frozen = false;
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			boundedSpaces(x, y,xdir,ydir);
			Agent a = new Agent(x,y,xdir,ydir);
			schedule.scheduleRepeating(a);
			sparseSpace.setObjectLocation(a,x,y);
		}
		
	}
	
	
	public void placeMidAgent() {
		int x_mid = gridWidth/2;
		int y_mid = gridHeight/2;
		int xdir_mid = 0;
		int ydir_mid = 0;
		boolean frozen = true;
		Agent a = new Agent(x_mid,y_mid,xdir_mid,ydir_mid);
		sparseSpace.setObjectLocation(a,x_mid,y_mid);
		makeRemainAgents();
	}
	
	
	public void boundedSpaces(int x, int y, int xdir, int ydir) {
		if (x+xdir>=600|y+ydir>=600) {
			xdir = -xdir;
			ydir= -ydir;
		}
	}
	
	// get/set n size
	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	// getter and setter for p
	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}
	
	public void start() {
		super.start();
		spaces = Spaces.SPARSE;
		make2DSpace(spaces, gridWidth,gridHeight);
		makeAgents();
	}
	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}
	
}
