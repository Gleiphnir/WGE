package uow.hsckWE.WGZ;

import uow.hsckWE.WGZ.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;

public class MapActivity extends Activity {
	//View viewMap = findViewById(R.id.viewMap);
	LinearLayout mapLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		MapView mapView = new MapView(MapActivity.this);
		mapLayout = (LinearLayout)findViewById(R.id.mapLayout);
		mapLayout.addView(mapView);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}
	
	

}
