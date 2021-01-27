package freezing_aggregation;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;

public class Agent implements Steppable {
	int x; //x location in space
	int y;//y location in space
	int xdir;//x direction of movement
	int ydir;//ydirection of movement
	public boolean frozen;//status of the agent
	
	public Agent(int x, int y, int xdir, int ydir, boolean frozen) {
		super();
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		this.frozen = frozen;
	}
	
	public void aggregate(Enviroment state) {
		int new_x_coor = -1;
		int new_y_coor = -1;
		
		if(this.frozen) {
			return;
		}
		
		if(state.isBroadRule()) {
			// BroadRule:
			Bag neighbor = null;
			
			// move method :
			if(state.random.nextBoolean(state.p)) {
				xdir = state.random.nextInt(3)-1;
				ydir = state.random.nextInt(3)-1;
			}
			
			// if TOROIDAL : 
			if(!state.bounded) {
				neighbor = state.sparseSpace.getMooreNeighbors(x, y, 1, SparseGrid2D.TOROIDAL, false);
				new_x_coor = state.sparseSpace.stx(this.x + xdir);
				new_y_coor = state.sparseSpace.sty(this.y + ydir);
			} else {
				neighbor = state.sparseSpace.getMooreNeighbors(x, y, 1, SparseGrid2D.BOUNDED, false);
				if(this.x + xdir < 0 || this.x + xdir > state.gridWidth - 1) {
					new_x_coor = -1 * xdir + this.x; 
				} else {
					new_x_coor = this.x + xdir;
				}
				
				if(this.y + ydir < 0 || this.y + ydir > state.gridHeight - 1) {
					new_y_coor = -1 * ydir + this.y;
				} else {
					new_y_coor = this.y + ydir;
				}
			}
			
			// try to see if any of the neighbor is frozen
			for(int i = 0; i < neighbor.numObjs; i++) {
				Agent a = (Agent)neighbor.objs[i];
				if(a.frozen) {
					this.frozen = true;
					return;
				}
			}
			
			this.x = new_x_coor;
			this.y = new_y_coor;
			
			state.sparseSpace.setObjectLocation(this, x,y);
		} else {
			// NarrowRule :	
			if(state.random.nextBoolean(state.p)) {
				xdir = state.random.nextInt(3)-1;
				ydir = state.random.nextInt(3)-1;
			}
			
			// first to generate a new direction and have a new coordinate
			// if TOROIDAL : 
			if(!state.bounded) {
				new_x_coor = state.sparseSpace.stx(this.x + xdir);
				new_y_coor = state.sparseSpace.sty(this.y + ydir);
			} else {
				if(this.x + xdir < 0 || this.x + xdir > state.gridWidth - 1) {
					new_x_coor = -1 * xdir + this.x; 
				} else {
					new_x_coor = this.x + xdir;
				}
				
				if(this.y + ydir < 0 || this.y + ydir > state.gridHeight - 1) {
					new_y_coor = -1 * ydir + this.y;
				} else {
					new_y_coor = this.y + ydir;
				}
			}
			
			Bag neighbor = state.sparseSpace.getObjectsAtLocation(new_x_coor, new_y_coor);
			Agent a;
			
			if(neighbor != null && neighbor.numObjs != 0) {
				a = (Agent)neighbor.objs[0];
				if(a.frozen) {
					this.frozen = true;
					return;
				}
			}
			
			this.x = new_x_coor;
			this.y = new_y_coor;
			state.sparseSpace.setObjectLocation(this, x,y);
		}
	}
	
	@Override
	public void step(SimState state) {
		Enviroment eState = (Enviroment)state;
		aggregate(eState);
	}

}
