package evolutionaryGames;


public enum Strategy {
	COOPERATOR 	(1.0),
	DEFECTOR 	(2.0),
	WALKAWAY 	(3.0),
	WALKAWAYD 	(4.0),
	TFTM		(5.0),
	TFTS		(6.0),
	PAVLOVM		(7.0),
	PAVLOVS		(8.0);
	
	private final double id;
	Strategy(double id){
		this.id =id;
	}
	
	public double id() {
		return id;
	}
}

