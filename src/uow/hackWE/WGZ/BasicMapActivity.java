package uow.hackWE.WGZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class BasicMapActivity extends android.support.v4.app.FragmentActivity implements LocationListener{
   
	private GoogleMap mMap;
    private LocationManager lManager;
    private EditText coor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);
        
        lManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        this.coor = (EditText) findViewById(R.id.editText1);
        this.coor.setText(this.getLatLng().toString());
        
        setUpMapIfNeeded();
        this.mMap.setMyLocationEnabled(true);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLng()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(this.getLatLng()).title("You Started Here!"));
        final AssetManager am = getAssets();
        try {
        	ArrayList<Double> arrLat = new ArrayList<Double>();
        	ArrayList<Double> arrLon = new ArrayList<Double>();
        	ArrayList<String> Schools = new ArrayList<String>();
			Scanner sn = new Scanner(am.open("lat.txt"));
			Log.i("Loading", "Latitudes");
			while(sn.hasNextBigDecimal()){
				arrLat.add(sn.nextDouble());
		//		
			}
			sn.close();
			Log.i("Loading", "Done");
			sn = new Scanner(am.open("lon.txt"));
			Log.i("Loading", "Longitudes");
			while(sn.hasNextBigDecimal()){
				arrLon.add(sn.nextDouble());
		//		
			}
			sn.close();
			Log.i("Loading", "Done");
			sn = new Scanner(am.open("sNames.txt"));
			Log.i("Loading", "Names");
			while(sn.hasNextLine()){
				Schools.add(sn.nextLine());
			//	Log.i("Loading", ".");
			}
			sn.close();
			Log.i("Loading", "Done");
			Log.i("Setting", "Spawns");
			for(int i=0;i<arrLat.size();i++){
				this.mMap.addMarker(new MarkerOptions().position(new LatLng(arrLat.get(i),arrLon.get(i))).title(Schools.get(i)+" | Z-Pop="+Math.round(Math.random()*1000)));
			}
			Log.i("Setting", "Done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	private LatLng getLatLng(){
		return new LatLng(
			this.lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(),
				this.lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude());
	}
}
