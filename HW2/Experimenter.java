package freezing_aggregation;

import observer.Observer;
import sim.engine.SimState;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {
	
	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper, String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);
	}
	
	public void numberOfFrozenAgents(Enviroment state) {
        Bag agents = state.sparseSpace.getAllObjects();
        int n = 0;//variable for counting frozen agents
        for(int i=0; i< agents.numObjs; i++) {
            Agent a = (Agent)agents.objs[i];
            if(a.frozen) {
                n++;//another frozen agent
             }
        }
        
       double time = (double)state.schedule.getTime();//get the current time
       this.upDateTimeChart(0,time, n, true, 1000);//update the chart with up to a 1000 milisecond delay
	}

   public void step(SimState state) {
	   super.step(state);
       if(step %this.state.dataSamplingInterval == 0) {//If a sampling interval, record data
           numberOfFrozenAgents((Enviroment) state);
       }
   }
}
