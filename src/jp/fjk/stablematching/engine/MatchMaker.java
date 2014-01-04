package jp.fjk.stablematching.engine;

import java.util.List;

public class MatchMaker {
	private static final int MAX_SESSION = 100;

	static public void arrange(Proposer[] proposer, Accepter[] accepter) {
		checkMembers(proposer, accepter);
		for (Proposer p : proposer) {
			p.initialize();
		}
		for (Accepter a : accepter) {
			a.initialize();
		}

		int session;
		for (session = 0; session < MAX_SESSION; session++) {
			boolean complete = true;
			for (Proposer p : proposer) {
				if (p.isEngaged())
					continue;
				p.proposeToNext();
				complete = false;
			}
			if (complete) {
				break;
			}
		}
		if (session >= MAX_SESSION) {
			throw new UnsupportedOperationException(
					"Can not get stable matching. Iteration does not stop.");
		}
		// for test
		checkCouples(proposer, accepter);
		checkStable(proposer, accepter);
	}

	private static void checkMembers(Proposer[] proposer, Accepter[] accepter) {
		if (proposer.length != accepter.length) {
			throw new IllegalArgumentException(
					"Nambers of proposer and accepter is different.");
		}
		Person[][] args = { proposer, accepter };
		for (Person[] ps : args) {
			for (Person m : ps) {
				List<Person> p = m.getPreference();
				if (p == null) {
					throw new IllegalArgumentException("Person " + m.toString()
							+ "'s preference is null");
				}
				if (p.size() < accepter.length) {
					throw new IllegalArgumentException("Person " + m.toString()
							+ "'s preference has too few members.");
				}
			}
		}
	}

	private static void checkCouples(Proposer[] proposer, Accepter[] accepter) {
		for (Proposer p : proposer) {
			Accepter fiancee = (Accepter) p.getFiance();
			if (fiancee == null) {
				throw new UnsupportedOperationException(
						"Can not get stable matching. " + p.toString()
								+ "'s fiancee is null.");
			}
			Proposer myself = (Proposer) fiancee.getFiance();
			if (myself == null || !myself.equals(p)) {
				throw new UnsupportedOperationException(
						"Can not get stable matching. "
								+ fiancee.toString() + "'s fiance is illegal.");
			}
		}
	}

	private static void checkStable(Proposer[] proposer, Accepter[] accepter) {
		for (Proposer m : proposer) {
			Accepter fiancee = (Accepter) m.getFiance();
			int rankOfHer = m.getRankOf(fiancee);
			List<Person> unattainables = m.getPreference().subList(0, rankOfHer);
			for (Person u: unattainables) {
				Proposer fiance = (Proposer) u.getFiance();
				int rankOfHim = u.getRankOf(fiance);
				List<Person> princes = m.getPreference().subList(0, rankOfHim);
				for (Person p: princes) {
					if (p.equals(m)) {
						throw new UnsupportedOperationException(
								"Can not get stable matching. "
										+ m.toString() + " and "
										+ u.toString()
										+ " prefere each other truely.");
					}
				}
			}
		}
	}
}
