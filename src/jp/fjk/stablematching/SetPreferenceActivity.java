package jp.fjk.stablematching;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Bundle;

public class SetPreferenceActivity extends Activity
		implements OnClickListener, OnItemSelectedListener {
	static final int OK = 0;
	static final int CANCEL = 1;
	Spinner[] spinner = new Spinner[Parameters.MAX_MEMBER];
	Button okButton;
	int member;
	Random rand = new Random(System.currentTimeMillis());
	
	final int rowId[] = {
			R.id.row0, R.id.row1, R.id.row2, R.id.row3,
			R.id.row4, R.id.row5, R.id.row6, R.id.row7,
			};
	final int textViewId[] = {
			R.id.textView0, R.id.textView1, R.id.textView2, R.id.textView3, 
			R.id.textView4, R.id.textView5, R.id.textView6, R.id.textView7,
			};
	final int spinnerId[] = {
			R.id.spinner0, R.id.spinner1, R.id.spinner2, R.id.spinner3, 
			R.id.spinner4, R.id.spinner5, R.id.spinner6, R.id.spinner7,
			};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_preference_activity);
		Bundle extras = getIntent().getExtras();
		
		TextView tv = (TextView) findViewById(R.id.message);
		tv.setText(extras.getString("PERSON") + "'s preference");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("");
		String[] names = extras.getStringArray("NAMES");
		this.member = names.length;
		for (String s: names) {
			adapter.add(s);
		}
		for (int i = 0; i < this.member; i++) {
			tv = (TextView) findViewById(textViewId[i]);
			tv.setText(String.valueOf(i+1) + ". ");
			spinner[i] = (Spinner) findViewById(spinnerId[i]);
			spinner[i].setAdapter(adapter);
			spinner[i].setOnItemSelectedListener(this);
		}
		for (int i = this.member; i < Parameters.MAX_MEMBER; i++) {
			TableRow row = (TableRow) findViewById(rowId[i]);
			row.setVisibility(View.GONE);
		}

		Button cancel = (Button) findViewById(R.id.cancelButton);
		cancel.setText("Cancel");
		cancel.setOnClickListener(this);
		this.okButton = (Button) findViewById(R.id.okButton);
		okButton.setText("OK");
		okButton.setEnabled(false);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.cancelButton: {
			setResult(CANCEL, null);
			finish();
			break; } 
		case R.id.okButton: {
			int[] ranking = new int[this.member];
			for (int i = 0; i < this.member; i++) {
				int pos = spinner[i].getSelectedItemPosition();
				if (pos == 0) {
					return; 
				}
				ranking[i] = pos - 1;
			}
			Intent intent = new Intent();
			intent.putExtra("RANKING", ranking);
			setResult(OK, intent);
			finish();
			break; } 
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(CANCEL, null);
			finish();
			return true;
		}
		return false;
	}

	@Override
	public void onItemSelected(
			AdapterView<?> parent, View view, 
			int position, long id) {
		//if select an item already selected, rob the item.
		if (position == 0) {
			this.okButton.setEnabled(false);
			return;
		}
		for (int i = 0; i < this.member; i++) {
			if (parent.getId() != spinnerId[i] && 
				position == spinner[i].getSelectedItemPosition()) {
				spinner[i].setSelection(0, true);
				break;
			}
		}
		//if only one spinner is not selected, fill it.
		/* behavior is unnatural, so kill it.
		int empty = -1;
		int sumNow = 0;
		int sumComp = 0; // sum of position number when each spinner is selected correctly.
		for (int i = 0; i < member; i++) {
			int pos = spinner[i].getSelectedItemPosition();
			if (pos == 0 && empty != -1) return; // two spinner is not selected.
			if (pos == 0) empty = i;
			sumNow += pos;
			sumComp += i + 1;
		}
		if (empty != -1) {
			spinner[empty].setSelection(sumComp - sumNow, true);				
		} */
		
		// if all spinner are filled, enable ok button.
		for (int i = 0; i < this.member; i++) {
			if (this.spinner[i].getSelectedItemPosition() == 0) {
				return;
			}
		}
		this.okButton.setEnabled(true);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		return;
	}
	
	//------------------------------------------------------------------------------------
	private final int MENU_RANDOM = 864087143;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_RANDOM, Menu.NONE, "fill at random");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_RANDOM:			
			for (int i = 0; i < this.member; i++) {
				if (this.spinner[i].getSelectedItemPosition() == 0) {
					selectRandom(this.spinner[i]);
				}
			}
			this.okButton.setEnabled(true);
			return true;
		}
		return false;
	}
	
	private int selectRandom(Spinner spinner) {
		int pos;
		for (;;) {
			pos = rand.nextInt(this.member) + 1;
			if (!isSelected(pos)) break;
		}
		spinner.setSelection(pos, true);
		return pos;
	}
	
	private boolean isSelected(int position) {
		for (int i = 0; i < this.member; i++) {
			if (this.spinner[i].getSelectedItemPosition() == position) {
				return true;
			}
		}
		return false;
	}
}
