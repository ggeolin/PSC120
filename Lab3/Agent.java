package khModel;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;

public class Agent implements Steppable {
	int x;//x,y coordinates
	int y;
	int dirx = 0;//the direction of movement
	int diry = 0;
	//KH model
	public boolean female;//determines whether an agent is a female == true, male == false
	public double attractiveness;//Attractiveness of this agent
	public double dates = 1;//starts with 0 and incremented by 1 with each date.
	public boolean dated = false;//flag for dating on each round
	public Stoppable event;//allows to remove an agent from the simulation.


	
    public Agent(boolean female, double attractiveness) {
		super();
		this.female = female;
		this.attractiveness = attractiveness;
	}

	public Agent(int x, int y, boolean female, double attractiveness) {
		super();
		this.x = x;
		this.y = y;
		this.female = female;
		this.attractiveness = attractiveness;
	}
	
	public void replicate (Environment state, boolean gender){
		int x = state.random.nextInt(state.gridWidth);
		int y = state.random.nextInt(state.gridHeight);
		double attractiveness = state.random.nextInt((int)state.maxAttractiveness)+1;
		Agent new_agent = new Agent(x, y, gender,attractiveness);
		new_agent.event = state.schedule.scheduleRepeating(new_agent);
		state.sparseSpace.setObjectLocation(new_agent,x, y);
		
		if(gender) {
			state.gui.setOvalPortrayal2DColor(new_agent, (float)1, (float)0, (float)0, (float)(attractiveness/state.maxAttractiveness));
		} else {
			state.gui.setOvalPortrayal2DColor(new_agent, (float)0, (float)0, (float)1, (float)(attractiveness/state.maxAttractiveness));
		}
	}
     
	/**
	 * Finds a date for an agent of the opposite sex.
	 * @param state
	 * @return
	 */
	public Agent findDate(Environment state) {
		if(female ) {//agent gender
			if(state.male.numObjs==0)
				return null;//if empty return null
			return (Agent)state.male.objs[state.random.nextInt(state.male.numObjs)];
		}
		else {
			if(state.female.numObjs==0)
				return null;
			return (Agent)state.female.objs[state.random.nextInt(state.female.numObjs)];
		}
	}
	
	public Agent findLocalDate(Environment state) {
		Bag neighbors = state.sparseSpace.getMooreNeighbors(x, y, state.searchRadius, state.sparseSpace.TOROIDAL, false);
		
		for(int i = 0; i < neighbors.numObjs; i++) {
			Agent random_agent = (Agent)neighbors.objs[i];

			if(random_agent.female != this.female) {
				return random_agent;
			}
		}
		return null; //a place holder for when you will return an agent or null if none can be found
	}
	
	
	/**
	 * KH closing time rule
	 * @param state
	 * @param p
	 * @return
	 */
	public double ctRule(Environment state, double p) {
		return Math.pow(p, ct(state));
	}
	
	public double ct(Environment state) {
		if (state.maxDates-dates > 0)
			return (state.maxDates-dates)/(state.maxDates-1.0);
		else
			return 0.0;
	}
	/**
	 * Attractiveness rule
	 * @param state
	 * @param a
	 * @return
	 */
	public double p1(Environment state, Agent a) {
		return Math.pow(a.attractiveness, state.choosiness)/Math.pow(state.maxAttractiveness, state.choosiness);
	}
	
	public double p2(Environment state, Agent a) {
		return Math.pow(state.maxAttractiveness-Math.abs(this.attractiveness - a.attractiveness), state.choosiness)/
				Math.pow(state.maxAttractiveness, state.choosiness);
	}
	
	/**
	 * Mixed rule
	 * @param state
	 * @param a
	 * @return
	 */
	public double p3(Environment state, Agent a) {
		return (p1(state,a)+p2(state,a))/2.0;
	}
	
	public double p4(Environment state, Agent a) {
		return p1(state, a)*ct(state) + p2(state,a)*(1-ct(state));
	}
	
	public void remove(Environment state) {
		if(female) 
			state.female.remove(this);//remove from the population
		else
			state.male.remove(this);
		state.sparseSpace.remove(this);//remove it from space
		event.stop();//remove from the schedule
	}
	
	public void nextPopulationStep(Environment state) {
		dated = true; //set dated to true.
		if(female) {
			state.nextFemale.add(this);
			state.female.remove(this);
		}
		else {
			state.nextMale.add(this);
			state.male.remove(this);
		}
	}
	
	public void dateNonSpatial(Environment state, Agent a) {
		double p;
		double q;
		switch(state.rule) {
		case ATTRACTIVE:
			p = p1(state,a);
			q = a.p1(state, this);
			break;
		case SIMILAR:
			p = p2(state,a);
			q = a.p2(state, this);
			break;
		case MIXED:
			p = p3(state,a);
			q = a.p3(state, this);
			break;
		default:
			p = p1(state,a);
			q = a.p1(state, this);
			break;	
		}
		p = ctRule(state,p);
		q = ctRule(state,q);

		if(state.random.nextBoolean(p)&& state.random.nextBoolean(q)) {//couple decison
			if(female) {
				state.experimenter.getData(this, a);
			}
			else {
				state.experimenter.getData(a, this);
			}
			remove(state);
			a.remove(state);
		} //end if test
		else {
			this.nextPopulationStep(state);
			a.nextPopulationStep(state);
		}
		if(dates < state.maxDates) {
			dates++;
		}
		if(a.dates < state.maxDates) {
			a.dates++;
		}
		
	}
	
