package uow.hsckWE.WGZ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;

public class MapView extends View{
	public static int RADIUS = 5;
	
	private DoublePoint playerGpsLocation;
	private Paint mapPaint;
	private Path garbageZones;
	private ArrayList<DoublePoint> spawnUtmLocations;
	private ArrayList<Point> spawnLocations;
	private Point playerPosition;
	private double minX, minY;
	//private DoublePoint maxLocationUtm;
	private double multiplier;
	private int xOffset, yOffset;
	
	private boolean offsetsCalculated = false;
	
	public class DoublePoint {
		 public double x;
		 public double y;
		 public DoublePoint(double x, double y) {
			 super();
			 this.x=x;
			 this.y=y;
		 }
		 
		 public DoublePoint() {
			 super();
		 }
	 };
		
	public MapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setBackgroundColor(0x80000000);
		//this.setBackgroundResource(R.drawable.dead_city);
		initPaint();
	}
	
	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		updateScreenLocations();
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		Point point;
		
		// drawing garbage zones
		mapPaint.setColor(Color.BLUE);
		mapPaint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(garbageZones, mapPaint);
		
		// drawing spawn locations
		mapPaint.setColor(Color.RED);
		mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		for (int i=0;i<spawnLocations.size();i++) {
			point=spawnLocations.get(i);
			canvas.drawCircle(point.x, point.y, MapView.RADIUS, mapPaint);
		}
		
		// drawing player position
		mapPaint.setColor(Color.GREEN);
		mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		point = playerPosition;
		canvas.drawCircle(point.x, point.y, MapView.RADIUS, mapPaint);
	}
	
	private void initPaint() {
		mapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
		
	public void updateScreenLocations() {
		
		updatePlayerPosition();
		updateSpawnLocations();
		garbageZones=getPathFromPoints(getGarbageZonesPoints());
		updateSpawnLocations();
	}
	
	public void updatePlayerPosition() {
		if (!offsetsCalculated) {
			playerPosition=null;
			return;
		}
		playerPosition = new Point();
		playerPosition.x=convertCoordinate(playerGpsLocation.x, minX, multiplier, xOffset);
		playerPosition.y=convertCoordinate(playerGpsLocation.y, minY, multiplier, yOffset);
		invalidate();
	}
	
	public void setNewPlayerGpsLocation(Location location) {
		playerGpsLocation = convertLatLonToUTM(location.getLatitude(),location.getLongitude());
		updatePlayerPosition();
	}
	
	
	
	private void getSpawnLocationsUTM() {
		spawnUtmLocations = new ArrayList<DoublePoint>();
		ArrayList<Double> xArray;
		ArrayList<Double> yArray;
		xArray = readDoubleArray(R.raw.spawns_x);
		yArray = readDoubleArray(R.raw.spawns_y);
		int size = Math.min(xArray.size(), yArray.size());
		DoublePoint point;
		
		for (int i=0;i<size;i++) {
			point = new DoublePoint();
			point.x=xArray.get(i);
			point.y=yArray.get(i);
			spawnUtmLocations.add(point);
		}
	}
	
	public void updateSpawnLocations() {
		calculateOffsets();
		spawnLocations = new ArrayList<Point>();
		Point  point;
		DoublePoint pointUtm;
		int size = spawnUtmLocations.size();
		for (int i=0;i<size;i++) {
			pointUtm = spawnUtmLocations.get(i);
			
			point = new Point();
			point.x=convertCoordinate(pointUtm.x, minX, multiplier, xOffset);
			point.y=convertCoordinate(pointUtm.y, minY, multiplier, yOffset);
			
			spawnLocations.add(point);
		}
	}
	
	private void calculateOffsets() {
		offsetsCalculated=false;
		DoublePoint minLocationUtm = arrayMinValue(spawnUtmLocations);
		DoublePoint maxLocationUtm = arrayMaxValue(spawnUtmLocations);
		double maxX = maxLocationUtm.x;
		double maxY = maxLocationUtm.y;
		minX = minLocationUtm.x;
		minY = minLocationUtm.y;
		Double difX = maxX-minX;
		Double difY = maxY-minY;
		Double divXY = difX/difY;
		int width = this.getWidth();
		int height = this.getHeight();
		Double divWH = (double)width/height;
		
		//Log.d("MapView","difX="+difX);
		//Log.d("MapView","difY="+difY);
		
		//Log.d("MapView","width="+width);
		//Log.d("MapView","height="+height);
		
		//Log.d("MapView","divXY="+divXY);
		//Log.d("MapView","divWH="+divWH);
		
		if (divXY>divWH) {
			multiplier = width/difX;
			xOffset = 0;
			yOffset = (height-(int)Math.round(difY*multiplier))/2;
		} else {
			multiplier = height/difY;
			yOffset=0;
			xOffset=(width-(int)Math.round(difX*multiplier))/2;
		}
		
		//Log.d("MapView","mult="+mult);
		//Log.d("MapView","xOffset="+xOffset);
		//Log.d("MapView","yOffset="+yOffset);
		
		offsetsCalculated=true;
	}
	
	public ArrayList<Point> getGarbageZonesPoints() {
		ArrayList<Point> garbageZonesPoints = new ArrayList<Point>();
		// points to get
		return garbageZonesPoints;
	}
	
	public DoublePoint arrayMinValue(ArrayList<DoublePoint> array) {
		DoublePoint min=new DoublePoint(Double.MAX_VALUE,Double.MAX_VALUE);
		DoublePoint p;
		for (int i=0;i<array.size();i++) {
			p = array.get(i);
			if (p.x<min.x)
				min.x = p.x;
			if (p.y<min.y)
				min.y=p.y;
		}
		return min;
	}
	
	public DoublePoint arrayMaxValue(ArrayList<DoublePoint> array) {
		DoublePoint max=new DoublePoint(Double.MIN_VALUE,Double.MIN_VALUE);
		DoublePoint p;
		for (int i=0;i<array.size();i++) {
			p = array.get(i);
			if (p.x>max.x)
				max.x = p.x;
			if (p.y>max.y)
				max.y=p.y;
		}
		return max;
	}
	
	public int convertCoordinate(double x, double start, double mult, int offset) {
		int ans;
		ans = (int)Math.round((x-start)*mult)+offset;
		return ans;
	}
	
	public ArrayList<Double> readDoubleArray(int res) {
		ArrayList<Double> array = new ArrayList<Double>();
		InputStream stream = this.getResources().openRawResource(res);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		Double val;
		try {
			for (line=reader.readLine();line!=null;line=reader.readLine()) {
				val = Double.parseDouble(line);
				array.add(val);
			}
		} catch (IOException e) {
			// DO NOTHING
		}
		return array;
	}
	
	private Path getPathFromPoints(ArrayList<Point> pointsArray) {
		Path path=new Path();
		Point p;
		if (pointsArray.size()<2)
			return path;
		p=pointsArray.get(0);
		path.moveTo(p.x, p.y);
		for (int i=1;i<pointsArray.size();i++) {
			p=pointsArray.get(i);
			path.lineTo(p.x, p.y);
		}
		return path;
	}
	
	public DoublePoint convertLatLonToUTM(double latitude, double longitude) {
		DoublePoint point = new DoublePoint();
		double lat = Math.toRadians(latitude);
		double lon = Math.toRadians(longitude);
		double a = 6378.137;
		double f = 1/298.257223563;
		double k0 = 0.9996;
		double E0 = 500;
		double N0 = 0;
		double lon0=Math.toRadians(17);
		
		double n = f/(2-f);
		double A = a/(1+n)*(1+n*n/4+n*n*n/64);
		double[] alpha = new double[3];
		alpha[0] = 1/2*n-2/3*n*n+5/16*n*n*n;
		alpha[1] = 13/48*n*n-3/5*n*n*n;
		alpha[2] = 61/240*n*n*n;
		double[] beta = new double[3];
		beta[0] = 1/2*n-2/3*n*n+37/96*n*n*n;
		beta[1] = 1/48*n*n+1/15*n*n*n;
		beta[2] = 17/480*n*n*n;
		double[] delta = new double[3];
		delta[0] = 2*n-2/3*n*n-2*n*n*n;
		delta[1] = 7/3*n*n-8/5*n*n*n;
		delta[2] = 56/15*n*n*n;
		
			
		
		double t = Math.sinh(tanhInv(Math.sin(lat))
							-2*Math.sqrt(n)/(1+n)*tanhInv(2*Math.sqrt(n)/(1+n)*Math.sin(lat)));
		double xi = Math.atan(t/Math.cos(lon-lon0));
		double eta = tanhInv(Math.sin(lon-lon0)/Math.sqrt(1+t*t));
		double sigma = 1;
		double tau = 0;
		double E1 = eta;
		double N1 = xi;
		for (int j=0;j<3;j++) {
			sigma+=2*(j+1)*alpha[j]*Math.cos(2*(j+1)*xi)*Math.cosh(2*(j+1)*eta);
			tau+=2*(j+1)*alpha[j]*Math.sin(2*(j+1)*xi)*Math.sinh(2*(j+1)*eta);
			E1+=alpha[j]*Math.cos(2*(j+1)*xi)*Math.sinh(2*(j+1)*eta);
			N1+=alpha[j]*Math.sin(2*(j+1)*xi)*Math.cosh(2*(j+1)*eta);
		}
		E1*=k0*A;
		N1*=k0*A;
		
		double E = E0+E1;
		double N = N0+N1;
		//double k = k0*A/a*Math.sqrt((1+Math.pow((1-n/(1+n)*Math.tan(lat)), 2))*(sigma*sigma+tau*tau)/(t*t+Math.pow(Math.cos(lon-lon0),2))); 
		
		//double b = 6356752.3142;
		//double n = (a-b)/(a+b);
		//double e = 0.08;
		
		//double A1 = a * (1-n+(5/4)*n*n*(1-n)+(81/64)*n*n*n*n*(1-n));
		/*double M = a*(
				(1-e*e/4-3*e*e*e*e/64-5*e*e*e*e*e*e/256)*lat
				-(3*e*e/8+3*e*e*e*e/32+45*e*e*e*e*e*e/1024)*Math.sin(2*lat)
				+(15*e*e*e*e/256+45*e*e*e*e*e*e/1024)*Math.sin(4*lat)
				-(35*e*e*e*e*e*e/3072)*Math.sin(6*lat));
		*/
		
		point.x=E;
		point.y=N;
		
		return point;
	}
	
	public double tanhInv(double x) {
		return 1/2*Math.log((1+x)/(1-x));
	}
	
}
