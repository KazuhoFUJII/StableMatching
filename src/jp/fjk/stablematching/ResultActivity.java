package jp.fjk.stablematching;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class ResultActivity extends Activity implements OnClickListener {
	int member;
	final int rowId[] = {
			R.id.row0,  R.id.row1,  R.id.row2,  R.id.row3,
			R.id.row4,  R.id.row5,  R.id.row6,  R.id.row7,
			};
	final int textViewSymbolId[] = {
			R.id.symbol0,  R.id.symbol1,  R.id.symbol2,  R.id.symbol3, 
			R.id.symbol4,  R.id.symbol5,  R.id.symbol6,  R.id.symbol7,
			};
	final int textViewNameId[] = {
			R.id.name0,  R.id.name1,  R.id.name2,  R.id.name3, 
			R.id.name4,  R.id.name5,  R.id.name6,  R.id.name7,
			};
	final int textViewFianceId[] = {
			R.id.fiance0,  R.id.fiance1,  R.id.fiance2,  R.id.fiance3, 
			R.id.fiance4,  R.id.fiance5,  R.id.fiance6,  R.id.fiance7,
			};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_activity);
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("The stable matching is below:");
		
		Bundle extras = getIntent().getExtras();
		String[] names = extras.getStringArray("NAMES");
		String[] results = extras.getStringArray("RESULTS");
		this.member = names.length;
		for (int i = 0; i < member; i++) {
			tv = (TextView) findViewById(this.textViewNameId[i]);
			tv.setText(names[i]);
			tv = (TextView) findViewById(this.textViewFianceId[i]);
			tv.setText(results[i]);
			tv = (TextView) findViewById(this.textViewSymbolId[i]);
			tv.setText(" - ");
		}
		for (int i = member; i < Parameters.MAX_MEMBER; i++) {
			TableRow row = (TableRow) findViewById(this.rowId[i]);
			row.setVisibility(View.GONE);
		}
		
		Button cancel = (Button) findViewById(R.id.cancelButton);
		cancel.setText("OK");
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return false;
	}
}
