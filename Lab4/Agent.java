package evolutionaryGames;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class Agent implements Steppable {
	public Stoppable event;
	long id;
	Strategy strategy;
	double resources;
	boolean played = false;
	Memory memory = null;
	int x; //location on the x-axis
	int y; //location on the y-axis
	int xdir; //x direction of change
	int ydir; //y direction of change
	int age; //current age of agent
	int maxAge;//age at which agent dies

	public Agent(Environment state, long id, Strategy strategy, double resources, int x, int y, int xdir, int ydir, boolean startup) {
		super();
		this.id = id;
		this.strategy = strategy;
		this.resources = resources;
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		maxAge = (int)(state.averageAge + state.random.nextGaussian()*state.sdAge);
		if(startup) {
			age = state.random.nextInt((int)state.averageAge);
		}
		else {
			age = 0;
		}
		// Uncomment to create the memory
		switch(strategy) {
			case TFTM: ; case TFTS:; case PAVLOVM:; case PAVLOVS: memory = new Memory(state.memorySize);
		}
		
	}
	
	/**
	 * Returns the strategy of an opponent
	 * @param opponent
	 * @return
	 */
	public Strategy getStrategy(Agent opponent) {
		switch(strategy) {
		case COOPERATOR:
			return Strategy.COOPERATOR;
		case DEFECTOR:
			return Strategy.DEFECTOR;
		case WALKAWAY:
			return Strategy.COOPERATOR;
		case TFTM:; case TFTS:
			return getStrategyTFT(opponent);
		case PAVLOVM:; case PAVLOVS:
			return getStrategyPAVLOV(opponent);
		case WALKAWAYD:
			return Strategy.DEFECTOR;
		default:
			return Strategy.COOPERATOR;
		}
		/*
		 * See lab for instructions to complete
		 * [COMPLETE]
		 */
	}
	
	public Strategy getStrategyTFT(Agent opponent) {
		/*
		 * See lab for instructions to complete
		 * [COMPLETE]
		 */
        Triple m = memory.getLastMemory(opponent);
        if(m == null) {
            return Strategy.COOPERATOR;	//if it has no memory of an agent, it always cooperates
        }
        switch(m.opponentStrategy) {	//this is what it remembers its opponent previous strategy
	        case COOPERATOR:
	            return Strategy.COOPERATOR;
	        case DEFECTOR:
	            return Strategy.DEFECTOR;
	        default: 					//not necessary but here for completeness
	            return Strategy.COOPERATOR;
        }
	}
	
	public Strategy getStrategyPAVLOV(Agent opponent) {
		/*
		 * See lab for instructions to complete
		 * [COMPLETE]
		 */
		Triple m = memory.getLastMemory(opponent);
		if(m == null) {
			return Strategy.COOPERATOR; 	//Like TFT, PAVLOV cooperates if it has no memory of previously playing an agent
		}
		switch(m.opponentStrategy) {
			case COOPERATOR:
				return m.myStrategy; 		//Use strategy appropriate for that opponent 
			case DEFECTOR:
				switch(m.myStrategy) {		//this is where the "learning" comes in, switching to find a better strategy
					case COOPERATOR:
						return Strategy.DEFECTOR;
					case DEFECTOR:
						return Strategy.COOPERATOR;
				}
			default:
				return Strategy.COOPERATOR;
		}
	}

	/**
	 * Calculates an agent's payoff given the strategy it played and the strategy of its opponent.
	 * @param state
	 * @param opponent
	 * @return
	 */
	public Strategy play(Environment state, Agent opponent) {
		Strategy myStrategy = getStrategy(opponent);
		Strategy myOpponentStrategy = opponent.getStrategy(this);
		switch(myOpponentStrategy) {
		case COOPERATOR:
			switch(myStrategy) {
			case COOPERATOR:
				resources += state.cooperate_cooperator;
				break;
			case DEFECTOR:
				resources += state.defect_cooperator;
				break;
			}
			break;
		case DEFECTOR:
			switch(myStrategy) {
			case COOPERATOR:
				resources += state.cooperate_defector;
				break;
			case DEFECTOR:
				resources += state.defect_defector;
				break;
			}
			break;
		}
		this.played = true;
		//Good place to add code for remembering an opponent. An agent that
        //is TFT or PAVLOV must remember the opponent it played.
        //memory.storeMemory(opponent, myOpponentStrategy, myStrategy);
		switch(myStrategy) {
			case TFTM:; case TFTS:; case PAVLOVM:; case PAVLOVS:
				memory.storeMemory(opponent, myOpponentStrategy, myStrategy);
				break;
		}
		
		return myOpponentStrategy;
	}

	/**
	 * An agent using a mobile strategy first searches for agents in its search radius.  If there are none, then
	 * it randomly moves then serches again for an opponent to play.  If so they play.  
	 * @param state
	 */
	public void mobileStrategy(Environment state) {
		Bag agents = search(state);
		if(agents == null || agents.numObjs == 0) 
		{
			if(state.random.nextBoolean(state.active)) {
				xdir = state.random.nextInt(3)-1;
				ydir = state.random.nextInt(3)-1;
			}
			placeAgent( state);
			agents = search(state);
			if(agents == null) {return;}
			Agent opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			play(state, opponent);
			opponent.play(state, this);
		}
		else {
			Agent opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			play(state, opponent);
			opponent.play(state, this);
		}
	}
	/**
	 * A walkaway strategy is just like a mobile strategy except that if a defection is encountered, it moves randomly.
	 * @param state
	 */
	public void walkawayStrategy(Environment state) {
		Bag agents = search(state);
		Agent opponent;
		Strategy opponentStrategy;
		if(agents == null || agents.numObjs == 0) {
			if(state.random.nextBoolean(state.active)) {
				xdir = state.random.nextInt(3)-1;
				ydir = state.random.nextInt(3)-1;
			}
			placeAgent( state);
			agents = search(state);
			if(agents == null) {return;}
			opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			opponentStrategy = play(state, opponent);
			opponent.play(state, this);
		}
		else {
			opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			opponentStrategy = play(state, opponent);
			opponent.play(state, this);
		}
		if(opponentStrategy == Strategy.DEFECTOR) { //walk away
			if(state.random.nextBoolean(state.active)) {
				xdir = state.random.nextInt(3)-1;
				ydir = state.random.nextInt(3)-1;
			}
			placeAgent( state);
		}
	}
	
	public void stationaryStrategy(Environment state) {
		Bag agents = search(state);
		Agent opponent;
		if(agents == null || agents.numObjs == 0) {
			return;
		}
		else {
			opponent = findOpponent(state, agents);
			if(opponent == null) {return;}
			play(state, opponent);
			opponent.play(state, this);
		}
	}

	public void play(Environment state) {
		switch(strategy) {
		case COOPERATOR:; case DEFECTOR:; case TFTM:; case PAVLOVM:
			mobileStrategy(state);
			break;
		case WALKAWAY:; case WALKAWAYD:
			walkawayStrategy(state);
			break;
        case TFTS:; case PAVLOVS:
            stationaryStrategy(state);//finally the stationary strategies
            break;
			/*
			 * See lab on intructions how to complete
			 * [COMPLETE]
			 */
		}
	}
	

	public Agent findOpponent(Environment state, Bag agents) {
		if(agents == null || agents.numObjs == 0) return null;
		Agent a = (Agent)agents.objs[state.random.nextInt(agents.numObjs)];//get a random agent
		while(a.played && a.equals(this)) {
			agents.remove(a);
			if(agents.numObjs == 0) {
				return null; //none to be found
			}
			a = (Agent)agents.objs[state.random.nextInt(agents.numObjs)];//get another random agent
		}
		return a;
	}


	public Strategy mutation(Environment state) {
		Strategy newStrategy;
		if(state.mutationRange) {
			if(state.random.nextBoolean(state.mutationRate)) {
				newStrategy = state.mutationList.get(state.random.nextInt(state.mutationList.size()));
				while(newStrategy == this.strategy) { //find a strategy different from the parent
					newStrategy = state.mutationList.get(state.random.nextInt(state.mutationList.size()));
				}
				return newStrategy;
			}
			else {
				return this.strategy; //the current agent
			}
		}
		else {
			if(state.random.nextBoolean(state.mutationRate)) {
				newStrategy = Strategy.values()[state.random.nextInt(Strategy.values().length)];
				while(newStrategy == this.strategy) { //find a strategy different from the parent
					newStrategy = Strategy.values()[state.random.nextInt(Strategy.values().length)];
				}
				return newStrategy;
			}
			else {
				return this.strategy; //the current agent
			}
		}
	}

	public Int2D findLocation(Environment state) {
		if(!state.groups) {
			Int2D location = state.sparseSpace.getRandomEmptyMooreLocation(state, x, y, state.reproductionRadius, state.sparseSpace.TOROIDAL, false);
			return location;
		}
		else {
			if(state.reproductionRadius == 0) {
				return new Int2D(x,y);
			}
			else {
				int xch = state.random.nextInt(state.reproductionRadius+1);
				int ych = state.random.nextInt(state.reproductionRadius+1);
				int newx = state.sparseSpace.stx(x+xch);
				int newy = state.sparseSpace.sty(y+ych);
				return new Int2D(newx,newy);
			}
		}
	}

	public Agent replicate(Environment state) {
		Int2D location;
		if(state.localReproduction) {
			location = this.findLocation(state);
		}
		else {
			if(!state.groups)
				location = state.uniqueXY();
			else
				location = state.locationXY();
		}
		if(location == null) {
			resources = 0; //reproduction failed
			return null; //reproduction cannot proceed because there is not place for the agent
		}
		if(carryingCapacityExceeded(state)) {
			resources = 0; //reproduction failed
			return null; //exceeds 
		}
		int xdir = state.random.nextInt(3)-1;
		int ydir = state.random.nextInt(3)-1;
		Strategy newStrategy = mutation(state);
		long newId = state.id++;
		Agent a = new Agent(state,newId, newStrategy,0,location.x,location.y,xdir,ydir, false);
		a.event = state.schedule.scheduleRepeating(a);
		state.sparseSpace.setObjectLocation(a, location.x, location.y);
		colorByStrategy(a.strategy, state,  a);
		return a;
	}

	public void colorByStrategy(Strategy strategy, Environment state, Agent a) {
		switch(strategy) {
		case COOPERATOR: 
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)0, (float)1, (float)1);
			break;
		case DEFECTOR:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)0, (float)1);
			break;
		case WALKAWAY:
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)1, (float)0, (float)1);
			break;
		case WALKAWAYD:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0.6, (float)0, (float)1);
			break;
		case TFTM:
			state.gui.setOvalPortrayal2DColor(a, (float)0.3, (float)0.7, (float)0.5, (float)1);
			break;
		case TFTS:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0.6, (float)0.4, (float)1);
			break;
		case PAVLOVM:
			state.gui.setOvalPortrayal2DColor(a, (float)0.5, (float)0.7, (float)0.1, (float)1);
			break;
		case PAVLOVS:
			state.gui.setOvalPortrayal2DColor(a, (float)0.7, (float)0.6, (float)0.4, (float)1);
			break;
			/*
			 * you will need to add case statements to color the different strategies.
			 * [COMPLETE]
			 */
		default:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)1, (float)1, (float)1);
			break;
		}
	}

	public void placeAgent(Environment state) {
		if(!state.groups) {
			int tempx = state.sparseSpace.stx(x + xdir);
			int tempy = state.sparseSpace.sty(y + ydir);
			Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
			if(b == null || b.numObjs == 0){
				x = tempx;
				y = tempy;
				state.sparseSpace.setObjectLocation(this, x, y);
			}
		}
		else {
			x = state.sparseSpace.stx(x + xdir);
			y = state.sparseSpace.sty(y + ydir);
			state.sparseSpace.setObjectLocation(this, x, y);
		}
	}
	/**
	 * Searches locally in it search radius for agents and returns a bag of them. If none, it returns null
	 * @param state
	 * @return
	 */
	public Bag search(Environment state) {
		Bag agents = null;
		if(state.groups) {//For Aktipis model
			Bag temp = state.sparseSpace.getObjectsAtLocation(x,y);
			if(temp == null) {
				return null;
			}
			agents = new Bag(temp.numObjs);//size of temp
			agents.addAll(temp); //have to make a copy
			agents.remove(this);
			return agents;
		}
		else {
			agents = state.sparseSpace.getMooreNeighbors(x, y, state.searchOpponent, state.sparseSpace.TOROIDAL,false);
			return agents;
		}
	}

	public void die(Environment state) {
		state.sparseSpace.remove(this);
		event.stop();
	}

	public boolean carryingCapacityExceeded(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		if(agents.numObjs<=state.carryingCapacity) {
			return false;
		}
		else {
			return true;
		}
	}

	public void step(SimState state) {
		Environment eState = (Environment)state;
		if(age > maxAge) {
			die(eState);
			return;
		}
		if(resources >= eState.resoucesToReproduce) {
			replicate(eState);
		}
		age++; //aging
		if(played) {
			return;
			} //if played
		play(eState);
		
	}

}
