package khModel;

import sim.util.Bag;
import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep {
	public int mated_num = 1;
	public int n = 50;  //n is the number of agents
	public double active = 1.0;//probability of activity
	public double p = 1.0; //random movement
	public boolean oneCellPerAgent = true;//controls whether agents can occupy the same place or not
	public boolean replacement = false;


	public double aggregate = 0.0;//probability of aggregating
	public int searchRadius = 1;// the radius or view of an agent when aggregating

	/*
	 * KH Model parameters
	 */
	public boolean nonSpatialModel = false;//original non-spatial KH model
	public int males = 1000;//size of the population of males
    public int females = 1000;
    public double maxAttractiveness = 10;//maximum attractiveness
    public double choosiness =3;//exponent in rule for calculating probabilities
    public double maxDates = 50;//maximum number of dates.
    public Rule rule= Rule.ATTRACTIVE;//Constant for the decision rules
    public int ruleNumber = 0;
    Bag male = new Bag();//the population males
    Bag female = new Bag();//the current population of females
    Bag nextMale = new Bag();//population of males for next step 
    Bag nextFemale = new Bag();
    public Experimenter experimenter = null;

    
	
    public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}
    
    /**
     * Reset global variables
     */
    public void reset() {
    	male.clear();
    	female.clear();
    	nextMale.clear();
    	nextFemale.clear();
    }
    
	public boolean isReplacement() {
		return replacement;
	}

	public void setReplacement(boolean replacement) {
		this.replacement = replacement;
	}

	public boolean isNonSpatialModel() {
		return nonSpatialModel;
	}


	public void setNonSpatialModel(boolean nonSpatialModel) {
		this.nonSpatialModel = nonSpatialModel;
	}


	public int getMales() {
		return males;
	}

	public void setMales(int males) {
		this.males = males;
	}

	public int getFemales() {
		return females;
	}

	public void setFemales(int females) {
		this.females = females;
	}

	public double getMaxAttractiveness() {
		return maxAttractiveness;
	}

	public void setMaxAttractiveness(double maxAttractiveness) {
		this.maxAttractiveness = maxAttractiveness;
	}

	public double getChoosiness() {
		return choosiness;
	}

	public void setChoosiness(double choosiness) {
		this.choosiness = choosiness;
	}

	public double getMaxDates() {
		return maxDates;
	}

	public void setMaxDates(double maxDates) {
		this.maxDates = maxDates;
	}

	public int getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(int ruleNumber) {
		this.ruleNumber = ruleNumber;
		this.rule = (Rule.values())[ruleNumber];
	}

	public Object domRuleNumber() {
		return Rule.values();
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getActive() {
		return active;
	}

	public void setActive(double active) {
		this.active = active;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public boolean isOneCellPerAgent() {
		return oneCellPerAgent;
	}

	public void setOneCellPerAgent(boolean oneCellPerAgent) {
		this.oneCellPerAgent = oneCellPerAgent;
	}

	public double getAggregate() {
		return aggregate;
	}

	public void setAggregate(double aggregate) {
		this.aggregate = aggregate;
	}

	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	/**
	 * Creates agents and places them in space and on the schedule.
	 */
	
	public void makeAgentsSpatial() {
		for(int i=0;i<females;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			double attractiveness = random.nextInt((int)maxAttractiveness)+1;
			Agent f = new Agent(x, y, true,attractiveness);
			f.event = schedule.scheduleRepeating(f);
			sparseSpace.setObjectLocation(f,x, y);
			gui.setOvalPortrayal2DColor(f, (float)1, (float)0, (float)0, (float)(attractiveness/maxAttractiveness));
		}
		for(int i=0;i<males;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			double attractiveness = random.nextInt((int)maxAttractiveness)+1;
			Agent m = new Agent(x, y, false,attractiveness);
			m.event = schedule.scheduleRepeating(m);
			sparseSpace.setObjectLocation(m,x, y);
			gui.setOvalPortrayal2DColor(m, (float)0, (float)0, (float)1, (float)(attractiveness/maxAttractiveness));
		}
	}
	
	public void makeAgentNonSpatial() {
		for(int i=0;i<females;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			double attractiveness = random.nextInt((int)maxAttractiveness)+1;
			Agent f = new Agent(x, y, true,attractiveness);
			f.event = schedule.scheduleRepeating(f);
			sparseSpace.setObjectLocation(f,random.nextInt(gridWidth), random.nextInt(gridHeight));
			gui.setOvalPortrayal2DColor(f, (float)1, (float)0, (float)0, (float)(attractiveness/maxAttractiveness));
			female.add(f);
		}
		for(int i=0;i<males;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			double attractiveness = random.nextInt((int)maxAttractiveness)+1;
			Agent m = new Agent(x, y, false,attractiveness);
			m.event = schedule.scheduleRepeating(m);
			sparseSpace.setObjectLocation(m,random.nextInt(gridWidth), random.nextInt(gridHeight));
			gui.setOvalPortrayal2DColor(m, (float)0, (float)0, (float)1, (float)(attractiveness/maxAttractiveness));
			male.add(m);
		}
	}
	
	public void makeAgents() {
		if(nonSpatialModel) {
			makeAgentNonSpatial();
			
		}
		else {
			makeAgentsSpatial();
		}
	}

	public void start() {
		super.start();
		reset();
		this.make2DSpace(Spaces.SPARSE, gridWidth, gridHeight);
		makeAgents();
		if(observer != null) {
			observer.initialize(sparseSpace, Spaces.SPARSE);//initialize the experimenter by calling initialize in the parent class
			experimenter = (Experimenter)observer;//cast observer as experimenter
			experimenter.resetVariables();
		}
	}
}
