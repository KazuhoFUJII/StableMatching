package jp.fjk.stablematching.engine;

import java.util.List;

public class Person {
	protected String symbol;
	protected String name;
	protected Person fiance = null;
	protected List<Person> preference = null;
	
	public Person(String symbol, String name) throws IllegalArgumentException {
		if (symbol == null) {
			throw new IllegalArgumentException("Argument symbol is null");
		}
		if (name == null) {
			throw new IllegalArgumentException("Argument name is null");
		}
		this.symbol = symbol;
		this.name = name;
	}

	@Override
	public String toString() {
		return this.symbol + ". " + this.name;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEngaged() {
		return this.fiance != null;
	}

	public Person getFiance() {
		return this.fiance;
	}

	public void getEngagedWith(Person person) {
		this.fiance = person;
	}

	public void becomeFree() {
		this.fiance = null;
	}

	public void setPreference(List<Person> preference) {
		if (preference == null) {
			this.preference = preference;
			return;
		}
		if (preference.contains(null)) {
			throw new IllegalArgumentException(
					"Argument preference contains null.");
		}
		for (Person p: preference) {
			if (preference.indexOf(p) != preference.lastIndexOf(p)) {
				throw new IllegalArgumentException(
						"Argument preference contains same mumber.");
			}
		}
		this.preference = preference;
	}
	
	public List<Person> getPreference() {
		return this.preference;
	}

	/**
	 * return true if prefer person1 to person2
	 * 
	 * @param person1
	 * @param person2
	 * @return
	 */
	public boolean whichPrefersMore(Person p1, Person p2) {
		return getRankOf(p1) < getRankOf(p2);
	}

	public int getRankOf(Person p) {
		int rank = this.preference.indexOf(p);
		if (rank >= 0) {
			return rank;
		}
		throw new IllegalArgumentException(
				"Argument " + p.toString() + " is unknown.");
	}

	public void initialize() {
		becomeFree();
	}
}
