package uow.hsckWE.WGZ;

import uow.hsckWE.WGZ.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FightActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fight);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_fight, menu);
		return true;
	}

}
