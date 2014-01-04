package jp.fjk.stablematching.engine;

import java.util.ListIterator;

public class Proposer extends Person{
	protected ListIterator<Person> iterator;
	
	public Proposer(String symbol, String name) {
		super(symbol, name);
	}

	public Accepter nextAccepter() {
		if (!this.iterator.hasNext()) {
			return null;
		}
		return (Accepter) this.iterator.next();
	}

	public boolean proposeToNext() {
		Accepter next = nextAccepter();
		if (next == null) {
			throw new ArrayIndexOutOfBoundsException(
					"Cannot propose more woman.");
		}
		return proposeTo(next);
	}

	public boolean proposeTo(Accepter woman) {
		if (!woman.acceptProposeFrom(this)) {
			return false;
		}
		getEngagedWith(woman);
		return true;
	}

	@Override
	public void initialize() {
		super.initialize();
		if (this.preference == null) {
			throw new UnsupportedOperationException("Preference has not been set.");
		}
		this.iterator = this.preference.listIterator();
	}
}
