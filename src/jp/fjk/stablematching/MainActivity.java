package jp.fjk.stablematching;

import java.util.ArrayList;
import java.util.List;

import jp.fjk.stablematching.engine.MatchMaker;
import jp.fjk.stablematching.engine.Person;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Spannable;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	Session session;	
	Session previous;
			
	private int secret = 0;

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

	public MainActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Enter each preference.");
		tv.setOnClickListener(this);

		this.arrange = (Button) findViewById(R.id.arrangeButton);
		this.arrange.setText("Arrange");
		this.arrange.setOnClickListener(this);
		
		this.session = new Session(4);
		setTable();
	}

	private void setTable() {
		for (int i = 0; i < this.session.member; i++) {
			TableRow row = (TableRow) findViewById(this.rowId[i]);
			row.setVisibility(View.VISIBLE);
			row = (TableRow) findViewById(this.rowId[i + Parameters.MAX_MEMBER]);
			row.setVisibility(View.VISIBLE);
			setPreferenceButton(i);
			setPreferenceButton(i + Parameters.MAX_MEMBER);
			setNameText(i);
			setNameText(i + Parameters.MAX_MEMBER);
		}
		for (int i = this.session.member; i < Parameters.MAX_MEMBER; i++) {
			TableRow row = (TableRow) findViewById(this.rowId[i]);
			row.setVisibility(View.GONE);
			row = (TableRow) findViewById(this.rowId[i + Parameters.MAX_MEMBER]);
			row.setVisibility(View.GONE);
		}
		this.arrange.setEnabled(isPreferencesCompleted());
	}

	private void setPreferenceButton(int i) {
		Button button = (Button) findViewById(this.buttonId[i]);
		button.setText("Preference");
		button.setOnClickListener(this);
	}

	private void setNameText(int i) {
		// symbol
		TextView tv = (TextView) findViewById(this.textViewSymbolId[i]);
		tv.setText(this.session.persons[i].getSymbol() + ". ");
		// name : underline
		tv = (TextView) findViewById(this.textViewNameId[i]);
		String name = this.session.persons[i].getName();
		name = this.session.persons[i].getName();
		for (int j = 0; j < this.session.renew[i] % 4; j++) {
			name = name.concat("*");
		}
		Spannable t = Spannable.Factory.getInstance().newSpannable(name);
		UnderlineSpan us = new UnderlineSpan();
		t.setSpan(us, 0, name.length(), t.getSpanFlags(us));
		tv.setText(t, TextView.BufferType.SPANNABLE);
		tv.setOnClickListener(this);
	}

	static private final int CANCEL = 1;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		this.secret = 0;

		if (resultCode == CANCEL) {
			return;
		}
		// set preference activity
		if (requestCode < Parameters.MAX_MEMBER * 2) {
			Person[] vsGroup;
			int memberId = requestCode;
			if (requestCode < Parameters.MAX_MEMBER) {
				vsGroup = this.session.women;
			} else {
				vsGroup = this.session.men;
			}
			int[] ranking = intent.getIntArrayExtra("RANKING");
			List<Person> preference = new ArrayList<Person>();
			for (int i : ranking) {
				preference.add(vsGroup[i]);
			}
			this.session.persons[memberId].setPreference(preference);
			{
				String str = new String("");
				for (int i : ranking) {
					str = str + String.valueOf(i);
				}
			}
			this.session.renew[memberId]++;
			setTable();
			return;
		}
		// set name activity
		if (requestCode >= Parameters.MAX_MEMBER * 2) {
			int memberId = requestCode - Parameters.MAX_MEMBER * 2;
			String name = intent.getStringExtra("NAME");
			this.session.persons[memberId].setName(name);
			setNameText(memberId);
			return;
		}
	}

	boolean isPreferencesCompleted() {
		for (int i = 0; i < this.session.member; i++) {
			if (this.session.men[i].getPreference() == null ||
			    this.session.women[i].getPreference() == null) {
				return false;
			}
		}
		return true;
	}

	public void onClick(View v) {
		int id = v.getId();
		// ----------------------------------------------------
		// Arrange Button
		if (id == R.id.arrangeButton) {
			for (int i = 0; i < this.session.member; i++) {
				if (this.session.men[i].getPreference() == null
						|| this.session.women[i].getPreference() == null) {
					return;
				}
			}

			MatchMaker.arrange(this.session.men, this.session.women);
			String[] names = new String[this.session.member];
			String[] results = new String[this.session.member];
			for (int i = 0; i < this.session.member; i++) {
				names[i] = this.session.men[i].toString();
				results[i] = this.session.men[i].getFiance().toString();
			}
			Intent intent = new Intent(this, ResultActivity.class);
			intent.putExtra("NAMES", names);
			intent.putExtra("RESULTS", results);
			startActivity(intent);
			return;
		}
		// ----------------------------------------------------
		// Set Preference Button
		for (int i = 0; i < Parameters.MAX_MEMBER; i++) {
			if (id == buttonId[i]) {
				setPreferenceActivity(i, 
						this.session.men[i], this.session.women);
				return;
			}
			if (id == buttonId[i + Parameters.MAX_MEMBER]) {
				setPreferenceActivity(i + Parameters.MAX_MEMBER, 
						this.session.women[i], this.session.men);
				return;
			}
		}
		// ----------------------------------------------------
		// Peep at preference: secret command
		if (id == R.id.textView1)
			this.secret++;
		if (this.secret == 10 && id == R.id.name0) {
			this.secret = 0;
			String pref = new String("");
			Person[][] mw = { this.session.men, this.session.women };
			for (Person[] p : mw) {
				for (int i = 0; i < this.session.member; i++) {
					pref += p[i].toString() + ": ";
					List<Person> list = p[i].getPreference();
					if (list != null) {
						for (int j = 0; j < this.session.member; j++) {
							pref += list.get(j).getSymbol();
						}
					}
					pref += "\n";
				}
			}
			Intent intent = new Intent(this, PeepPreferenceActivity.class);
			intent.putExtra("PREFERENCE", pref);
			startActivity(intent);
			return;
		}

		// ----------------------------------------------------
		// Name Text
		for (int i = 0; i < 2 * Parameters.MAX_MEMBER; i++) {
			if (id == textViewNameId[i]) {
				Intent intent = new Intent(this, SetNameActivity.class);
				intent.putExtra("NAME", this.session.persons[i].getName());
				startActivityForResult(intent, i + 2 * Parameters.MAX_MEMBER);
				return;
			}
		}
	}

	private void setPreferenceActivity(int j, Person p, Person[] vsGroup) {
		Intent intent = new Intent(this, SetPreferenceActivity.class);
		intent.putExtra("PERSON", p.toString());
		String[] names = new String[this.session.member];
		for (int i = 0; i < this.session.member; i++) {
			names[i] = vsGroup[i].toString();
		}
		intent.putExtra("NAMES", names);
		startActivityForResult(intent, j);
	}

	// ---------------------------------------------------------------------------
	private final int MENU_PREV_SESSION = 924388375;
	private final int MENU_NEW_SESSION  = 599889334;
	// menu id 2 ~ 8 is used
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(Menu.NONE, MENU_PREV_SESSION, Menu.NONE, "Previous Session");
		SubMenu subMenu = menu.addSubMenu(
				Menu.NONE, MENU_NEW_SESSION, Menu.NONE, "New Session");
		for (int i = 2; i <= 8; i++) {
			String title = String.valueOf(i) + " - " + String.valueOf(i);
			subMenu.add(Menu.NONE, i, Menu.NONE, title);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		/* New Session */
		if (id >= 2 && id <= 8) {
			this.previous = this.session;
			this.session = new Session(id);
			setTable();
			return true;
		}
		
		/* Previous Session */
		if (id == MENU_PREV_SESSION) {
			if (this.previous != null) {
				Session tmp = this.previous;
				this.previous = this.session;
				this.session = tmp;		
				setTable();
				return true;
			}
		}
		return false;
	}
}
