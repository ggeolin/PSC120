package freezing_aggregation;

import java.awt.Color;

import spaces.Spaces;
import sweep.GUIStateSweep;
import sweep.SimStateSweep;

public class GUIfreezing extends GUIStateSweep {
	
	public GUIfreezing(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor, boolean agentPortrayal) {
		super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
	}
	
	public static void main(String[] args) {
		String[] title = {"Agents Frozen"};
		String[] x = {"Time Steps"};
		String[] y = {"Number"};
		
		GUIfreezing.initializeArrayTimeSeriesChart(1, title, x, y);
		
		GUIfreezing.initialize(Enviroment.class, Experimenter.class, GUIfreezing.class, 600, 600, Color.WHITE, Color.BLUE, true, Spaces.SPARSE);
	}
}
