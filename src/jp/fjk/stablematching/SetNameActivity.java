package jp.fjk.stablematching;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SetNameActivity extends Activity implements OnClickListener {
	static final private int OK = 0;
	static final private int CANCEL = 1;
	String oldName;
	EditText textbox;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_name_activity);

		this.textbox = (EditText) findViewById(R.id.textbox);
		this.oldName = getIntent().getStringExtra("NAME");
		this.textbox.setText(oldName);
		
		Button button = (Button) findViewById(R.id.okButton);
		button.setText("OK");
		button.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		String newName = textbox.getText().toString();
		if (newName.equals(oldName) || newName.equals("")) {
			setResult(CANCEL, null);
		} else {
			Intent intent = new Intent();
			intent.putExtra("NAME", newName);
			setResult(OK, intent);
		}
		finish();
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
	
}
