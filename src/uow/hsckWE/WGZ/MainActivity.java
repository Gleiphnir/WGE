package uow.hsckWE.WGZ;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

public class MainActivity extends Activity implements LocationListener {

    private LocationManager lManager;
    private EditText lat;
    private EditText lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = (EditText)findViewById(R.id.editText1);
        lon = (EditText)findViewById(R.id.editText2);
        
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);


        lManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0, 0, this);
        else
            Log.i("Test", "network provider unavailable");

        Location lastKnownLocation =
                lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (lastKnownLocation != null) {
            Log.i("Test", lastKnownLocation.getLatitude() + ", "
                    + lastKnownLocation.getLongitude());
        } else
            Log.i("Test", "null");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onLocationChanged(Location location) {
    	if (location != null) {
    		double lat = location.getLatitude();
    		double lng = location.getLongitude();
 //   		p = new GeoPoint((int) lat * 1000000, (int) lng * 1000000);
 //   		mc.animateTo(p);
    	}

    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}