package jp.fjk.stablematching;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class PeepPreferenceActivity extends Activity {
	final int rowId[] = {
			R.id.row0,  R.id.row1,  R.id.row2,  R.id.row3,
			R.id.row4,  R.id.row5,  R.id.row6,  R.id.row7,
			};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String pref = getIntent().getStringExtra("PREFERENCE");		
		setContentView(R.layout.result_activity);
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(pref);
		
		for (int i = 0; i < Parameters.MAX_MEMBER; i++) {
			TableRow row = (TableRow) findViewById(this.rowId[i]);
			row.setVisibility(View.GONE);
		}
		
		Button cancel = (Button) findViewById(R.id.cancelButton);
		cancel.setText("OK");
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}			
		});
	}
		
}