	public void dateSpatial(Environment state, Agent a) {
		double p;
		double q;
		switch(state.rule) {
		case ATTRACTIVE:
			p = p1(state,a);
			q = a.p1(state, this);
			break;
		case SIMILAR:
			p = p2(state,a);
			q = a.p2(state, this);
			break;
		case MIXED:
			p = p3(state,a);
			q = a.p3(state, this);
			break;
		case NEW_MIXED:
			p = p4(state,a);
			q = a.p4(state, this);
		default:
			p = p1(state,a);
			q = a.p1(state, this);
			break;	
		}
		p = ctRule(state,p);
		q = ctRule(state,q);
		if(state.random.nextBoolean(p)&& state.random.nextBoolean(q)) {//couple decison
			System.out.println("COUNT: " + state.mated_num + "\t" + "NEW PAIR MATCHED");
			state.sparseSpace.remove(this);
			state.sparseSpace.remove(a);
			
			a.dated = true;
			dated = true;
			
			event.stop();
			a.event.stop();
			
			if(state.replacement) {
				System.out.println("COUNT: " + state.mated_num + "\t" + "NEW PAIR GENERATED");
				replicate(state, false);
				replicate(state, true);
			}
			
			state.mated_num++;

		} else {
			if(dates < state.maxDates) {
				dates++;
			}
			if(a.dates < state.maxDates) {
				a.dates++;
			}
		}
	}
	
	/**
	 * Handles dates for non-spatial and spatial models
	 * @param state
	 */
	public void date(Environment state) {
		if(state.nonSpatialModel) {
			Agent a = findDate(state);
			if(a!= null) {
				dateNonSpatial(state, a);
			}
		}
		else {
			Agent a = findLocalDate(state);
			if(a!=null) {

				dateSpatial(state, a);
			} else {

			}
		}
	}

	public void placeAgent(Environment state) {
        if(state.oneCellPerAgent) {//only one agent per cell
             int tempx = state.sparseSpace.stx(x + dirx);//tempx and tempy location
             int tempy = state.sparseSpace.sty(y + diry);
             Bag b = state.sparseSpace.getObjectsAtLocation(tempx, tempy);
             if(b == null){//if empty, agent moves to new location
                   x = tempx;
                   y = tempy;
                   state.sparseSpace.setObjectLocation(this, x, y);
             }//otherwise it does not move.
        }
        else {               
             x = state.sparseSpace.stx(x + dirx);
             y = state.sparseSpace.sty(y + diry);
             state.sparseSpace.setObjectLocation(this, x, y);
        }
   }
	/**
	 * Agents move randomly to a new location for either one agent per cell or possibly
	 * multiple agents per cell.
	 * @param state
	 */
	public void move(Environment state) {
		if(!state.random.nextBoolean(state.active)) {
			return;
		}
		if(state.random.nextBoolean(state.p)) {
			dirx = state.random.nextInt(3)-1;
			diry = state.random.nextInt(3)-1;
		}
		placeAgent(state);
	}
	
	public int decideX(Environment state, Bag neighbors) {
		int posX =0, negX =0;
		for(int i=0;i<neighbors.numObjs;i++) {
			Agent a = (Agent)neighbors.objs[i];
			if(a.x > x) {
				posX++;
			}
			else if(a.x < x) {
				negX++;
			}
		}
		if(posX > negX) {
			return 1;
		}
		else if (negX > posX) {
			return -1;
		}
		else {
			return state.random.nextInt(3)-1;
		}
	}
	
	public int decideY(Environment state, Bag neighbors) {
		int posY =0, negY =0;
		for(int i=0;i<neighbors.numObjs;i++) {
			Agent a = (Agent)neighbors.objs[i];
			if(a.y > y) {
				posY++;
			}
			else if(a.y < y) {
				negY++;
			}
		}
		if(posY > negY) {
			return 1;
		}
		else if (negY > posY) {
			return -1;
		}
		else {
			return state.random.nextInt(3)-1;
		}
	}
	
	public void aggregate (Environment state) {
		Bag b = state.sparseSpace.getMooreNeighbors(x, y, state.searchRadius, state.sparseSpace.TOROIDAL, false);
		dirx = decideX(state,b);
		diry = decideY(state,b);
		placeAgent(state);
	}

	public void step(SimState state) {
		Environment environment = (Environment)state;
		if(!dated)
			date(environment);
		if(!dated) {
			if(environment.random.nextBoolean(environment.aggregate)) {
				aggregate (environment);
			} else {
				move(environment);
			}
		}
		
	}

}
