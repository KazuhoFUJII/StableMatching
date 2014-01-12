package jp.fjk.stablematching;

import jp.fjk.stablematching.engine.Accepter;
import jp.fjk.stablematching.engine.Namer;
import jp.fjk.stablematching.engine.Person;
import jp.fjk.stablematching.engine.Proposer;

public class Session {
	Proposer[] men;
	Accepter[] women;
	Person[] persons;
	int[] renew;
	int member;
	
	public Session(int member) {
		this.member = member;
		
		this.men = new Proposer[member];
		this.women = new Accepter[member];
		this.persons = new Person[member + Parameters.MAX_MEMBER];
		this.renew = new int[member + Parameters.MAX_MEMBER];
		
		Namer namer = new Namer();		
		for (int i = 0; i < member; i++) {
			char symbol = (char) ('A' + i);
			this.men[i] = new Proposer(String.valueOf(symbol), namer.getMaleName());
			this.persons[i] = men[i];
			this.renew[i] = 0;
		}
		for (int i = 0; i < member; i++) {
			char symbol = (char) ('a' + i);
			this.women[i] = new Accepter(String.valueOf(symbol),
					namer.getFemaleName());
			this.persons[i + Parameters.MAX_MEMBER] = women[i];
			this.renew[i + Parameters.MAX_MEMBER] = 0;
		}
	}
}
