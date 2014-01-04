package jp.fjk.stablematching.engine;

public class Accepter extends Person {
	public Accepter(String symbol, String name) {
		super(symbol, name);
	}

	public boolean acceptProposeFrom(Proposer man) {
		if (!isEngaged()) {
			getEngagedWith(man);
			return true;
		}
		Proposer fiance = (Proposer) getFiance();
		if (whichPrefersMore(man, fiance)) {
			fiance.becomeFree();
			getEngagedWith(man);
			return true;
		}
		return false;
	}
}
