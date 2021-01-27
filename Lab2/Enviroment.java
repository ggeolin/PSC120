package freezing_aggregation;

import spaces.Spaces;
import sweep.SimStateSweep;

public class Enviroment extends SimStateSweep {
	public int n = 100;		//default number of agent to create.
	public double p = 1.0; 		//probability of random movement.
	public int searchRadius = 2;	
	public boolean broadRule = false;
	public boolean bounded = false;

	public boolean isBounded() {
		return bounded;
	}

	public void setBounded(boolean bounded) {
		this.bounded = bounded;
	}

	public boolean isBroadRule() {
		return broadRule;
	}

	public void setBroadRule(boolean broadRule) {
		this.broadRule = broadRule;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}
	
	public void makeAgents() {
		Agent center = new Agent(gridWidth/2, gridHeight/2, 0, 0, true);
		schedule.scheduleRepeating(center);
		sparseSpace.setObjectLocation(center,gridWidth/2,gridHeight/2);
		
		for(int i=0; i<n-1;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			int xdir = random.nextInt(3)-1;
			int ydir = random.nextInt(3)-1;
			Agent a = new Agent(x,y,xdir,ydir,false);
			schedule.scheduleRepeating(a);
			sparseSpace.setObjectLocation(a,x,y);
		}
	}
	
	public Enviroment(long seed, Class observer) {
		super(seed, observer);
	}
	
	public void start() {
		super.start();
		spaces = Spaces.SPARSE;
		make2DSpace(spaces, gridWidth, gridHeight);
		makeAgents();
	}

}
