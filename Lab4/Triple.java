package evolutionaryGames;
/**
 * A triple is a basic packet of memory, the first element is an opponents id, the second, an opponent's
 * last strategy, and the third is the agent's strategy used against that opponent.
 * @author jcschankadmin
 *
 */
class Triple {
	long id;
	Strategy opponentStrategy;
	Strategy myStrategy;
	public Triple(long id, Strategy opponentStrategy, Strategy myStrategy) {
		super();
		this.id = id;
		this.opponentStrategy = opponentStrategy;
		this.myStrategy = myStrategy;
	}
}