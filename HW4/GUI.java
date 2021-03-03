package evolutionaryGames;

import java.awt.Color;

import sweep.GUIStateSweep;
import sweep.SimStateSweep;

public class GUI extends GUIStateSweep {



	public GUI(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal) {
		super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
	}

	public static void main(String[] args) {
		//TODO:  Add histogram intitialization code here
		String[] title = {"Strategy Distribution"};
		String[] x = {"Agent ID"};
		String[] y = {"Number"};
		GUI.initializeArrayHistogramChart(1, title, x, y, new int[10]);
		GUI.initialize(Environment.class, Experimenter.class, GUI.class, 400, 400, Color.WHITE, Color.RED, false, spaces.SPARSE);
	}

}
