package evolutionaryGames;

import observer.Observer;
import sim.engine.SimState;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {
	public int cooperators = 0;
	public int defectors = 0;
	public int walkaways = 0;
	public int walkawaysD = 0;
	public int tftm = 0; //These variables have been added for the lab
	public int tfts = 0;
	public int pavlovm = 0;
	public int pavlovs = 0;

	
	public void stop(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		if(agents == null || agents.numObjs == 0) {
			event.stop();
		}
	}
	

	public void countStrategies(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		for(int i=0;i<agents.numObjs;i++) {
			Agent a =(Agent)agents.objs[i];
			switch(a.strategy) {
			case COOPERATOR:
				cooperators++;
				break;
			case DEFECTOR:
				defectors++;
				break;
			case WALKAWAY:
				walkaways++;
				break;
			case WALKAWAYD:
				walkawaysD++;
				break;
			case TFTM:
				tftm++;
				break;
			case TFTS:
				tfts++;
				break;
			case PAVLOVM:
				pavlovm++;
				break;
			case PAVLOVS:
				pavlovs++;
				break;
			/*
			 * You need to add cases for four new strategies added for this lab
			 * [COMPLETE]
			 */
			}
		}
	}

	public boolean reset(SimState state) {
		super.reset();
		cooperators =0;
		defectors = 0;
		walkaways = 0;
		walkawaysD = 0;
		tftm = 0;
		tfts = 0;
		pavlovm = 0;
		pavlovs = 0;
		/*
		 * You need to reset the new strategy variables we added to 0
		 * [COMPLETE]
		 */
		return true;
	}
	
	/**
	 * This method collects data for automated simulation sweeps.  Behind the scenes, data are stored in arrays
	 * that allow the calculations of means and standard deviations between simulation runs.
	 * @return
	 */
	
	public boolean nextInterval() {
		double total = cooperators+defectors+walkaways+walkawaysD+ tftm+tfts+pavlovm+pavlovs;
		data.add(total);
		data.add(cooperators/total);
		data.add(defectors/total);
		data.add(walkaways/total);
		data.add(walkawaysD/total);
		data.add(tftm/total);
		data.add(tfts/total);
		data.add(pavlovm/total);
		data.add(pavlovs/total);
		/*
		 * You need to add to the data the four strategy variables that we added for this lab, just like the previous four
		 * [COMPLETE]
		 */
		return false;
	}

	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
			String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);

	}
	
	public void upDatePopulation(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		for(int i=0;i<agents.numObjs;i++) {
			Agent a = (Agent)agents.objs[i];
			a.played=false;
		}
	}
	
	public void strategyDistribution(Environment state) {
		//TODO
	}

	public void step(SimState state) {
		super.step(state);
		upDatePopulation((Environment) state);
		if(/*((Environment)state).charts &&*/ getdata) {//uncomment the first condition for the homework
			strategyDistribution((Environment) state);
		}
		if(((Environment)state).paramSweeps && getdata) {//for parameter sweeps
			reset(state);
			countStrategies((Environment) state);
			nextInterval();
		}
	}
}
