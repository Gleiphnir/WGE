package uow.hsckWE.WGZ;

import uow.hsckWE.WGZ.R;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;

public class MapActivity extends Activity implements LocationListener{
	//View viewMap = findViewById(R.id.viewMap);
	LinearLayout mapLayout;
	private LocationManager lmanager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		this.lmanager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		if(this.lmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			this.lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		
		
		MapView mapView = new MapView(MapActivity.this);
		Location location = this.lmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		mapView.convertLatLonToUTM(location.getLatitude(), location.getLongitude());
		mapLayout = (LinearLayout)findViewById(R.id.mapLayout);
		mapLayout.addView(mapView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	

}
