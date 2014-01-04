package jp.fjk.stablematching;

import java.util.ArrayList;
import java.util.List;

import jp.fjk.stablematching.engine.Proposer;
import jp.fjk.stablematching.engine.MatchMaker;
import jp.fjk.stablematching.engine.Namer;
import jp.fjk.stablematching.engine.Person;
import jp.fjk.stablematching.engine.Accepter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Spannable;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	static final int CANCEL = 1;
	Proposer[] men = new Proposer[Parameters.MAX_MEMBER];
	Accepter[] women = new Accepter[Parameters.MAX_MEMBER];
	Person[] persons = new Person[Parameters.MAX_MEMBER * 2];
	int[] renew = new int[Parameters.MAX_MEMBER * 2];
	
	private Button arrange;
	
	private final int rowId[] = {
			R.id.row0,  R.id.row1,  R.id.row2,  R.id.row3,
			R.id.row4,  R.id.row5,  R.id.row6,  R.id.row7,
			R.id.row8,  R.id.row9,  R.id.row10, R.id.row11,
			R.id.row12, R.id.row13, R.id.row14, R.id.row15,
			};
	private final int textViewSymbolId[] = {
			R.id.symbol0,  R.id.symbol1,  R.id.symbol2,  R.id.symbol3, 
			R.id.symbol4,  R.id.symbol5,  R.id.symbol6,  R.id.symbol7,
			R.id.symbol8,  R.id.symbol9,  R.id.symbol10, R.id.symbol11,
			R.id.symbol12, R.id.symbol13, R.id.symbol14, R.id.symbol15,
			};
	private final int textViewNameId[] = {
			R.id.name0,  R.id.name1,  R.id.name2,  R.id.name3, 
			R.id.name4,  R.id.name5,  R.id.name6,  R.id.name7,
			R.id.name8,  R.id.name9,  R.id.name10, R.id.name11,
			R.id.name12, R.id.name13, R.id.name14, R.id.name15,
			};
	private final int buttonId[] = {
			R.id.button0,  R.id.button1,  R.id.button2,  R.id.button3,
			R.id.button4,  R.id.button5,  R.id.button6,  R.id.button7,
			R.id.button8,  R.id.button9,  R.id.button10, R.id.button11,
			R.id.button12, R.id.button13, R.id.button14, R.id.button15,
			}; 
	int member = 4;
	
	public MainActivity() {
		super();
		Namer namer = new Namer();
		for (int i = 0; i < men.length; i++) {
			char symbol = (char) ('A' + i);
			men[i] = new Proposer(String.valueOf(symbol), namer.getMaleName());
			persons[i] = men[i];
		}
		for (int i = 0; i < women.length; i++) {
			char symbol = (char) ('a' + i);
			women[i] = new Accepter(String.valueOf(symbol), namer.getFemaleName());
			persons[i + Parameters.MAX_MEMBER] = women[i];
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Enter each preference.");
		        		
		this.arrange = (Button) findViewById(R.id.arrangeButton);
		this.arrange.setText("Arrange");
		this.arrange.setOnClickListener(this);
		
		setTable();
	}
	
	private void setTable() {
    	for (int i = 0; i < this.member; i++) {
    		TableRow row = (TableRow) findViewById(this.rowId[i]);
    		row.setVisibility(View.VISIBLE);
    		row = (TableRow) findViewById(this.rowId[i + Parameters.MAX_MEMBER]);
    		row.setVisibility(View.VISIBLE);
    		setPreferenceButton(i);
    		setPreferenceButton(i + Parameters.MAX_MEMBER);
    		this.renew[i] = 0;
    		this.renew[i + Parameters.MAX_MEMBER] = 0;
    		setNameText(i);
    		setNameText(i + Parameters.MAX_MEMBER);
    	}
    	for (int i = this.member; i < Parameters.MAX_MEMBER; i++) {
    		TableRow row = (TableRow) findViewById(this.rowId[i]);
    		row.setVisibility(View.GONE);
    		row = (TableRow) findViewById(this.rowId[i + Parameters.MAX_MEMBER]);
    		row.setVisibility(View.GONE);
    	}
    	initializePreference();
	}
	
	private void initializePreference() {
		for (Proposer m: men) {			
			m.setPreference(null);
		}
		for (Accepter w: women) {
			w.setPreference(null);
		}
		this.arrange.setEnabled(false);
	}
	
	private void setPreferenceButton(int i) {
		Button button = (Button) findViewById(this.buttonId[i]);
		button.setText("Preference");
	    button.setOnClickListener(this);
	}
	
	private void setNameText(int i) {
		// symbol
		TextView tv = (TextView) findViewById(this.textViewSymbolId[i]);
		tv.setText(this.persons[i].getSymbol() + ". ");
		// name : underline
		tv = (TextView) findViewById(this.textViewNameId[i]);
		String name = this.persons[i].getName();
		name = this.persons[i].getName();
		for (int j = 0; j < this.renew[i] % 4; j++) {
			name = name.concat("*");
		}
		Spannable t = Spannable.Factory.getInstance().newSpannable(name);
		UnderlineSpan us = new UnderlineSpan();
		t.setSpan(us, 0, name.length(), t.getSpanFlags(us));
		tv.setText(t, TextView.BufferType.SPANNABLE);
		tv.setOnClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) { 
		super.onActivityResult(requestCode, resultCode, intent);	
		if (resultCode == CANCEL) {
			return;				
		}
		// set preference activity
		if (requestCode < Parameters.MAX_MEMBER * 2) {
			Person[] vsGroup;
			int memberId = requestCode;
			if (requestCode < Parameters.MAX_MEMBER) {
				vsGroup = women;
			} else {
				vsGroup = men;
			}
			int[] ranking = intent.getIntArrayExtra("RANKING");
			List<Person> preference = new ArrayList<Person>();
			for (int i: ranking) {
				preference.add(vsGroup[i]);
			}
			persons[memberId].setPreference(preference); 
			{
				String str = new String("");
				for (int i: ranking) {
					str = str + String.valueOf(i);
				}
				Log.d("", persons[memberId].toString() + "'s ranking: " + str);
			}
			this.renew[memberId]++;
			setNameText(memberId);
			
			if (isPreferencesCompleted()) {
				this.arrange.setEnabled(true);
			}
			return;
		}
		// set name activity
		if (requestCode >= Parameters.MAX_MEMBER * 2) {
			int memberId = requestCode - Parameters.MAX_MEMBER * 2;
			String name = intent.getStringExtra("NAME");
			this.persons[memberId].setName(name);
			setNameText(memberId);
			return;
		}
	}
	
	boolean isPreferencesCompleted() {
		for (int i = 0; i < this.member; i++) {
			if (this.persons[i].getPreference() == null) {
				return false;
			}
		}
		return true;
	}
	
	public void onClick(View v) {
		int id = v.getId();
		// Arrange Button
      	if (id == R.id.arrangeButton) {
      		for (int i = 0; i < member; i++) {
      			if(this.men[i].getPreference() == null ||
      			   this.women[i].getPreference() == null) {
      				return;
      			}
      		}
			Proposer[] men   = (Proposer[]) pertialArray(this.men, member);
			Accepter[] women = (Accepter[]) pertialArray(this.women, member);

			MatchMaker.arrange(men, women);		
			String[] names = new String[member];
			String[] results = new String[member];
			for (int i = 0; i < member; i++) {
				names[i] = men[i].toString();
				results[i] = men[i].getFiance().toString();
			}			
			Intent intent = new Intent(this, ResultActivity.class);
			intent.putExtra("NAMES", names);
			intent.putExtra("RESULTS", results);
			startActivity(intent);
			return; 
		}
      	
    	// Set Preference Button 	
        for (int i = 0; i < Parameters.MAX_MEMBER; i++) {
        	if (id == buttonId[i]) {
        		setPreferenceActivity(i, men[i], women);
        		return;
        	}
        	if (id == buttonId[i + Parameters.MAX_MEMBER]) {
        		setPreferenceActivity(i + Parameters.MAX_MEMBER, women[i], men);
            	return;
        	}     	

        }
    	// Name Text
        for (int i = 0; i < 2 * Parameters.MAX_MEMBER; i++) {
        	if (id == textViewNameId[i]) {
        		Intent intent = new Intent(this, SetNameActivity.class);
        		intent.putExtra("NAME", persons[i].getName());
        		startActivityForResult(intent, i + 2 * Parameters.MAX_MEMBER);
        	}
        }    	
	}
	
	private void setPreferenceActivity(int j, Person p, Person[] vsGroup) {
    	Intent intent = new Intent(this, SetPreferenceActivity.class);
    	intent.putExtra("PERSON", p.toString());
    	String[] names = new String[this.member];
    	for (int i = 0; i < this.member; i++) {
    		names[i] = vsGroup[i].toString();
    	}
    	intent.putExtra("NAMES", names);
    	startActivityForResult(intent, j);
	}
	
	//---------------------------------------------------------------------------
	private final int MENU_NAM_PEOPLE = 0;
	// menu id 2 ~ 8 is used
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu = menu.addSubMenu(
				Menu.NONE, MENU_NAM_PEOPLE, Menu.NONE, "change namber of people");
		for (int i = 2; i <= 8; i++) {
			String title = String.valueOf(i) + " - " + String.valueOf(i);
			subMenu.add(Menu.NONE, i, Menu.NONE, title);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (item.getItemId() >= 2 && item.getItemId() <= 8) {
			this.member = id;
			setTable();
			return true;
		}
		return false;
	}
	
	private static Person[] pertialArray(Person[] original, int newLength) {
		if (original.length < newLength) {
			throw new IllegalArgumentException("New length is larger than original one.");
		}
		Person[] pertial;
		if (original instanceof Proposer[]) {
			pertial = new Proposer[newLength];
		} else if (original instanceof Accepter[]) {
			pertial = new Accepter[newLength];
		} else {
			pertial = new Person[newLength];
		}
		for (int i = 0; i < newLength; i++) {
			pertial[i] = original[i];
		}
		return pertial;
	}
}
